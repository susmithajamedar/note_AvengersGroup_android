package com.example.note_avengersgroup_android.utils;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Activity activity) {
        super(activity, Constants.DATABASE_NAME, null, 1);
    }

    //OnCreating Database Also create 2 Tables Category and Notes
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + Constants.TABLE_CATEGORY +
                        " (category text PRIMARY KEY)"
        );
        sqLiteDatabase.execSQL(
                "create table " + Constants.TABLE_NOTES +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT,category text, note text, annotation text , imageOne text , imageTwo text , audio text , location text , date text )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
