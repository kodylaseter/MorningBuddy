package com.example.song.myapplication.models;

import java.sql.Time;

/**
 * Created by Kody on 10/21/2015.
 */
public class Alarm {

    private long id;
    private String name;
    private Time time;

    public Alarm(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Alarm() {}

    public Time getTime() {
        return time;
    }

    public void setTime(Time t) {
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
