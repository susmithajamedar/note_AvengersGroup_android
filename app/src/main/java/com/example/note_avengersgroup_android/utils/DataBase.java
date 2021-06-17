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

    public ArrayList<NoteModel> getAllNote(String category) {
        ArrayList<NoteModel> array_list = new ArrayList<NoteModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + Constants.TABLE_NOTES + " where category='" + category + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            NoteModel noteModel = new NoteModel();
            noteModel.setId(res.getInt(res.getColumnIndex("id")));
            noteModel.setCategory(res.getString(res.getColumnIndex("category")));
            noteModel.setNote(res.getString(res.getColumnIndex("note")));
            noteModel.setAnnotation(res.getString(res.getColumnIndex("annotation")));
            noteModel.setImageOne(res.getString(res.getColumnIndex("imageOne")));
            noteModel.setImageTwo(res.getString(res.getColumnIndex("imageTwo")));
            noteModel.setAudio(res.getString(res.getColumnIndex("audio")));
            noteModel.setLocation(res.getString(res.getColumnIndex("location")));
            noteModel.setDate(res.getString(res.getColumnIndex("date")));
            array_list.add(noteModel);
            res.moveToNext();
        }
        return array_list;
    }

    public int updateNote(NoteModel noteModel, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("note", noteModel.getNote());
        values.put("annotation", noteModel.getAnnotation());
        values.put("imageOne", noteModel.getImageOne());
        values.put("imageTwo", noteModel.getImageTwo());
        values.put("audio", noteModel.getAudio());
        values.put("location", noteModel.getLocation());
        values.put("category", noteModel.getCategory());
        return db.update(Constants.TABLE_NOTES, values, "id" + " =?", new String[]{String.valueOf(id)});
    }

    public int deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(Constants.TABLE_NOTES, "id" + "=?", new String[]{String.valueOf(id)});
        db.close();
        return i;
    }


}
