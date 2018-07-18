package org.androidtown.muksujung;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button findmine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findmine = (Button)findViewById(R.id.findmine);
    }

    public void onButton(View v) {
        Intent i = new Intent(this,Restaurants.class);
        switch (v.getId()){

            case R.id.alone:
                i.putExtra("theme_keyword", "성신여대 혼밥");
                startActivity(i);
                break;
            case R.id.date:
                i.putExtra("theme_keyword", "성신여대 데이트");
                startActivity(i);
                break;
            case R.id.family:
                i.putExtra("theme_keyword", "성신여대 가족식사");
                startActivity(i);
                break;
            case R.id.brunch:
                i.putExtra("theme_keyword", "성신여대 브런치");
                startActivity(i);
                break;
            case R.id.bakery:
                i.putExtra("theme_keyword", "성신여대 베이커리");
                startActivity(i);
                break;
            case R.id.group:
                i.putExtra("theme_keyword", "성신여대 회식");
                startActivity(i);
                break;
            case R.id.searchAll:
                Intent i2 = new Intent(this, SearchAll.class);
                startActivity(i2);
                break;
            case R.id.findmine:
                i = new Intent(this,MineList.class);
                startActivity(i);
                break;
        }
    }
}