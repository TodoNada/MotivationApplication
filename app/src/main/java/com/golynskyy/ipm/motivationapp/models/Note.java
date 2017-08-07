package com.golynskyy.ipm.motivationapp.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.golynskyy.ipm.motivationapp.basic.BasicNote;
import com.golynskyy.ipm.motivationapp.database.DatabaseStorable;

import static android.R.attr.name;

/**
 * Created by Dep5 on 27.07.2017.
 */

public class Note extends BasicNote implements DatabaseStorable{

   //database for storage
    private SQLiteDatabase db;

    public Note(SQLiteDatabase db) {
        this.db = db;
        this.id = -1;
    }

    public Note() {
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
        return DatabaseStructure.tables.notes;
    }

    // inserts note into table
    @Override
    public boolean insert() {
        if(db != null) {
            try {
                String insertSql = "INSERT INTO " + getTableNameInDb() + " (" +
                        DatabaseStructure.columns.note.noteName + ", " +
                        DatabaseStructure.columns.note.noteDesc + ", " +
                        DatabaseStructure.columns.note.status + ", " +
                        DatabaseStructure.columns.note.tags + ", " +
                        DatabaseStructure.columns.note.reminders + ", " +
                        DatabaseStructure.columns.note.type + ", " +
                        DatabaseStructure.columns.note.dateCreated + ", " +
                        DatabaseStructure.columns.note.lastModified + ", " +
                        DatabaseStructure.columns.note.beginDate + ", " +
                        DatabaseStructure.columns.note.endDate + ", " +
                        DatabaseStructure.columns.note.alarmIndex + " " +
                        ") VALUES (?,?,?,?,?,?,?,?,?,?,?)";


                int n = 1;
                SQLiteStatement ss = db.compileStatement(insertSql);
                ss.bindString(n++,getName());
                ss.bindString(n++,getDescription());
                ss.bindLong(n++, getStatus());
                ss.bindString(n++, getTags());
                ss.bindLong(n++, getReminders());
                ss.bindLong(n++, getType());
                ss.bindLong(n++, getDateCreated());
                ss.bindLong(n++, getLastModified());
                ss.bindLong(n++, getBeginDate());
                ss.bindLong(n++, getEndDate());
                ss.bindLong(n++, getAlarmIndex());

                long localId = ss.executeInsert();

                setId(localId);

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

    //updates note in table
    @Override
    public boolean update() {
        if(db != null)
        {
            try
            {

                String updateSql = "UPDATE " + getTableNameInDb() + " SET " +
                        DatabaseStructure.columns.note.id + " = ?, " +
                        DatabaseStructure.columns.note.noteName + " = ?, " +
                        DatabaseStructure.columns.note.noteDesc + " = ?, " +
                        DatabaseStructure.columns.note.status + " = ?, " +
                        DatabaseStructure.columns.note.tags + " = ?, " +
                        DatabaseStructure.columns.note.reminders + " = ?, " +
                        DatabaseStructure.columns.note.type + " = ?, " +
                        DatabaseStructure.columns.note.dateCreated + " = ?, " +
                        DatabaseStructure.columns.note.lastModified + " = ?, " +
                        DatabaseStructure.columns.note.beginDate + " = ?, " +
                        DatabaseStructure.columns.note.endDate + " = ?, " +
                        DatabaseStructure.columns.note.alarmIndex + " = ?" +
                        " WHERE " + DatabaseStructure.columns.note.id + " = ?";

                int n = 1;
                SQLiteStatement ss = db.compileStatement(updateSql);

                ss.bindLong(n++, getId());
                ss.bindString(n++,getName());
                ss.bindString(n++,getDescription());
                ss.bindLong(n++, getStatus());
                ss.bindString(n++, getTags());
                ss.bindLong(n++, getReminders());
                ss.bindLong(n++, getType());
                ss.bindLong(n++, getDateCreated());
                ss.bindLong(n++, getLastModified());
                ss.bindLong(n++, getBeginDate());
                ss.bindLong(n++, getEndDate());
                ss.bindLong(n++, getAlarmIndex());

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
                String removeSql = "DELETE FROM " + getTableNameInDb() + " WHERE " + DatabaseStructure.columns.note.id + "= ?";
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
