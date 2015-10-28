package com.example.song.myapplication.service;

import java.sql.Time;

/**
 * Created by Kody on 10/27/2015.
 */
public class Utilities {
    public static Time minutesToTime(int minutes) {
        return new Time(minutes / 60, minutes % 60, 0);
    }

    public static int timeToMinutes(Time time) {
        return (time.getHours() * 60) + time.getMinutes();
    }
}
