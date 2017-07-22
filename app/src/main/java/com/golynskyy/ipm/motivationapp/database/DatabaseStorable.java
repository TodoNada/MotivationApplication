package com.golynskyy.ipm.motivationapp.database;

/**
 * Created by Dep5 on 24.07.2017.
 */

public interface DatabaseStorable {

    public String getTableNameInDb();
    public boolean insert();
    public boolean update();
    public boolean remove();

}
