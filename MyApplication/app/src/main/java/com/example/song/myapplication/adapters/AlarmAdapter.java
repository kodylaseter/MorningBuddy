package com.example.song.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.song.myapplication.R;
import com.example.song.myapplication.db.AlarmDBHelper;
import com.example.song.myapplication.models.Alarm;

import java.util.ArrayList;

/**
 * Created by Kody on 10/21/2015.
 */
public class AlarmAdapter extends ArrayAdapter<Alarm>{

    AlarmDBHelper alarmDBHelper;
    public AlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, 0, alarms);
        alarmDBHelper = new AlarmDBHelper(null);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Alarm alarm = alarmDBHelper.getAlarm(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarms_list_item, parent, false);
        }
        TextView firstLine = (TextView) convertView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) convertView.findViewById(R.id.secondLine);
        firstLine.setText(alarm.getName());
        secondLine.setText(alarm.getTimeString());
        return convertView;
    }
}
