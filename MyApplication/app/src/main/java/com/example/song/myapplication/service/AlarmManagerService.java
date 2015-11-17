package com.example.song.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import com.example.song.myapplication.models.Alarm;

import java.util.Calendar;

/**
 * Created by Kody on 10/28/2015.
 */
public class AlarmManagerService {

    private static AlarmManagerService ams;
    public static final String ALARM_ID = "alarmID";
    public static AlarmManagerService getInstance() {
        if (ams == null) ams = new AlarmManagerService();
        return ams;
    }

    public void setAlarm(Alarm alarm, Context ctx) {
        Calendar calSet = Utilities.getCalendarFromTime(alarm);
        long t = calSet.getTimeInMillis();
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        intent.putExtra(ALARM_ID, alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
    }
}
