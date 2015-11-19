package com.example.song.myapplication.data;

import java.util.HashMap;

/**
 * Created by song on 11/19/2015 0019.
 */
public class WeatherConditionStates {
    private HashMap<Integer, String> snowState;
    private HashMap<Integer, String> stormState;
    private HashMap<Integer, String> windState;

    private static WeatherConditionStates weatherConditionStates;

    public static WeatherConditionStates getInstance() {
        if (weatherConditionStates == null) {
            weatherConditionStates = new WeatherConditionStates();
        }
        return weatherConditionStates;
    }

    public WeatherConditionStates() {
        //weather code refefence: https://gist.github.com/bzerangue/805520
        initSnowStates();
        initStormStates();
        initWindStates();
    }

    private void initSnowStates() {

        int [] snowStateCodes = new int[] {5, 6, 7, 13, 14, 15, 16, 17, 18, 41, 42, 43, };
        for (int i:snowStateCodes) {
            snowState.put(i, "");
        }
    }
    private void initStormStates() {

        //so far the rain state is included under the storm; it could be further separated

        int [] stormStateCodes = new int[] {1, 3, 4, 8, 9, 10, 11, 12, 35, 37, 38, 39, 40, 45, 47};
        for (int i:stormStateCodes) {
            stormState.put(i, "");
        }
    }
    private void initWindStates() {
        int [] windStateCodes = new int[] {0, 2, 23, 24, };
        for (int i:windStateCodes) {
            windState.put(i, "");
        }
    }
    public String weatherConditionState(int code) {
        String state = "";
        if (snowState.containsKey(code)) {
            state = "snow";
        } else if (stormState.containsKey(code)) {
            state = "storm";
        } else if (windState.containsKey(code)) {
            state = "wind";
        } else {
            state = "other";
        }
        return state;
    }
}
