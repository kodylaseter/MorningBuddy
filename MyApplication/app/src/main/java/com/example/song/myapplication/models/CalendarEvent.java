package com.example.song.myapplication.models;

import android.text.format.Time;

/**
 * Created by Ivan on 11/4/2015.
 */
public class CalendarEvent {
    public String name;
    public Time startTime;
    public Time endTime;

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(Time time) {
        this.startTime = time;
    }

    public void setEndTime(Time time) {
        this.endTime = time;
    }

    public String getName() {
        return this.name;
    }

    public Time getStartTime() {
        return this.startTime;
    }

    public Time getEndTime() { return this.endTime; }

    /**
     * Custom toString() method
     * Returns time of event in this format: HOURS:MINUTES:AM/PM
     * @return String time
     */
    public String startTimeToString() {
        String hour = startTime.hour + "";
        String minute = startTime.minute + "";
        boolean pm = false;

        if (startTime.hour == 0) {
            hour = "12";
        } else if (startTime.hour == 12) {
            pm = true;
        } else if (startTime.hour > 12) {
            hour = startTime.hour % 12 + "";
            pm = true;
        }

        if (startTime.minute < 10) {
            minute = "0" + startTime.minute;
        }

        if (pm) {
            return hour + ":" + minute + "PM";
        }

        return hour + ":" + minute + "AM";
    }

    public String endTimeToString() {
        String hour = endTime.hour + "";
        String minute = endTime.minute + "";
        boolean pm = false;

        if (endTime.hour == 0) {
            hour = "12";
        } else if (endTime.hour == 12) {
            pm = true;
        } else if (endTime.hour > 12) {
            hour = endTime.hour % 12 + "";
            pm = true;
        }

        if (endTime.minute < 10) {
            minute = "0" + endTime.minute;
        }

        if (pm) {
            return hour + ":" + minute + "PM";
        }

        return hour + ":" + minute + "AM";
    }
}
