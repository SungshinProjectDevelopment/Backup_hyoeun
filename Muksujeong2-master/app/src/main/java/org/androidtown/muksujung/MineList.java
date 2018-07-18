package org.androidtown.muksujung;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MineList extends AppCompatActivity {
    ListView listView;
    ArrayList<MPostItem> mineitem;
    MPostItem pi;
    MineListAdapter MyAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_list);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        toolbar.setTitleTextColor(Color.parseColor("#ffff33")); //제목의 칼라
//        toolbar.setSubtitle("부제목"); //부제목 넣기
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher); //제목앞에 아이콘 넣기
        setSupportActionBar(toolbar);

        mineitem = new ArrayList<MPostItem>();
        pi = new MPostItem("벌레나옴", "식당1", "1.5", "1", "김 미고랭", "여기 매일왔는데 이번에 벌레나왔어요! ㅡㅡ");
        mineitem.add(pi);
        MyAdapter = new MineListAdapter(this, R.layout.minelistitem, mineitem);

        listView = (ListView) findViewById(R.id.minelist);
        listView.setAdapter(MyAdapter);
        GetMines();

        Button resetlist = findViewById(R.id.refresh_minelist);
        resetlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMines();
            }
        });

        Button new_mine = (Button) findViewById(R.id.btn_add_mine);
        new_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MineList.this, AddMine.class);
                startActivityForResult(i, 101);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if(resultCode==RESULT_OK){
                pi = new MPostItem(data.getStringExtra("posttitle"),
                        data.getStringExtra("storename"),
                        data.getStringExtra("rating"), "1",
                        data.getStringExtra("writerid"),
                        data.getStringExtra("body"));
                mineitem.add(pi);
                MyAdapter.notifyDataSetChanged();
            }
        }
    }


    private void GetMines() {

        final String SIGNIN_URL = "http://13.209.48.149:8080/testdir/connect_minelist.jsp";
        final String urlSuffix = "?hits=1";
        Log.d("urlSuffix", urlSuffix);

        class SignupUser extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {

                    try {
//                        Toast.makeText(MineList.this, "HERE", Toast.LENGTH_SHORT).show();

                        JSONArray jArr = new JSONArray(s);
                        JSONObject json = new JSONObject();

                        for (int i = 0; i < jArr.length(); i++) {
                            json = jArr.getJSONObject(i);
                            pi = new MPostItem(json.getString("posttitle"),
                                    json.getString("restaurantname"),
                                    json.getString("rating"),
                                    json.getString("hits"),
                                    json.getString("writerid"),
                                    json.getString("postbody"));
                            mineitem.add(pi);
                            MyAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(MineList.this, "서버와의 통신에 문제가 발생했습니다", Toast.LENGTH_SHORT).show();

            }

            @Override
            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;

                try {
                    HttpClient client = new DefaultHttpClient();  // 보낼 객체 만들기
                    HttpPost post = new HttpPost(SIGNIN_URL + urlSuffix);  // 주소 뒤에 데이터를 넣기

                    HttpResponse response = client.execute(post); // 데이터 보내기

                    BufferedReader bufreader = new BufferedReader(
                            new InputStreamReader(
//                                    response.getEntity().getContent(), "utf-8"));
                                    response.getEntity().getContent(), "EUC-KR"));

                    String line = null;
                    String page = "";

                    while ((line = bufreader.readLine()) != null) {
                        page += line;
                    }
                    return page;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        SignupUser su = new SignupUser();
        su.execute(urlSuffix);
    }

}

class MineListAdapter extends BaseAdapter {
    Context maincon;
    LayoutInflater Inflater;
    ArrayList<MPostItem> mineItems;
    int layout;

    public MineListAdapter(Context context, int alayout, ArrayList<MPostItem> mineItems) {
        this.maincon = context;
        this.Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mineItems = mineItems;
        this.layout = alayout;
    }

    @Override
    public int getCount() {
        return mineItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mineItems.get(i).posttitle;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        if (view == null)
            view = Inflater.inflate(layout, viewGroup, false);

        TextView posttitle = (TextView) view.findViewById(R.id.minelistitem_posttitle);
        posttitle.setText(mineItems.get(position).posttitle);

        TextView restaurantname = (TextView) view.findViewById(R.id.minelistitem_restaurantname);
        restaurantname.setText(mineItems.get(position).restaurantname);

        TextView rating = (TextView) view.findViewById(R.id.minelistitem_rating);
        rating.setText(mineItems.get(position).rating);

        TextView hits = (TextView) view.findViewById(R.id.minelistitem_hits);
        hits.setText(mineItems.get(position).hits);

        TextView writerid = (TextView) view.findViewById(R.id.minelistitem_writer);
        writerid.setText(mineItems.get(position).writerid);

        TextView postbody = (TextView) view.findViewById(R.id.minelistitem_postbody);
        postbody.setText(mineItems.get(position).postbody);

        return view;
    }
}
