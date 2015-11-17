package com.example.song.myapplication.models;

import com.example.song.myapplication.service.Utilities;

import java.sql.Time;

/**
 * Created by Kody on 10/21/2015.
 */
public class Alarm {

    private long id;
    private String name;
    private int time; //time in millis since
    private boolean weatherEnabled;
    private boolean trafficEnabled;
    private String origin;
    private String destination;

    public Alarm(long id, String name, int minutesAfterMidnight, boolean weatherEnabled, boolean trafficEnabled, String origin, String destination) {
        this.id = id;
        this.name = name;
        this.time = minutesAfterMidnight;
        this.weatherEnabled = weatherEnabled;
        this.trafficEnabled = trafficEnabled;
    }

    //passing traffic and weather as 0 or 1. probably to be used by db
    public Alarm(long id, String name, int minutesAfterMidnight, int weatherEnabled, int trafficEnabled) {
        this.id = id;
        this.name = name;
        this.time = minutesAfterMidnight;
        this.weatherEnabled = weatherEnabled == 1;
        this.trafficEnabled = trafficEnabled == 1;

    }

    public Alarm() {
    }

    public int getTime() {
        return time;
    }

    public Time getTimeAsTime() {
        return Utilities.minutesToTime(time);
    }

    public String getTimeString() {
        String temp = "";
        Time t = Utilities.minutesToTime(time);
        int minutes = t.getMinutes();
        int hours = t.getHours();
        String sH = Integer.toString(hours);
        String sM = sM = minutes < 10 ? "0" + minutes : "" + minutes;
        if (hours < 1) {
            sH = "12";
            temp = sH + ":" + sM + " AM";
        }
        if (hours > 12) {
            sH = Integer.toString(hours - 12);
            temp = sH + ":" + sM + " PM";
        } else {
            sH = hours < 10 ? "0" + hours : "" + hours;
            temp = sH + ":" + sM + " AM";
        }
        return temp;
    }

    public void setTime(int t) {
        this.time = t;
    }

    public long getId() {
        return id;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return this.name;
    }

    public boolean isWeatherEnabled() {
        return weatherEnabled;
    }

    public void setWeatherEnabled(boolean weatherEnabled) {
        this.weatherEnabled = weatherEnabled;
    }

    public boolean isTrafficEnabled() {
        return trafficEnabled;
    }

    public void setTrafficEnabled(boolean trafficEnabled) {
        this.trafficEnabled = trafficEnabled;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
