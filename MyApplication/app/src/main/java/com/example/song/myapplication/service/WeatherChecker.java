package com.example.song.myapplication.service;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.example.song.myapplication.data.Channel;
import com.example.song.myapplication.data.Condition;
import com.example.song.myapplication.data.Item;
import com.example.song.myapplication.data.WeatherConditionStates;
import com.example.song.myapplication.data.WeatherMonitor;
import android.util.Log;
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
    }

    /**
     * do weather query based on current location
     * @param alarm
     */
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

    /**
     * if current weather information has been returned
     * @param channel, weather information
     */
    @Override
    public void serviceSuccess(Channel channel) {
        Item item = channel.getItem();

        int weatherConditionCode = item.getCondition().getCode();

        WeatherState weatherState = WeatherConditionStates.getStateFromCode(weatherConditionCode);

        //set to snow for testing/debugging
        weatherState = WeatherState.snow;
        switch (weatherState){
            case snow:
                Log.d("mbuddy", "alarm moved up due to snow");
                alarm.setNewTime((int)(Utilities.timeToMinutes(alarm.getEitherTimeAsTime()) - WeatherMonitor.getInstance().getSnowTime()));
                Toast.makeText(this.ctx, "Alarm moved up " + WeatherMonitor.getInstance().getSnowTime() + " minutes due to traffic conditions!", Toast.LENGTH_SHORT).show();
                break;
            case storm:
                Log.d("mbuddy", "alarm moved up due to storm");
                alarm.setNewTime((int)(Utilities.timeToMinutes(alarm.getEitherTimeAsTime()) - WeatherMonitor.getInstance().getSnowTime()));
                break;
            case wind:
                Log.d("mbuddy", "alarm moved up due to wind");
                alarm.setNewTime((int)(Utilities.timeToMinutes(alarm.getEitherTimeAsTime()) - WeatherMonitor.getInstance().getSnowTime()));
                break;
            case rain:
                Log.d("mbuddy", "alarm moved up due to rain");
                alarm.setNewTime((int)(Utilities.timeToMinutes(alarm.getEitherTimeAsTime()) - WeatherMonitor.getInstance().getSnowTime()));
                break;
            case other:
                Log.d("mbuddy", "alarm not changed by weather checker");
                break;
        }

        this.alarmReceiver.finishWeatherCheck(this.alarm);
    }

    /**
     * if fail of fetching weather information
     * @param exception
     */
    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this.ctx, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * weather state based on weather condition code
     */
    public enum WeatherState {
        snow, storm, wind, rain, other;
    }
}
