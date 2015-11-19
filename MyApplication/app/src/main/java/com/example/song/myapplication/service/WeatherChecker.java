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
        ////////

        //write something here to take the item above returned from the weatherservice and parse it into one of the weather states below.
        //idk if there's an easy way to do this from the yahoo api or if you'll have to write a regex or string search thing.

        ////////
        WeatherState weather = WeatherState.snow; //you can use this variable
        this.alarmReceiver.finishWeatherCheck(this.alarm);
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this.ctx, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public enum WeatherState {
        snow, storm, wind, other;
    }

}
