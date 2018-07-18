package org.androidtown.muksujung;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;


public class Restaurants extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.POIItemEventListener {
    MapView mapView;
    ListView listView;
    StoreAdapter mAdapter;
    EditText destTitle;
    MapPOIItem marker;
    Button currbtn; // 현재위치버튼
    Button search_btn; // 검색버튼
    Button select_destination_btn;
    Button selectdst_btn;
    int selectedDstPos = -1;
    NaverStores resultcopy;
    GeoItem geoArray[] = null;

    // 좌표계변환
    List<Address> listgeo = null;
    Geocoder geocoder;
    double longi, lati;

    // 현위치 좌표 get
    double latitude4route;
    double longitude4route;
    LocationManager manager;
    GPSListener gpsListener;
    Location lastLocation;

    int selectedmarkerIdx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        //다음이 제공하는 MapView객체 생성 및 API Key 설정
        mapView = new MapView(this);
        mapView.setDaumMapApiKey("API_KEY");
        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        LinearLayout container = (LinearLayout) findViewById(R.id.map_view);
        container.addView(mapView);
//        mapView.setCurrentLocationEventListener(this);
        mapView.setPOIItemEventListener(this);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5913103, 127.0199425), true);    // 처음 화면을 학교로 고정하여 보여줌()
//        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);


        // 현위치 트래킹 모드 및 나침반 모드를 설정한다.
        // 현위치 트래킹/나침반 모드를 활성화 시키면 현위치 정보가 설정된 MapView.CurrentLocationEventListener 객체에 전달된다.
        //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

//        destTitle = (EditText) findViewById(R.id.restaurant_dest_title_text);
        listView = (ListView) findViewById(R.id.restaurant_listview);
        //mAdapter = new ArrayAdapter<MovieItem>(this, android.R.layout.simple_list_item_1);
        mAdapter = new StoreAdapter();
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        currbtn = (Button) findViewById(R.id.current_location_button);
        selectdst_btn = (Button) findViewById(R.id.restaurant_select_destination_button);

        // gps수신
        gpsListener = new GPSListener();

        // 현재위치로 이동 버튼
        currbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
            }
        });

        // 목적지 설정하는 버튼
        selectdst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                // 목적지 설정 버튼
                if (selectedDstPos != -1) { // 선택된게 있으면
                    double latitude = 37.5909707, longitude = 127.0228875;

                    String desturl = "daummaps://route?sp=" + latitude + "," + longitude + "&ep=" + geoArray[selectedDstPos].geolati + "," + geoArray[selectedDstPos].geolongi + "&by=FOOT";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(desturl));
                    startActivity(intent);
                } else
                    Toast.makeText(Restaurants.this, "목적지를 먼저 설정하세요!", Toast.LENGTH_SHORT);
            }
        });

        // edittext 엔터 버튼 처리
//        keywordView.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    search_btn.performClick();
//                    getCurrentFocus();
//                    return true;
//                }
//                return false;
//            }
//        });

        // 검색안누르고 바로 뜨게
        mAdapter.notifyDataSetChanged();
        Intent i = getIntent();
        String keyword = i.getStringExtra("theme_keyword").toString();

        NaverStoreRequest request = new NaverStoreRequest(keyword);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<NaverStores>() {
            @Override
            public void onSuccess(NetworkRequest<NaverStores> request, NaverStores result) {
                resultcopy = result;
                mAdapter.addAll(result.items);
                geoArray = new GeoItem[result.items.size()]; // 좌표값 이용 편하게 하기 위해 따로 저장해두려함.

                int i = 0; // 배열인덱스이자 리스트의 순서
                for (StoreItem item : result.items) {
                    try {
                        geocoder = new Geocoder(Restaurants.this);
                        listgeo = geocoder.getFromLocationName(item.address, 3);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i("Restaurants.java", "Restaurants.java_ address로 경/위도찾기 fail");
                    }
                    Log.i("Restaurants.java", listgeo.toString());

                    longi = listgeo.get(0).getLongitude();
                    lati = listgeo.get(0).getLatitude();

                    geoArray[i] = new GeoItem(item.title, lati, longi, marker);
                    geoArray[i].marker = new MapPOIItem();
                    geoArray[i].marker.setItemName(item.title);
                    geoArray[i].marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lati, longi));
                    geoArray[i].marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                    geoArray[i].marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                    Log.i("Restaurant.java 변환된 경위도값", lati + ", " + longi);

                    mapView.addPOIItem(geoArray[i].marker);
                    Log.i("Restaurant.java", "geoArray 로그찍기 - 현재 인덱스: " + i + "geoArray[" + i + "] = " + geoArray[i].toString());
                    i++;
                }
            }

            @Override
            public void onFailure(NetworkRequest<NaverStores> request, int errorCode, int responseCode, String message, Throwable exception) {
                Toast.makeText(Restaurants.this, "fail", Toast.LENGTH_SHORT).show();
                Log.i("Restaurants.java", "responseCode : " + responseCode);
            }
        });

        // 리스트뷰를 누르면 해당 마커가 Redpin으로 selected되게 하기
        final AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                pos = listView.getCheckedItemPosition();
                if (pos != -1) {
                    if (selectedmarkerIdx != -1) { // 마커중에 선택중인거 있음
                        mapView.deselectPOIItem(geoArray[selectedmarkerIdx].marker); // 선택되었던 마커를 다시 블루핀으로.
                        mapView.selectPOIItem(geoArray[pos].marker, true);// 지금 선택한 리스트뷰의 해당마커 레드로
                    } else { // selectedmarkerIdx == -1, 마커들 중에 선택 중인것 없음
                        mapView.selectPOIItem(geoArray[pos].marker, true);// 지금 선택한 리스트뷰의 해당마커 레드로
                    }
                    selectedmarkerIdx = pos;
                    selectedDstPos = pos;
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        listView.setOnItemClickListener(listener);
    }


    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        // 단말의 현위치 좌표값을 통보받을 수 있다.

        Toast.makeText(this, mapPoint.getMapPointGeoCoord().toString(), Toast.LENGTH_SHORT).show();
//        latitude4route = mapPoint.getMapPointGeoCoord().latitude;
//        longitude4route = mapPoint.getMapPointGeoCoord().longitude;
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    // 마커 이벤트
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        String markertitle = mapPOIItem.getItemName();

        for (StoreItem item : resultcopy.items) {
            if (item.title.equals(markertitle)) {
                if (item.link != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.link)));
                } else
                    Toast.makeText(Restaurants.this, "링크없음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    public void startLocationService(){

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsListener = new GPSListener();
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, gpsListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, gpsListener);
            lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                double latitude = lastLocation.getLatitude();
                double longitude = lastLocation.getLongitude();
                latitude4route = latitude;
                longitude4route = longitude;
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    // 위치리스너
    private class GPSListener implements LocationListener {         // 위치 정보가 확인될 때 자동 호출되는 메소드

        public void onLocationChanged(Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            latitude4route = latitude;
            longitude4route = longitude;

            Log.i("GPSListener", "Latitude : " + latitude4route + "\nLongitude:" + longitude4route);

            Toast.makeText(getApplicationContext(), "Latitude : " + latitude + "\nLongitude:" + longitude, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }


}

