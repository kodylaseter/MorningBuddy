package com.example.song.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.song.myapplication.AlarmActivity;
import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.models.Alarm;
import com.example.song.myapplication.models.AlarmType;

import java.util.Calendar;


/**
 * Created by klaseter3 on 10/30/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private Context ctx;

    @Override
    public void onReceive(Context context, Intent arg1) {
        this.ctx = context;
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
                String origin = arg1.getStringExtra(AlarmManagerService.ORIGIN);
                String dest = arg1.getStringExtra(AlarmManagerService.DESTINATION);
                break;
            case AlarmType.CHECKWEATHER:
                Alarm alarm = AlarmDBHelper.getInstance(context).getAlarm(id);
                WeatherChecker wc = new WeatherChecker(this, context);
                wc.checkWeather(alarm);
                break;
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
