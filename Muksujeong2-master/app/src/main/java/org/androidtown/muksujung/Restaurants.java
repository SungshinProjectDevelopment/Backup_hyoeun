package org.androidtown.muksujung;

import android.content.Intent;
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

import net.daum.mf.map.api.MapView;

public class Restaurants extends AppCompatActivity {
    //private MapView mapView;

    ListView listView;
    //    ArrayAdapter<MovieItem> mAdapter;
    StoreAdapter mAdapter;
    EditText keywordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);


        //다음이 제공하는 MapView객체 생성 및 API Key 설정
        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("API_KEY");
        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        LinearLayout container = (LinearLayout) findViewById(R.id.map_view);
        container.addView(mapView);

        keywordView = (EditText)findViewById(R.id.restaurant_search_text);
        listView = (ListView)findViewById(R.id.restaurant_listview);
//        mAdapter = new ArrayAdapter<MovieItem>(this, android.R.layout.simple_list_item_1);
        mAdapter = new StoreAdapter();
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 검색버튼
        Button btn = (Button)findViewById(R.id.restaurant_search_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.notifyDataSetChanged();

                String keyword = keywordView.getText().toString();

                NaverStoreRequest request = new NaverStoreRequest(keyword);
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<NaverStores>() {
                    @Override
                    public void onSuccess(NetworkRequest<NaverStores> request, NaverStores result) {
//                                                   for (StoreItem item : result.items) {
//                                mAdapter.add(item);
//                            }
                        mAdapter.addAll(result.items);
                    }

                    @Override
                    public void onFailure(NetworkRequest<NaverStores> request, int errorCode, int responseCode, String message, Throwable exception) {
                        Toast.makeText(Restaurants.this, "fail", Toast.LENGTH_SHORT).show();
                        Log.i("Restaurants.java", "responseCode : " + responseCode);

                    }
                });
            }
        });

        // 상세페이지로 이동
        final AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                pos = listView.getCheckedItemPosition();
                if(pos != -1){
                    Log.d("Restaurants.java", "link= " + mAdapter.getItem(pos).link);

                    String url = mAdapter.getItem(pos).link;

                    if(url != null){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Restaurants.this, "링크없음", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        listView.setOnItemClickListener(listener);

    }
}
