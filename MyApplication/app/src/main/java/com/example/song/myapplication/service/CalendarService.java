package com.example.song.myapplication.service;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.format.Time;

import com.example.song.myapplication.models.CalendarEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



/**
 * Created by Ivan on 11/4/2015.
 */
public class CalendarService {

    private static List<CalendarEvent> events;

    public CalendarService(Context context) {
        events = new ArrayList<CalendarEvent>();
        readCalendarEvent(context);
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

    public CalendarEvent getEvent(int position) {
        return events.get(position);
    }

    public void readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();

        //Time now = new Time();
        //String timeNow, eventTime;

        //Loop through all calendar events, add only the ones that match with when the alarm goes off
        for (int i = 0; i < cursor.getCount(); i++) {
            //now.setToNow();

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            Date today = new Date();
            Date eventDate = new Date();
            eventDate.setTime(Long.parseLong(cursor.getString(3)));
            cal1.setTime(today);
            cal2.setTime(eventDate);
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if (sameDay) {
                String name = cursor.getString(1);
                CalendarEvent temp = new CalendarEvent();
                temp.setName(name);
                temp.setStartTime(getDate(Long.parseLong(cursor.getString(3))));
                temp.setEndTime(getDate(Long.parseLong(cursor.getString(4))));
                events.add(temp);
            }

            /**
            if (now.getJulianDay(now.toMillis(false),0) == getDate(Long.parseLong(cursor.getString(3))).getJulianDay(Long.parseLong(cursor.getString(3)),0)) {
                String name = cursor.getString(1);
                CalendarEvent temp = new CalendarEvent();
                temp.setName(name);
                events.add(temp);
            }
            */

            /**
            eventTime = getDate(Long.parseLong(cursor.getString(3)));
            timeNow = now.toString();
            if (eventTime.equals(timeNow.substring(0,8))) {
                String name = cursor.getString(1);
                CalendarEvent temp = new CalendarEvent();
                temp.setName(name);
                events.add(temp);
            }
            */
            cursor.moveToNext();
        }

    }


    public static Time getDate(long milliSeconds) {
        //SimpleDateFormat formatter = new SimpleDateFormat(
        //        "dd/MM/yyyy hh:mm:ss a");
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(milliSeconds);
        Time t = new Time();
        t.set(milliSeconds);
        //t.set(calendar.getTimeInMillis());
        return t;
        //calendar.setTimeInMillis(milliSeconds);
        //return formatter.format(calendar.getTime());
    }



}
