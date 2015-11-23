package com.example.song.myapplication.data;

import com.example.song.myapplication.service.WeatherChecker;

import java.util.HashMap;

/**
 * Created by song on 11/19/2015 0019.
 */
public class WeatherConditionStates {
    private HashMap<Integer, String> snowState;
    private HashMap<Integer, String> rainState;

    private HashMap<Integer, String> stormState;
    private HashMap<Integer, String> windState;

    private static Integer [] snowStateCodes = new Integer[] {5, 6, 7, 13, 14, 15, 16, 17, 18, 41, 42, 43, 46};
    private static Integer [] rainStateCodes = new Integer[] {8, 9, 10, 11, 12, 35};
    private static Integer [] stormStateCodes = new Integer[] {1, 3, 4, 37, 38, 39, 45, 47};
    private static Integer [] windStateCodes = new Integer[] {0, 2, 23, 24, };

    public static WeatherChecker.WeatherState getStateFromCode(int code) {

        if (contains(snowStateCodes, code)) {
            return WeatherChecker.WeatherState.snow;
        } else if (contains(rainStateCodes, code)){
            return WeatherChecker.WeatherState.rain;
        } else if (contains(stormStateCodes, code)) {
            return WeatherChecker.WeatherState.storm;
        } else if (contains(windStateCodes, code)) {
            return WeatherChecker.WeatherState.wind;
        } else {
            return WeatherChecker.WeatherState.other;
        }
    }

    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (e == v || v != null && v.equals(e))
                return true;

        return false;
    }
}
