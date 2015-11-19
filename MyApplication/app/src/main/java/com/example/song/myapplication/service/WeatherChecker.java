package com.example.song.myapplication.service;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.example.song.myapplication.data.Channel;
import com.example.song.myapplication.data.Condition;
import com.example.song.myapplication.data.Item;
import com.example.song.myapplication.data.WeatherConditionStates;
import com.example.song.myapplication.models.Alarm;

/**
 * Created by Kody on 11/18/2015.
 */
public class WeatherChecker implements WeatherServiceCallback {

    private AlarmReceiver alarmReceiver;
    private WeatherService weatherService;
    private Context ctx;
    private Alarm alarm;
    private WeatherConditionStates wcs;

    public WeatherChecker(AlarmReceiver alarmReceiver, Context ctx) {
        this.alarmReceiver = alarmReceiver;
        this.weatherService = new WeatherService(this);
        this.ctx = ctx;
        this.wcs = WeatherConditionStates.getInstance();
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
        WeatherState weather;
        //String weatherCondition = item.getCondition().getDescription().toLowerCase();
        int weatherConditionCode = item.getCondition().getCode();

        switch(wcs.weatherConditionState(weatherConditionCode)) {
            case "snow":
                weather = WeatherState.snow;
                break;
            case "storm":
                weather = WeatherState.storm;
                break;
            case "wind":
                weather = WeatherState.wind;
                break;
            default:
                weather = WeatherState.other;
                break;
        }

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
