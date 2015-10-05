package com.example.song.myapplication.data;

import org.json.JSONObject;

/**
 * Created by song on 9/29/2015 0029.
 */
public class Units implements JSONPopulator{

    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
