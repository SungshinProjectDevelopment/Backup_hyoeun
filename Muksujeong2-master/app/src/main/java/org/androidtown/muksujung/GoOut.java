package org.androidtown.muksujung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GoOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_out);
    }

    public void onButton(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.alone:
                i = new Intent(this,Restaurants.class);
                startActivity(i);
                break;
            case R.id.date:
                i = new Intent(this,Restaurants.class);
                startActivity(i);
                break;
            case R.id.family:
                i = new Intent(this,Restaurants.class);
                startActivity(i);
                break;
            case R.id.calm:
                i = new Intent(this,Restaurants.class);
                startActivity(i);
                break;
            case R.id.fancy:
                i = new Intent(this,Restaurants.class);
                startActivity(i);
                break;
            case R.id.group:
                i = new Intent(this,Restaurants.class);
                startActivity(i);
                break;
        }
    }
}
