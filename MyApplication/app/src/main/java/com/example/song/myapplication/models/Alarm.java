package com.example.song.myapplication.models;

/**
 * Created by Kody on 10/21/2015.
 */
public class Alarm {

    private long id;
    private String name;

    public Alarm(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Alarm() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return this.name;
    }


}
