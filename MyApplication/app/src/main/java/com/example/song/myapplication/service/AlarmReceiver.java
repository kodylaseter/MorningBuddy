package com.example.song.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.song.myapplication.AlarmActivity;


/**
 * Created by klaseter3 on 10/30/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        int id = Integer.parseInt(arg1.getStringExtra(AlarmManagerService.ALARM_ID));
        String origin = arg1.getStringExtra(AlarmManagerService.ORIGIN);
        String dest = arg1.getStringExtra(AlarmManagerService.DESTINATION);
        Intent i = new Intent(context, AlarmActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
