package org.androidtown.muksujung;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddMine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mine);

        Button addmine = (Button) findViewById(R.id.btn_addmine);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_addmine) {
            EditText titlemine = (EditText) findViewById(R.id.text_addmine_posttitle);
            finish();
        }
    }

}


