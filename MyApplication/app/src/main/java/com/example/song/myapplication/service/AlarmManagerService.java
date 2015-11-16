package com.example.song.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kody on 10/28/2015.
 */
public class AlarmManagerService {

    private static AlarmManagerService ams;

    public static AlarmManagerService getInstance() {
        if (ams == null) ams = new AlarmManagerService();
        return ams;
    }

    public void setAlarm(Time time, Context ctx) {
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
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
    }
}
