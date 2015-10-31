package com.example.song.myapplication.data;

import java.io.Serializable;

/**
 * Created by song on 10/30/2015 0030.
 */
public class WeatherMonitor implements Serializable {
    private int snowTime;
    private int windyTime;
    private int stormTime;

    public WeatherMonitor(int snow, int windy, int storm) {
        snowTime = snow;
        windyTime = windy;
        stormTime = storm;
    }

    public int getSnowTime() {
        return snowTime;
    }

    public int getWindyTime() {
        return windyTime;
    }

    public int getStormTime() {
        return stormTime;
    }

    public void setSnowTime(int snowTime) {
        this.snowTime = snowTime;
    }

    public void setWindyTime(int windyTime) {
        this.windyTime = windyTime;
    }

    public void setStormTime(int stormTime) {
        this.stormTime = stormTime;
    }
}
