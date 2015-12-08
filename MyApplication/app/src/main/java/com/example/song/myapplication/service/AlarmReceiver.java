package com.example.song.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.song.myapplication.AlarmActivity;
import com.example.song.myapplication.data.WeatherMonitor;
import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.models.Alarm;
import com.example.song.myapplication.models.AlarmType;


/**
 * Created by klaseter3 on 10/30/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private Context ctx;

    @Override
    public void onReceive(Context context, Intent arg1) {
        this.ctx = context;
        Alarm alarm;
        String alarmType = arg1.getStringExtra(AlarmManagerService.ALARM_TYPE);
        int id = Integer.parseInt(arg1.getStringExtra(AlarmManagerService.ALARM_ID));
        //still lots to be added here
        switch (alarmType) {
            case AlarmType.ACTUALALARM:
                Log.d("mbuddy", "final alarm!");
                Intent i = new Intent(context, AlarmActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            case AlarmType.CHECKTRAFFIC:
                alarm = AlarmDBHelper.getInstance(context).getAlarm(id);
                TrafficChecker trafficChecker = new TrafficChecker(this, context);
                trafficChecker.checkTime(alarm);
                break;
            case AlarmType.CHECKWEATHER:
                alarm = AlarmDBHelper.getInstance(context).getAlarm(id);
                WeatherChecker wc = new WeatherChecker(this, context);
                wc.checkWeather(alarm);
                break;
        }
    }

    public void trafficCheckStep(Alarm alarm) {
        if (alarm.getNewTime() != Alarm.DUMMY_TIME) {
            if (alarm.getNewTime() < alarm.getTime() - WeatherMonitor.getInstance().getMaxTime()) {
                Toast.makeText(this.ctx, "Alarm moved up " + (alarm.getTime() - alarm.getNewTime()) + " minutes due to traffic conditions!", Toast.LENGTH_SHORT).show();
                AlarmManagerService.getInstance().setAlarm(alarm, AlarmType.ACTUALALARM, this.ctx, alarm.getNewTime());
            } else {
                if (alarm.isWeatherEnabled()) {
                    Toast.makeText(this.ctx, "Alarm moved up " + (alarm.getTime() - alarm.getNewTime()) + " minutes and weather check will still occur.", Toast.LENGTH_LONG).show();
                    AlarmManagerService.getInstance().setAlarm(alarm, AlarmType.CHECKWEATHER, this.ctx, alarm.getNewTime());
                } else {
                    Toast.makeText(this.ctx, "Alarm moved up " + (alarm.getTime() - alarm.getNewTime()) + " minutes due to traffic conditions!", Toast.LENGTH_SHORT).show();
                    AlarmManagerService.getInstance().setAlarm(alarm, AlarmType.ACTUALALARM, this.ctx, alarm.getNewTime());
                }
            }
        } else {
            if (alarm.isWeatherEnabled()) {
                AlarmManagerService.getInstance().setAlarm(alarm, AlarmType.CHECKWEATHER, this.ctx, alarm.getTime());
            } else {
                AlarmManagerService.getInstance().setAlarm(alarm, AlarmType.ACTUALALARM, this.ctx, alarm.getTime());
            }
        }
    }

    public void finishWeatherCheck(Alarm alarm) {
        int a = alarm.getId();
        if (alarm.getNewTime() != Alarm.DUMMY_TIME) {
            AlarmManagerService.getInstance().setAlarm(alarm, AlarmType.ACTUALALARM, ctx, alarm.getNewTime());
        } else {
            AlarmManagerService.getInstance().setAlarm(alarm, AlarmType.ACTUALALARM, ctx, alarm.getTime());
        }
    }
}
