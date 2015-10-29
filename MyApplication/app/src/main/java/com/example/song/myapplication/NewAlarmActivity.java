package com.example.song.myapplication;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.models.Alarm;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by song on 10/19/2015 0019.
 */
public class NewAlarmActivity extends AppCompatActivity {
    AlarmDBHelper alarmDBHelper;
    EditText name;
    EditText trafficLocation;
    Switch trafficSwitch;
    Switch weatherSwitch;
    EditText weatherLocation;
    EditText trafficDestination;
    TimePicker timePicker;
    int alarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        alarmDBHelper = AlarmDBHelper.getInstance(this);
        name = (EditText)findViewById(R.id.nameText);
        trafficSwitch = (Switch)findViewById(R.id.trafficSwitch);
        weatherSwitch = (Switch)findViewById(R.id.weatherSwitch);
        trafficLocation = (EditText)findViewById(R.id.trafficLocation);
        trafficDestination = (EditText)findViewById(R.id.trafficDestination);
        weatherLocation = (EditText)findViewById(R.id.weatherLocation);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        alarmTime = Calendar.getInstance().getTime().getHours() * 60 + Calendar.getInstance().getTime().getMinutes();

        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.trafficSwitch) {
                    if (isChecked) {
                        trafficDestination.setVisibility(View.VISIBLE);
                        trafficLocation.setVisibility(View.VISIBLE);
                    } else {
                        trafficDestination.setVisibility(View.GONE);
                        trafficLocation.setVisibility(View.GONE);
                    }
                } else if (buttonView.getId() == R.id.weatherSwitch) {
                    if (isChecked) {
                        weatherLocation.setVisibility(View.VISIBLE);
                    } else {
                        weatherLocation.setVisibility(View.GONE);
                    }
                }
            }
        };

        TimePicker.OnTimeChangedListener timePickerListener = new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        };

        timePicker.setOnTimeChangedListener(timePickerListener);
        trafficSwitch.setOnCheckedChangeListener(switchListener);
        weatherSwitch.setOnCheckedChangeListener(switchListener);

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

    public void createAlarmClicked (View view) {
        Alarm alarm = new Alarm();
        alarm.setName(name.getText().toString());
        alarm.setTime(alarmTime);
        alarmDBHelper.addAlarm(alarm);
        Intent i = new Intent(this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("EXIT", true);
        startActivity(i);
    }
}
