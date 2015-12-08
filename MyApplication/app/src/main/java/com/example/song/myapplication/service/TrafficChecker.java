package com.example.song.myapplication.service;

import android.content.Context;
import android.util.Log;

import com.example.song.myapplication.NewAlarmActivity;
import com.example.song.myapplication.models.Alarm;



/**
 * Created by Kody on 12/1/2015.
 */
public class TrafficChecker implements OnTaskCompleted {

    private Object callbackObject;
    private TrafficService trafficService;
    private Alarm alarm;
    private Context context;

    public TrafficChecker (Object callbackObject, Context ctx) {
        this.callbackObject = callbackObject;
        this.context = ctx;
        this.trafficService = new TrafficService(this);
    }

    /**
     * check the time for travelling
     * @param alarm
     */
    public void checkTime (Alarm alarm) {
        this.alarm = alarm;
        trafficService.getTimeEstimateAtTime(alarm.getOrigin(), alarm.getDestination(), alarm.getTime());
    }

    public void checkTimeNow (Alarm alarm) {
        this.alarm = alarm;
        trafficService.getTimeEstimateNow(alarm.getOrigin(), alarm.getDestination());
    }

    /**
     * auto time checking interval for later time adjustment
     * @param res
     */
    public void onTaskCompleted(String res) {
        float newTime = Float.parseFloat(res);
        if (this.callbackObject instanceof AlarmReceiver) {
            newTime = 30;
            AlarmReceiver alarmReceiver = (AlarmReceiver)this.callbackObject;
            alarmReceiver.trafficCheckStep(handleTime(newTime));
        } else if (this.callbackObject instanceof NewAlarmActivity) {
            newTime = 22;
            NewAlarmActivity newAlarmActivity = (NewAlarmActivity)this.callbackObject;
            newAlarmActivity.finishAddAlarm(newTime);
        }
    }

    /**
     * reformat the time
     * @param time
     * @return
     */
    private Alarm handleTime(float time) {
        Alarm newAlarm = this.alarm;
        float estimate = alarm.getTimeEstimate();
        if (time > estimate) {
            Log.d("mbuddy", "time was less");
            int difference = Math.round(time - estimate);
            newAlarm.setNewTime(newAlarm.getTime() - difference);
        }
        return newAlarm;
    }
}
