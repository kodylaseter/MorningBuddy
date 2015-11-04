package com.example.song.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.song.myapplication.adapters.AlarmAdapter;
import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.models.Alarm;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView alarmListView;
    private AlarmDBHelper alarmDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        alarmListView = (ListView) findViewById(R.id.alarmListView);
        alarmDBHelper = AlarmDBHelper.getInstance(this);
        AlarmAdapter adapter = new AlarmAdapter(this, alarmDBHelper.getAlarms());
        alarmListView.setAdapter(adapter);

    }

    public void newAlarmButtonClicked (View view) {
        Intent intent = new Intent(this, NewAlarmActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.weather_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
