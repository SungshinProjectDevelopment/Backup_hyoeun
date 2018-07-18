package org.androidtown.muksujung;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AddMine extends AppCompatActivity {

    private final String TAG = "AddMine";
    Toolbar toolbar;
    Button applyMine, closeBtn;
    EditText posttitle_et, storename_et, body_et;
    RatingBar rating_bar;
    String posttitle;
    String storename;
    String body;
    String rating;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mine);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        toolbar.setTitleTextColor(Color.parseColor("#ffff33")); //제목의 칼라
//        toolbar.setSubtitle("부제목"); //부제목 넣기
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher); //제목앞에 아이콘 넣기
        setSupportActionBar(toolbar);

        posttitle_et = findViewById(R.id.addmine_posttitle);
        storename_et = findViewById(R.id.addmine_storename);
        body_et = findViewById(R.id.addmine_postbody);
        rating_bar = findViewById(R.id.addmine_ratingbar);

        applyMine = (Button)findViewById(R.id.btn_apply);
        applyMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posttitle = posttitle_et.getText().toString();
                storename = storename_et.getText().toString();
                rating = String.valueOf(rating_bar.getRating());
                body = body_et.getText().toString();

                if(posttitle.length()!=0 && storename.length()!=0  && body.length()!=0 ){   // 셋다 null이 아닐때
                    //ConnectionForApplyMine();   // 내용을 DB에 저장하고

                    Intent result = new Intent();
                    result.putExtra("posttitle", posttitle);
                    result.putExtra("storename", storename);
                    result.putExtra("rating", rating);
                    result.putExtra("body", body);
                    result.putExtra("writerid", "익명5");
                    setResult(RESULT_OK, result);
//                    Toast.makeText(AddMine.this, "저장시작!\n"+posttitle+"\n"+storename +"\n"+body+"\n"+rating, Toast.LENGTH_SHORT).show();

                    finish();
                }
                else{
                    Toast.makeText(AddMine.this, "msg: 빈칸을 채워야 글을 쓸수 있습니다!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        closeBtn = (Button)findViewById(R.id.btn_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("글쓰기 종료");
                alertDialogBuilder.setMessage("글쓰기를 종료할 것입니까?").
                        setCancelable(false).
                        setPositiveButton("종료",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        AddMine.this.finish();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // 다이얼로그 생성, 보여주기
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    private void ConnectionForApplyMine(){
        final String SIGNIN_URL = "http://13.209.48.149:8080/testdir/connection_addmine.jsp";
        final String urlSuffix = "?posttitle="+posttitle+"&restaurantname="+storename+"&rating="+rating+"&postbody="+body+"&writerid="+"익명3";

        Log.d("urlSuffix", urlSuffix);
        class SignupUser extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d(TAG,s);
                if (s != null) {
                    try{
                        Toast.makeText(AddMine.this, "서버와의 통신 중1", Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AddMine.this, "서버와의 통신에 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;

                try {

                    HttpClient client = new DefaultHttpClient();  // 보낼 객체 만들기
                    HttpPost post = new HttpPost(SIGNIN_URL + urlSuffix);  // 주소 뒤에 데이터를 넣기

                    HttpResponse response = client.execute(post); // 데이터 보내기
                    Toast.makeText(AddMine.this, "서버와의 통신 중2", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddMine.this, response.toString(), Toast.LENGTH_SHORT).show();

                    BufferedReader bufreader = new BufferedReader(
                            new InputStreamReader(
                                    response.getEntity().getContent(), "EUC-KR"));

                    String line = null;
                    String page = "";

                    while ((line = bufreader.readLine()) != null) {
                        page += line;
                    }
                    Log.d(TAG, page);
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