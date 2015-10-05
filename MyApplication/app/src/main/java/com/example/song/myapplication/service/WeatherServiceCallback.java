package com.example.song.myapplication.service;

import com.example.song.myapplication.data.Channel;

/**
 * Created by song on 9/29/2015 0029.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
