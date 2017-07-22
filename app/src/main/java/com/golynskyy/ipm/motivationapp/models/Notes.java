package com.golynskyy.ipm.motivationapp.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.golynskyy.ipm.motivationapp.basic.BasicNotes;
import com.golynskyy.ipm.motivationapp.database.DatabaseSelectable;

/**
 * Created by Dep5 on 27.07.2017.
 */

public class Notes extends BasicNotes implements DatabaseSelectable {

    private SQLiteDatabase db;

    public Notes(SQLiteDatabase db)
    {
        this.db = db;
    }

    public Notes()
    {
        this(null);
    }

    public SQLiteDatabase getDb()
    {
        return db;
    }

    public void setDb(SQLiteDatabase db)
    {
        this.db = db;
    }


    @Override
    public String getTableNameInDb() {
        return DatabaseStructure.tables.notes;
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
                    Note note = new Note();
                    note.setId(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.note.id)));
                    note.setName(loader.getString(loader.getColumnIndex(DatabaseStructure.columns.note.noteName)));
                    note.setDescription(loader.getString(loader.getColumnIndex(DatabaseStructure.columns.note.noteDesc)));
                    note.setStatus(loader.getInt(loader.getColumnIndex(DatabaseStructure.columns.note.status)));
                    note.setTags(loader.getString(loader.getColumnIndex(DatabaseStructure.columns.note.tags)));
                    note.setReminders(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.note.reminders)));
                    note.setType(loader.getInt(loader.getColumnIndex(DatabaseStructure.columns.note.type)));
                    note.setDateCreated(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.note.dateCreated)));
                    note.setLastModified(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.note.lastModified)));
                    note.setBeginDate(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.note.beginDate)));
                    note.setEndDate(loader.getLong(loader.getColumnIndex(DatabaseStructure.columns.note.endDate)));
                    note.setAlarmIndex(loader.getInt(loader.getColumnIndex(DatabaseStructure.columns.note.alarmIndex)));

                    this.add(note);

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
