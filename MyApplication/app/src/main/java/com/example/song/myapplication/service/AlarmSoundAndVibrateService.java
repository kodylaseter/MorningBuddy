package com.example.song.myapplication.service;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.song.myapplication.R;

/**
 * Created by Ivan on 11/18/2015.
 */
public class AlarmSoundAndVibrateService{

    private Ringtone ringtone;
    private Vibrator vibrator;
    private Context context;
    private final long[] pattern = {0, 1000, 1000, 1000, 1000, 1000};



    public AlarmSoundAndVibrateService(Context ctx) {
        context = ctx;
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        } catch (Exception e) {
            Log.d("mbuddy", e.toString());
        }
    }

    public void startAlarm() {

        Log.d("mbuddy", "alarm started");
        ringtone.play();
        vibrator.vibrate(pattern, 1);

    }

    public void stopAlarm() {
        Log.d("mbuddy", "alarm stopped");
        ringtone.stop();
        vibrator.cancel();
    }




}
