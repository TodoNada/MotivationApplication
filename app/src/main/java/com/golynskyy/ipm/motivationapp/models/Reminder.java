package com.golynskyy.ipm.motivationapp.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.golynskyy.ipm.motivationapp.basic.BasicReminder;
import com.golynskyy.ipm.motivationapp.database.DatabaseStorable;

/**
 * Created by Dep5 on 27.07.2017.
 */

public class Reminder extends BasicReminder implements DatabaseStorable {


    private SQLiteDatabase db;

    public Reminder(SQLiteDatabase db)
    {
        this.db = db;
    }

    public Reminder()
    {
        this(null);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public String getTableNameInDb() {
        return DatabaseStructure.tables.reminders;
    }

    @Override
    public boolean insert()
    {
        if(db != null)
        {
            try {
                String insertSql = "INSERT INTO " + getTableNameInDb() + " (" +
                        DatabaseStructure.columns.reminders.id + ", " +
                        DatabaseStructure.columns.reminders.type + ", " +
                        DatabaseStructure.columns.reminders.targetDate + ", " +
                        DatabaseStructure.columns.reminders.noteId + ", " +
                        DatabaseStructure.columns.reminders.gap + ", " +
                        DatabaseStructure.columns.reminders.title + ", " +
                        DatabaseStructure.columns.reminders.details + ", " +
                        DatabaseStructure.columns.reminders.vibrate + ", " +
                        DatabaseStructure.columns.reminders.sound + ", " +
                        DatabaseStructure.columns.reminders.light + " " +
                        ") VALUES (?,?,?,?,?,?,?,?,?,?)";

                int n = 1;
                SQLiteStatement ss = db.compileStatement(insertSql);
                ss.bindLong(n++, getId());
                ss.bindLong(n++, getType());
                ss.bindLong(n++, getTargetDate());
                ss.bindLong(n++, getNoteId());
                ss.bindLong(n++, getGap());
                ss.bindString(n++, getTitle());
                ss.bindString(n++, getDetails());
                ss.bindLong(n++, getVibrate());
                ss.bindLong(n++, getSound());
                ss.bindLong(n++, getLight());

                ss.execute();
                ss.close();

                setId(getId());

                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    //updates note in table
    @Override
    public boolean update() {
        if(db != null)
        {
            try
            {

                        String updateSql = "UPDATE " + getTableNameInDb() + " SET " +
                        DatabaseStructure.columns.reminders.id + " = ?, " +
                        DatabaseStructure.columns.reminders.type + " = ?, " +
                        DatabaseStructure.columns.reminders.targetDate + " = ?, " +
                        DatabaseStructure.columns.reminders.noteId + " = ?, " +
                        DatabaseStructure.columns.reminders.gap + " = ?, " +
                        DatabaseStructure.columns.reminders.title + " = ?, " +
                        DatabaseStructure.columns.reminders.details + " = ?, " +
                        DatabaseStructure.columns.reminders.vibrate + " = ?, " +
                        DatabaseStructure.columns.reminders.sound + " = ?, " +
                        DatabaseStructure.columns.reminders.light + " = ?, " +
                        " WHERE " + DatabaseStructure.columns.reminders.id + " = ?";

                int n = 1;
                SQLiteStatement ss = db.compileStatement(updateSql);

                ss.bindLong(n++, getId());
                ss.bindLong(n++,getType());
                ss.bindLong(n++,getTargetDate());
                ss.bindLong(n++,getNoteId());
                ss.bindLong(n++,getGap());
                ss.bindString(n++,getTitle());
                ss.bindString(n++,getDetails());
                ss.bindLong(n++, getVibrate());
                ss.bindLong(n++, getSound());
                ss.bindLong(n++, getLight());

                ss.bindLong(n++, getId());

                ss.execute();
                ss.close();

                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;

    }


    @Override
    public boolean remove() {
        if(db != null && getId() > 0)
        {
            try {
                String removeSql = "DELETE FROM " + getTableNameInDb() + " WHERE " + DatabaseStructure.columns.reminders.id + "= ?";
                int n = 1;
                SQLiteStatement ss = db.compileStatement(removeSql);
                ss.bindLong(n++, getId());
                ss.execute();
                ss.close();
                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;

    }


}
