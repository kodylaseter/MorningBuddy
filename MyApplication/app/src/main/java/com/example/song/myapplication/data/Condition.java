package com.example.song.myapplication.data;

import org.json.JSONObject;

/**
 * Created by song on 9/29/2015 0029.
 */
public class Condition implements JSONPopulator{

    private int code;
    private int temperature;
    private String description;

    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Populate weather condition
     * @param data, queried weather JSON data
     */
    @Override
    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperature = data.optInt("temp");
        description = data.optString("text");
    }
}
