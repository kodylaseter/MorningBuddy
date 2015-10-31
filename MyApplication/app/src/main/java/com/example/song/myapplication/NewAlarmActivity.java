package com.example.song.myapplication;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import com.example.song.myapplication.data.WeatherMonitor;
import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.models.Alarm;
import com.example.song.myapplication.service.AlarmManagerService;
import com.example.song.myapplication.service.AlarmReceiver;

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

    WeatherMonitor wm = new WeatherMonitor(0, 0, 0);

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
                        /*
                        weatherLocation.setVisibility(View.VISIBLE);
                    } else {
                        weatherLocation.setVisibility(View.GONE);
                        */
                        weatherMonitoringOn();
                    }
                }
            }
        };

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

    @TargetApi(Build.VERSION_CODES.M)
    public void createAlarmClicked (View view) {
        Alarm alarm = new Alarm();
        alarm.setName(name.getText().toString());
        int alarmTime = (timePicker.getCurrentHour() * 60) + timePicker.getCurrentMinute();
        alarm.setTime(alarmTime);
        alarmDBHelper.addAlarm(alarm);
        setAlarm(alarm.getTimeAsTime());

        Intent i = new Intent(this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("EXIT", true);
        startActivity(i);

    }
    public void setAlarm(Time time) {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.setTimeInMillis(System.currentTimeMillis());
        calSet.set(Calendar.HOUR_OF_DAY, time.getHours());
        calSet.set(Calendar.MINUTE, time.getMinutes());
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        if (calSet.compareTo(calNow) <= 0) {
            calSet.add(Calendar.DATE, 1);
        }
        long t = calSet.getTimeInMillis();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);

    }

    private void weatherMonitoringOn() {
        Intent  i = new Intent(this, WeatherActivity.class);
        //startActivity(i);

        //i.putExtra("myobject", wm);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //int s = data.getIntExtra("testing", 2);
            Bundle b = data.getExtras();
            if (b != null) {
                wm = (WeatherMonitor) b.getSerializable("weatherMonitor");
                System.out.println(wm.getSnowTime() + ", " + wm.getWindyTime() + ", " + wm.getStormTime());
            }
            //Log.d("result", ""+s);
        }
    }

}
