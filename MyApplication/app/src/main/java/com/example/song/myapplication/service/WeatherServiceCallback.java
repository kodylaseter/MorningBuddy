package com.example.song.myapplication.service;

import com.example.song.myapplication.data.Channel;

/**
 * Created by song on 9/29/2015 0029.
 */
public interface WeatherServiceCallback {
    /**
     * if weather information returns correctly
     * @param channel, weather information
     */
    void serviceSuccess(Channel channel);

    /**
     * if no valid weather information could be obtained
     * @param exception
     */
    void serviceFailure(Exception exception);
}
