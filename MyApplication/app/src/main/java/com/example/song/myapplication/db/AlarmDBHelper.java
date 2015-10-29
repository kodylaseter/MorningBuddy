package com.example.song.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.song.myapplication.models.Alarm;
import com.example.song.myapplication.service.AlarmManagerService;
import com.example.song.myapplication.service.Utilities;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kody on 10/21/2015.
 */
public class AlarmDBHelper {
    private SQLiteDatabase db;
    private DBService dbservice;
    private String[] columns = {DBService.COLUMN_ID, DBService.COLUMN_NAME, DBService.COLUMN_TIME};
    private ArrayList<Alarm> alarms;
    private AlarmManagerService alarmManagerService;
    private Context ctx;

    private static AlarmDBHelper alarmDBInstance;

    public static AlarmDBHelper getInstance(Context ctx) {
        if (alarmDBInstance == null) {
            alarmDBInstance = new AlarmDBHelper(ctx.getApplicationContext());
        }
        return alarmDBInstance;
    }

    public AlarmDBHelper (Context context) {
        dbservice = DBService.getInstance(context);
        alarmManagerService = new AlarmManagerService();
        ctx = context;
        updateAlarms();
    }

    public void open() throws SQLException{
        db = dbservice.getWritableDatabase();
    }

    public void close() {
        dbservice.close();
    }

    public SQLiteDatabase getDb () {
        if (db == null) try {
            this.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return db;
    }

    public Alarm addAlarm(Alarm alarm) {
        setActualAlarm(alarm);
        ContentValues values = new ContentValues();
        values.put(DBService.COLUMN_NAME, alarm.getName());
        values.put(DBService.COLUMN_TIME, alarm.getTime());
        long insertID = getDb().insert(DBService.TABLE_NAME, null, values);
        Cursor cursor = getDb().query(DBService.TABLE_NAME, columns, DBService.COLUMN_ID + " =" + insertID, null, null, null, null);
        cursor.moveToFirst();
        Alarm a = toAlarmModel(cursor);

        updateAlarms();
        return a;
    }

    public void setActualAlarm(Alarm alarm) {
        alarmManagerService.setAlarm(Utilities.minutesToTime(alarm.getTime()), ctx);
    }

    public Alarm getAlarm(int position) {
        ArrayList<Alarm> alarms = getAlarms();
        return alarms.get(position);
    }

    public void updateAlarms() {

        this.alarms = new ArrayList<Alarm>();
        Cursor cursor = getDb().query(DBService.TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Alarm alarm = toAlarmModel(cursor);
            alarms.add(alarm);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public ArrayList<Alarm> getAlarms() {
        return this.alarms;
    }

    public Alarm toAlarmModel(Cursor cursor) {
        return new Alarm(cursor.getLong(0), cursor.getString(1), cursor.getLong(2));

    }
}
