package com.golynskyy.ipm.motivationapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.golynskyy.ipm.motivationapp.models.DatabaseStructure;

import static com.golynskyy.ipm.motivationapp.models.DatabaseStructure.columns.note.status;

/**
 * Created by Dep5 on 23.07.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    static final int DB_VERSION = 42;
    static final String DB_NAME = "motivation_app";

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL("CREATE TABLE "+ DatabaseStructure.tables.notes+" (" +
                DatabaseStructure.columns.note.id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DatabaseStructure.columns.note.noteName+" TEXT, " +
                DatabaseStructure.columns.note.noteDesc+" TEXT, " +
                DatabaseStructure.columns.note.status+" INTEGER, " +
                DatabaseStructure.columns.note.tags+" TEXT, " +
                DatabaseStructure.columns.note.reminders+" INTEGER, "+
                DatabaseStructure.columns.note.type+" INTEGER, " +
                DatabaseStructure.columns.note.dateCreated+" INTEGER, " +
                DatabaseStructure.columns.note.lastModified+" INTEGER, " +
                DatabaseStructure.columns.note.beginDate+" INTEGER, " +
                DatabaseStructure.columns.note.endDate+" INTEGER, " +
                DatabaseStructure.columns.note.alarmIndex+" INTEGER "+
                ")");

        // create reminders table
        db.execSQL("CREATE TABLE "+ DatabaseStructure.tables.reminders+" (" +
                DatabaseStructure.columns.reminders.id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DatabaseStructure.columns.reminders.type+" INTEGER, "+
                DatabaseStructure.columns.reminders.targetDate+" INTEGER, "+
                DatabaseStructure.columns.reminders.noteId+" INTEGER, " +
                DatabaseStructure.columns.reminders.gap+" INTEGER, " +
                DatabaseStructure.columns.reminders.title+" TEXT, " +
                DatabaseStructure.columns.reminders.details+" TEXT, " +
                DatabaseStructure.columns.reminders.light+" INTEGER, " +
                DatabaseStructure.columns.reminders.vibrate+" INTEGER, " +
                DatabaseStructure.columns.reminders.sound+" INTEGER, " +
                ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
