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
    private String[] columns = {DBService.COLUMN_ID, DBService.COLUMN_NAME, DBService.COLUMN_TIME, DBService.COLUMN_WEATHERENABLED, DBService.COLUMN_TRAFFICENABLED, DBService.COLUMN_ORIGIN, DBService.COLUMN_DESTINATION, DBService.COLUMN_TIMEESTIMATE, DBService.COLUMN_NEW_TIME};
    private ArrayList<Alarm> alarms;
    private Context ctx;

    private static AlarmDBHelper alarmDBInstance;

    /**
     * singleton
     * @param ctx
     * @return
     */
    public static AlarmDBHelper getInstance(Context ctx) {
        if (alarmDBInstance == null) {
            alarmDBInstance = new AlarmDBHelper(ctx.getApplicationContext());
        }
        return alarmDBInstance;
    }

    public AlarmDBHelper (Context context) {
        dbservice = DBService.getInstance(context);
        ctx = context;
        updateAlarms();
    }

    /**
     * get db
     * @throws SQLException
     */
    public void open() throws SQLException{
        db = dbservice.getWritableDatabase();
    }

    /**
     * close db access to avoid overflow
     */
    public void close() {
        dbservice.close();
    }

    /**
     *
     * @return current db
     */
    public SQLiteDatabase getDb () {
        if (db == null) try {
            this.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return db;
    }

    /**
     * populate alarm information and set it into db
     * @param alarm
     * @return
     */
    public Alarm addAlarm(Alarm alarm) {
        //alarmManagerService.setAlarm(Utilities.minutesToTime(alarm.getTime()), ctx);
        ContentValues values = new ContentValues();
        values.put(DBService.COLUMN_NAME, alarm.getName());
        values.put(DBService.COLUMN_TIME, alarm.getTime());
        values.put(DBService.COLUMN_WEATHERENABLED, alarm.isWeatherEnabled());
        values.put(DBService.COLUMN_TRAFFICENABLED, alarm.isTrafficEnabled());
        if (alarm.isTrafficEnabled()) {
            if (alarm.getOrigin() != null && alarm.getOrigin() != "") {
                values.put(DBService.COLUMN_ORIGIN, alarm.getOrigin());
            }
            if (alarm.getDestination() != null && alarm.getDestination() != "") {
                values.put(DBService.COLUMN_DESTINATION, alarm.getDestination());
            }
            values.put(DBService.COLUMN_TIMEESTIMATE, alarm.getTimeEstimate());
        }
        values.put(DBService.COLUMN_NEW_TIME, alarm.getNewTime());
        long insertID = getDb().insert(DBService.TABLE_NAME, null, values);
        Cursor cursor = getDb().query(DBService.TABLE_NAME, columns, DBService.COLUMN_ID + " =" + insertID, null, null, null, null);
        cursor.moveToFirst();
        Alarm a = toAlarmModel(cursor);
        updateAlarms();
        return a;
    }

    /**
     * getter for alarm
     * @param position
     * @return alarm based on index position
     */
    public Alarm getAlarm(int position) {
        ArrayList<Alarm> alarms = getAlarms();
        return alarms.get(position);
    }

    /**
     * update alarm
     */
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

    /**
     * get all the alarms available in the db
     * @return
     */
    public ArrayList<Alarm> getAlarms() {
        return this.alarms;
    }

    public Alarm toAlarmModel(Cursor cursor) {
        return new Alarm(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getFloat(7), cursor.getInt(8));

    }
}
