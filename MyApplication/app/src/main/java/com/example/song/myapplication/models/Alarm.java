package com.example.song.myapplication.models;

import com.example.song.myapplication.service.Utilities;

import java.sql.Time;

/**
 * Created by Kody on 10/21/2015.
 */
public class Alarm {

    private long id;
    private String name;
    private int time;

    public Alarm(long id, String name, int minutesAfterMidnight) {
        this.id = id;
        this.name = name;
        this.time = minutesAfterMidnight;
    }

    public Alarm() {}

    public int getTime() {
        return time;
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

//    public void setId(long id) {
//        this.id = id;
//    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return this.name;
    }


}
