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

        int weatherConditionCode = item.getCondition().getCode();

        WeatherState weatherState = WeatherConditionStates.getStateFromCode(weatherConditionCode);
        //set to snow for testing
        //weatherState = WeatherState.snow;
        switch (weatherState){
            case snow:
                Log.d("mbuddy", "alarm moved up due to snow");
                alarm.setNewTime(alarm.getTime() - WeatherMonitor.getInstance().getSnowTime());
                break;
            case storm:
                Log.d("mbuddy", "alarm moved up due to storm");
                alarm.setNewTime(alarm.getTime() - WeatherMonitor.getInstance().getStormTime());
                break;
            case wind:
                Log.d("mbuddy", "alarm moved up due to wind");
                alarm.setNewTime(alarm.getTime() - WeatherMonitor.getInstance().getWindyTime());
                break;
            case rain:
                Log.d("mbuddy", "alarm moved up due to rain");
                alarm.setNewTime(alarm.getTime() - WeatherMonitor.getInstance().getRainTime());
                break;
            case other:
                Log.d("mbuddy", "alarm not changed by weather checker");
                break;
        }

        this.alarmReceiver.finishWeatherCheck(this.alarm);
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this.ctx, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public enum WeatherState {
        snow, storm, wind, rain, other;
    }
}
