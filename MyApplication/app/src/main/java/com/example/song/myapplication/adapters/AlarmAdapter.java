package com.example.song.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    /**
     * set up page view of alarm listing
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        Alarm alarm = alarmDBHelper.getAlarm(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarms_list_item, parent, false);
        }
        //layout of the current list view
        TextView firstLine = (TextView) convertView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) convertView.findViewById(R.id.secondLine);
        ImageView trafficOn = (ImageView) convertView.findViewById(R.id.traffic_on);
        ImageView trafficOff = (ImageView) convertView.findViewById(R.id.traffic_off);
        ImageView weatherOn = (ImageView) convertView.findViewById(R.id.weather_on);
        ImageView weatherOff = (ImageView) convertView.findViewById(R.id.weather_off);
        //image view for if any weather and traffic monitoring is on
        if (alarm.isWeatherEnabled()) {
            weatherOn.setVisibility(View.VISIBLE);
            weatherOff.setVisibility(View.GONE);
        } else {
            weatherOn.setVisibility(View.GONE);
            weatherOff.setVisibility(View.VISIBLE);
        }
        if (alarm.isTrafficEnabled()) {
            trafficOn.setVisibility(View.VISIBLE);
            trafficOff.setVisibility(View.GONE);
        } else {
            trafficOn.setVisibility(View.GONE);
            trafficOff.setVisibility(View.VISIBLE);
        }
        firstLine.setText(alarm.getName());
        secondLine.setText(alarm.getTimeString());
        return convertView;
    }
}
