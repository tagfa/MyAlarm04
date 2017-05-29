package com.example.tag.myalarm04;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import java.net.URI;

/**
 * Created by tag on 2017/05/27.
 */

public class DBAdapter {

    //DB名
    static final String DATABASE_NAME = "myAlarm.db";
    //TBL名
    public static final String TABLE_NAME = "alarm";
    //バージョン
    static final int DATABASE_VERSION = 2;

    //DBのカラム名
    public static final String ALARMID = "_id";
    public static final String ALARMNAME = "ALARMNAME";
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String DAY = "DAY";
    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";
    public static final String SOUNDFILEPATH="SOUNDFILEPATH";

    protected Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    // Adapter Methods
    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        dbHelper.close(); }


    // // App Methods //
    public boolean deleteAllAlarm(){
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public boolean deleteAlarm(int id){
        return db.delete(TABLE_NAME, ALARMID + "=" + id, null) > 0;
    }

    public Cursor getAllAlarm(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public int saveAlarm(String alarmName, int year, int month, int day, int hour, int minute, String soundFilePath){
        ContentValues values = new ContentValues();

        values.put(ALARMNAME,alarmName);
        values.put(YEAR,year);
        values.put(MONTH,month+1);
        values.put(DAY,day);
        values.put(HOUR,hour);
        values.put(MINUTE,minute);
        values.put(SOUNDFILEPATH,soundFilePath);

        long status = db.insert(TABLE_NAME, null, values);
        Cursor c = db.query(TABLE_NAME,
                null,  //columns
                null,  //selection
                null,  //selectionArgs
                null,  //groupby
                null,  //having
                ALARMID+" desc"); //orderby

        long recodeCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        System.out.printf("recodeCount : " + recodeCount);
        c.moveToFirst();
        int deleteAlarmID = c.getInt(c.getColumnIndex(DBAdapter.ALARMID));
        return deleteAlarmID;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL( "CREATE TABLE " + TABLE_NAME +
                    " (" + ALARMID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ALARMNAME + " TEXT NOT NULL," + YEAR + " INTEGER NOT NULL,"
                    + MONTH+ " INTEGER NOT NULL,"     + DAY  + " INTEGER NOT NULL,"
                    + HOUR + " INTEGER NOT NULL,"      + MINUTE + " INTEGER NOT NULL,"
                    + SOUNDFILEPATH + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

    }
}

