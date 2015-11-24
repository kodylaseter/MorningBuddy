package com.example.song.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.song.myapplication.data.WeatherMonitor;
import com.example.song.myapplication.models.Alarm;
import com.example.song.myapplication.models.AlarmType;

import java.util.Calendar;

/**
 * Created by Kody on 10/28/2015.
 */
public class AlarmManagerService {

    private static AlarmManagerService ams;
    public static final String ALARM_ID = "alarmID";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String ALARM_TYPE = "alarmType";
    public static AlarmManagerService getInstance() {
        if (ams == null) ams = new AlarmManagerService();
        return ams;
    }

    public void setAlarm(Alarm alarm, String alarmType, Context ctx, int time) {
        Calendar calSet = Utilities.getCalendarFromTime(time);
        long t = calSet.getTimeInMillis();
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        switch (alarmType) {
            case AlarmType.ACTUALALARM:
                break;
            case AlarmType.CHECKTRAFFIC:
                break;
            case AlarmType.CHECKWEATHER:
                int tempTime = WeatherMonitor.getInstance().getMaxTime() + 1;
                calSet.add(Calendar.MINUTE, 0 - tempTime);
                break;
        }
        int a = alarm.getId();
        intent.putExtra(ALARM_ID, String.valueOf(alarm.getZeroId()));
        intent.putExtra(ORIGIN, alarm.getOrigin());
        intent.putExtra(DESTINATION, alarm.getDestination());
        intent.putExtra(ALARM_TYPE, alarmType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Log.d("mbuddy", "alarm set at " + calSet.getTime());
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
    }
}
