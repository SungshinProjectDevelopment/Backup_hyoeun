package org.androidtown.muksujung;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import net.daum.mf.map.api.MapView;

public class Restaurants extends AppCompatActivity {
    //private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

//        MapLayout mapLayout = new MapLayout(this);
//        MapView mapView = mapLayout.getMapView(); // 선언
//        mapView.setMapType(MapView.MapType.Standard);
//
//
//        mapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
//
////        mapView = new MapView(this);
//        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
//        mapViewContainer.addView(mapLayout);

        //다음이 제공하는 MapView객체 생성 및 API Key 설정
        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("API_KEY");
        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        LinearLayout container = (LinearLayout) findViewById(R.id.map_view);
        container.addView(mapView);




    }
}
