package com.example.song.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kody on 10/21/2015.
 */
public class DBService extends SQLiteOpenHelper {

    public static DBService dbInstance;

    public static final String TABLE_NAME = "alarm";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";

    private static final String DATABASE_NAME = "alarm.db";
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_NAME + " text not null, " + COLUMN_TIME + " integer not null);";

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


}
