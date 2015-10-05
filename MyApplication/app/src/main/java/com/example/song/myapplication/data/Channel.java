package com.example.song.myapplication.data;

import org.json.JSONObject;

/**
 * Created by song on 9/29/2015 0029.
 */
public class Channel implements JSONPopulator{

    private Units units;
    private Item item;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));
    }
}
