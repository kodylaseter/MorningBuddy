package com.example.song.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by song on 10/19/2015 0019.
 */
public class MainActivity extends AppCompatActivity {

    static String position, newAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        newAlarm = "N/A";
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void newButtonClicked (View view) {
        //Intent intent = new Intent(this, NewAlarm.class);
        //intent.putExtra("newAlarm", newAlarm);
        //intent.putExtra("id", position);
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
    }
}