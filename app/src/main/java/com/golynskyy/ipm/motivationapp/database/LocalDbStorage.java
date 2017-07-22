package com.golynskyy.ipm.motivationapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dep5 on 23.07.2017.
 */

public class LocalDbStorage {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public LocalDbStorage(Context context)
    {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void reopen()
    {
        db = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        try
        {
            db.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getDb()
    {
        return db;
    }
}
