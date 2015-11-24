package com.example.song.myapplication.service;

import com.example.song.myapplication.models.Alarm;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by Kody on 10/27/2015.
 */
public class Utilities {
    public static Time minutesToTime(int minutes) {
        return new Time(minutes / 60, minutes % 60, 0);
    }

    public static Calendar getCalendarFromTime(int minutes) {
        //Time time = alarm.getTimeAsTime();
        Time time = minutesToTime(minutes);
        Calendar cal = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, time.getHours());
        cal.set(Calendar.MINUTE, time.getMinutes());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (cal.compareTo(calNow) <= 0) {
            cal.add(Calendar.DATE, 1);
        }
        return cal;
    }

    public static long timeToMinutes(Time time) {
        return (time.getHours() * 60) + time.getMinutes();
    }

    public static long minutesToMilliseconds(int min) {
        return min * 60 * 1000;
    }

    public static long millisecondsToMinutes(int millis) {
        return millis / (60*1000);
    }

    public static boolean isAlarmFarEnoughAway(Alarm alarm, int buffer) {
        Calendar cal = Calendar.getInstance();
        Calendar calNow = (Calendar) cal.clone();
        Time time = alarm.getTimeAsTime();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, time.getHours());
        cal.set(Calendar.MINUTE, time.getMinutes());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MINUTE, 0 - buffer);
        //calNow is the current time
        //cal is the alarm time specified by the user with the buffer time subtracted
        //the purpose of this is to make sure the alarm is set far enough away that there is time to perform the needed checks
        //we want cal to be greater than calNow
        return (cal.compareTo(calNow) > 0);
    }


//    public static boolean checkForValidTime(Alarm alarm, float minutes) {
//
//    }
}
