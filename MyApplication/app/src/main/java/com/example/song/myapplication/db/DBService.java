package com.example.song.myapplication.db;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Kody on 10/21/2015.
 */
public class DBService extends SQLiteOpenHelper {

    public static DBService dbInstance;

    public static final String TABLE_NAME = "alarm";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_WEATHERENABLED = "weather_enabled";
    public static final String COLUMN_TRAFFICENABLED = "traffic_enabled";
    public static final String COLUMN_ORIGIN = "origin";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_TIMEESTIMATE = "time_estimate";


    private static final String DATABASE_NAME = "alarm.db";
    private static final int DATABASE_VERSION = 7;

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_NAME + " text not null, " + COLUMN_TIME + " integer not null, " + COLUMN_WEATHERENABLED + " boolean not null, " + COLUMN_TRAFFICENABLED + " boolean not null, " + COLUMN_ORIGIN + " text, " + COLUMN_DESTINATION + " text, " + COLUMN_TIMEESTIMATE + " real" + ");";

    private Context dbCtx;

    public static DBService getInstance(Context ctx) {
        if (dbInstance == null) {
            dbInstance = new DBService(ctx.getApplicationContext());
        }
        return dbInstance;
    }

    public DBService (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.dbCtx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBService.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }


}
