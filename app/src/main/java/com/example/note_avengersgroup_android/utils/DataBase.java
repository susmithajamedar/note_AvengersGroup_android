package com.example.note_avengersgroup_android.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.note_avengersgroup_android.models.NoteModel;

import java.util.ArrayList;

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

    public boolean insertCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", name);
        db.insert(Constants.TABLE_CATEGORY, null, contentValues);
        return true;
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + Constants.TABLE_CATEGORY, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("category")));
            res.moveToNext();
        }
        return array_list;
    }

    public boolean insertNote(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", noteModel.getCategory());
        contentValues.put("note", noteModel.getNote());
        contentValues.put("annotation", noteModel.getAnnotation());
        contentValues.put("imageOne", noteModel.getImageOne());
        contentValues.put("imageTwo", noteModel.getImageTwo());
        contentValues.put("audio", noteModel.getAudio());
        contentValues.put("location", noteModel.getLocation());
        contentValues.put("date", noteModel.getDate());
        db.insert(Constants.TABLE_NOTES, null, contentValues);
        return true;
    }

    


}
