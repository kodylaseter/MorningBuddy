package com.example.song.myapplication.service;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.example.song.myapplication.data.Channel;
import com.example.song.myapplication.data.Item;
import com.example.song.myapplication.models.Alarm;

/**
 * Created by Kody on 11/18/2015.
 */
public class WeatherChecker implements WeatherServiceCallback {

    private AlarmReceiver alarmReceiver;
    private WeatherService weatherService;
    private Context ctx;
    private Alarm alarm;

    public WeatherChecker(AlarmReceiver alarmReceiver, Context ctx) {
        this.alarmReceiver = alarmReceiver;
        this.weatherService = new WeatherService(this);
        this.ctx = ctx;
    }

    public void checkWeather(Alarm alarm) {
        this.alarm = alarm;
        SingleShotLocationProvider.requestSingleUpdate(ctx,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        //Log.d("Location", "my location is " + location.toString());
                        weatherService.refreshWeather(location.toString());
                    }
                });

    }

    @Override
    public void serviceSuccess(Channel channel) {
        Item item = channel.getItem();
        this.alarmReceiver.finishWeatherCheck(this.alarm);
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this.ctx, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

}
