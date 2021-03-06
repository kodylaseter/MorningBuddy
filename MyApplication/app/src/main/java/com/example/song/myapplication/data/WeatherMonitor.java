package com.example.song.myapplication.data;

import java.io.Serializable;

/**
 * Created by song on 10/30/2015 0030.
 */
public class WeatherMonitor implements Serializable {
    private int snowTime;
    private int windyTime;
    private int stormTime;
    private int rainTime;
    private static WeatherMonitor weatherMonitor;

    public static WeatherMonitor getInstance() {
        if (weatherMonitor == null) {
            weatherMonitor = new WeatherMonitor();
        }
        return weatherMonitor;
    }

    public WeatherMonitor() {
        this.snowTime = 10;
        this.stormTime = 10;
        this.windyTime = 4;
        this.rainTime = 6;
    }

    public int getRainTime() {
        return rainTime;
    }

    public void setRainTime(int rainTime) {
        this.rainTime = rainTime;
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

    public int getMaxTime() {
        return Math.max(snowTime, Math.max(windyTime, Math.max(stormTime, rainTime)));
    }
}
