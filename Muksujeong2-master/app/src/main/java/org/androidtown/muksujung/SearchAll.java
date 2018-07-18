package org.androidtown.muksujung;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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

public class SearchAll extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.POIItemEventListener  {
    MapView mapView;
    ListView listView;
    StoreAdapter mAdapter;
    EditText keywordView;
    MapPOIItem marker;
    Button currbtn; // 현재위치버튼
    Button search_btn; // 검색버튼
    NaverStores resultcopy;
    GeoItem geoArray[] = null;

    // 좌표계변환
    List<Address> listgeo = null;
    Geocoder geocoder;
    double longi, lati;

    int selectedmarkerIdx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);

        //다음이 제공하는 MapView객체 생성 및 API Key 설정
        mapView = new MapView(this);
        mapView.setDaumMapApiKey("API_KEY");
        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        LinearLayout container = (LinearLayout) findViewById(R.id.searchall_map_view);
        container.addView(mapView);

        mapView.setCurrentLocationEventListener(this);
        mapView.setPOIItemEventListener(this);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5913103, 127.0199425), true);    // 처음 화면을 학교로 고정하여 보여줌()

        // 현위치 트래킹 모드 및 나침반 모드를 설정한다.
        // 현위치 트래킹/나침반 모드를 활성화 시키면 현위치 정보가 설정된 MapView.CurrentLocationEventListener 객체에 전달된다.
        //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);


        // 기본마커추가

//        marker = new MapPOIItem();
//        marker.setItemName("Default Marker");
//        marker.setTag(0);
//        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633));
//        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
//        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//
//        mapView.addPOIItem(marker);

        keywordView = (EditText) findViewById(R.id.searchall_restaurant_search_text);
        listView = (ListView) findViewById(R.id.searchall_restaurant_listview);
        //mAdapter = new ArrayAdapter<MovieItem>(this, android.R.layout.simple_list_item_1);
        mAdapter = new StoreAdapter();
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        currbtn = (Button) findViewById(R.id.searchall_current_location_button);

        // 현재위치로 이동 버튼
        currbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeadingWithoutMapMoving);
            }
        });

        // edittext 엔터 버튼 처리
        keywordView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search_btn.performClick();
                    getCurrentFocus();
                    return true;
                }
                return false;
            }
        });

        // 검색버튼
        search_btn = (Button) findViewById(R.id.searchall_restaurant_search_button);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                keywordView.clearFocus();
                mAdapter.notifyDataSetChanged();

                String keyword = keywordView.getText().toString();

                NaverStoreRequest request = new NaverStoreRequest("성신여대 "+ keyword);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<NaverStores>() {
                    @Override
                    public void onSuccess(NetworkRequest<NaverStores> request, NaverStores result) {
                        mAdapter.addAll(result.items);
                        resultcopy = result;
                        geoArray = new GeoItem[result.items.size()]; // 좌표값 이용 편하게 하기 위해 따로 저장해두려함.

                        int i = 0; // 배열인덱스이자 리스트의 순서
                        for (StoreItem item : result.items) {
                            try {
                                geocoder = new Geocoder(SearchAll.this);
                                listgeo = geocoder.getFromLocationName(item.address, 10);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.i("address로 경/위도찾기", " fail");
                            }
                            Log.i("SearchAll.java", listgeo.toString());

                            longi = listgeo.get(0).getLongitude();
                            lati = listgeo.get(0).getLatitude();
                            Log.i("SearchAll.java", "lat:" + lati + "& longi:" + longi);

                            geoArray[i] = new GeoItem(item.title, lati, longi, marker);
                            geoArray[i].marker = new MapPOIItem();
                            geoArray[i].marker.setItemName(item.title);
                            geoArray[i].marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lati, longi));
                            geoArray[i].marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                            geoArray[i].marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                            Log.i("SearchAll.java 변환된 경위도값", lati + ", " + longi);

                            mapView.addPOIItem(geoArray[i].marker);
                            Log.i("SearchAll.java geoArray 로그찍기", "현재 인덱스: " + i + "geoArray[" + i + "] = " + geoArray[i].toString());
                            i++;
                        }
                    }

                    @Override
                    public void onFailure(NetworkRequest<NaverStores> request, int errorCode, int responseCode, String message, Throwable exception) {
                        Toast.makeText(SearchAll.this, "fail", Toast.LENGTH_SHORT).show();
                        Log.i("SearchAll.java", "responseCode : " + responseCode);
                    }
                });
            }
        });

//        // 상세페이지로 이동
//        final AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                pos = listView.getCheckedItemPosition();
//                if(pos != -1){
//                    Log.d("Restaurants.java", "link= " + mAdapter.getItem(pos).link);
//
//                    String url = mAdapter.getItem(pos).link;
//
//                    if(url != null){
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(intent);
//                    }
//                    else
//                        Toast.makeText(SearchAll.this, "링크없음", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };

        // 상세페이지로 이동
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
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        listView.setOnItemClickListener(listener);

    }


    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

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

        for(StoreItem item : resultcopy.items){
            if(item.title.equals(markertitle)) {
                if(item.link != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.link)));
                }
                else
                    Toast.makeText(SearchAll.this, "링크없음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
