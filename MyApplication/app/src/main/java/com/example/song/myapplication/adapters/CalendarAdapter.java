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
import com.example.song.myapplication.models.CalendarEvent;
import com.example.song.myapplication.service.CalendarService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 11/4/2015.
 */
public class CalendarAdapter extends ArrayAdapter<CalendarEvent> {

    public CalendarAdapter(Context context, List<CalendarEvent> events) {
        super(context, 0, events);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CalendarEvent event = super.getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_list_item, parent, false);
        }

        TextView eventName = (TextView) convertView.findViewById(R.id.eventNameTextView);
        TextView eventTime = (TextView) convertView.findViewById(R.id.eventTimeTextView);
        eventName.setText(event.getName());
        eventTime.setText(event.startTimeToString() + " - " + event.endTimeToString());

        return convertView;
    }
}

