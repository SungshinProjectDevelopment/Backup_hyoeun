//제일최근것.180405

package org.androidtown.muksujung;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button goout,school,findmine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goout = (Button)findViewById(R.id.goout);
        school = (Button)findViewById(R.id.school);
        findmine = (Button)findViewById(R.id.findmine);
    }

    public void onButton(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.goout:
                i = new Intent(this,GoOut.class);
                startActivity(i);
                break;
            case R.id.school:
                i = new Intent(this,School.class);
                startActivity(i);
                break;
            case R.id.findmine:
                i = new Intent(this,FindMine.class);
                startActivity(i);
                break;
        }
    }
}