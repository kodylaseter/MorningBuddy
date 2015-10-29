package com.example.song.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.song.myapplication.AlarmActivity;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by Kody on 10/28/2015.
 */
public class AlarmManagerService {

    public AlarmManager getAM(Context ctx) {
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(Time time, Context ctx) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        Intent intent = new Intent(ctx, AlarmActivity.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
        getAM(ctx).set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }
}
