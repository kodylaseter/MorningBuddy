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

    private static List<CalendarEvent> events; //Stores events that have the same date as the alarm

    public CalendarService(Context context) {
        events = new ArrayList<CalendarEvent>();
        readCalendarEvent(context); //Populates events, if there are any
    }

    /**
     * Returns a list of the date's events
     * @return List<CalendarEvent>
     */
    public List<CalendarEvent> getEvents() {
        return events;
    }

    /**
     * Returns an event at the specified position
     * For instance, position = 5 should return the 5th event of the day, in chronological order
     * @param position
     * @return CalendarEvent
     */
    public CalendarEvent getEvent(int position) {
        return events.get(position);
    }

    /**
     * Reads all calendar events on android's native calendar and returns the ones that have the same date as the alarm
     * @param context
     */
    public void readCalendarEvent(Context context) {

        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();

        //Loop through all calendar events, add only the ones that match with when the alarm goes off
        for (int i = 0; i < cursor.getCount(); i++) {

            //Creates a Date for today and the calendar event
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            Date today = new Date();
            Date eventDate = new Date();
            eventDate.setTime(Long.parseLong(cursor.getString(3)));
            cal1.setTime(today);
            cal2.setTime(eventDate);
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            //If the Date is the same, then add it to events
            if (sameDay) {
                String name = cursor.getString(1);
                CalendarEvent temp = new CalendarEvent();
                temp.setName(name);
                temp.setStartTime(getDate(Long.parseLong(cursor.getString(3))));
                temp.setEndTime(getDate(Long.parseLong(cursor.getString(4))));
                events.add(temp);
            }

            cursor.moveToNext();
        }
    }

    /**
     * Helper method to get the Time version of a calendar event
     * Used because CalendarEvent has startTime and endTime which are instances of Time
     * @param milliSeconds
     * @return Time
     */
    private static Time getDate(long milliSeconds) {
        Time t = new Time();
        t.set(milliSeconds);
        return t;
    }

}
