package com.example.song.myapplication.data;

import org.json.JSONObject;

/**
 * Created by song on 9/29/2015 0029.
 */
public class Item implements JSONPopulator {

    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    /**
     * Condition is under category of item
     * @param data, queried weather JSON data
     */
    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
