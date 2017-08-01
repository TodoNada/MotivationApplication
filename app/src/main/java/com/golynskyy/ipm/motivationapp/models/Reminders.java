package com.golynskyy.ipm.motivationapp.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.golynskyy.ipm.motivationapp.database.DatabaseSelectable;
import com.golynskyy.ipm.motivationapp.basic.BasicReminder;

import java.util.ArrayList;

/**
 * Created by Dep5 on 27.07.2017.
 */

public class Reminders<T extends BasicReminder> extends ArrayList<T> implements DatabaseSelectable {

    private SQLiteDatabase db;

    public Reminders() {
        this(null);
    }

    public Reminders (SQLiteDatabase db) {
        this.db = db;
    }

    public SQLiteDatabase getDb()
    {
        return db;
    }

    public void setDb(SQLiteDatabase db)
    {
        this.db = db;
    }


    public static Reminders fromArrayList(ArrayList<BasicReminder> basicArrayList)
    {
        Reminders list = new Reminders();
        for(int i=0; i<basicArrayList.size(); i++)
        {
            list.set(i, basicArrayList.get(i));
        }
        return list;
    }

    @Override
    public T get(int i)
    {
        return super.get(i);
    }

    @Override
    public String getTableNameInDb() {
        return DatabaseStructure.tables.reminders;
    }

    @Override
    public boolean loadFromDb(String sqlCriteria, String[] params, int limit) {
        try
        {
            if(db != null)
            {
                String sqlString = "SELECT * FROM "+getTableNameInDb();
                if(params.length > 0)
                    sqlString +=  " WHERE "+sqlCriteria;

                Cursor loader = db.rawQuery(sqlString,	params);

                if(!loader.moveToFirst())
                    return true; // no rows in the db table, do something?

                while(!loader.isAfterLast())
                {

                    Reminder reminder = new Reminder();
                    reminder.setId(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.reminders.id)));
                    reminder.setTargetDate(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.reminders.targetDate)));
                    reminder.setNoteId(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.reminders.noteId)));
                    reminder.setGap(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.reminders.gap)));
                    reminder.setTitle(loader.getString(loader.getColumnIndex(DatabaseStructure.columns.reminders.title)));
                    reminder.setDetails(loader.getString(loader.getColumnIndex(DatabaseStructure.columns.reminders.details)));
                    reminder.setVibrate(loader.getInt(loader.getColumnIndex(DatabaseStructure.columns.reminders.vibrate)));
                    reminder.setSound(loader.getInt(loader.getColumnIndex(DatabaseStructure.columns.reminders.sound)));
                    reminder.setLight(loader.getInt(loader.getColumnIndex(DatabaseStructure.columns.reminders.light)));


                    this.add((T)reminder);

                    loader.moveToNext();
                }

                loader.close();
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;

    }
}
