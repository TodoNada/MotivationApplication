package com.golynskyy.ipm.motivationapp.database;

/**
 * Created by Dep5 on 24.07.2017.
 */

public interface DatabaseSelectable {

    public String getTableNameInDb();
    public boolean loadFromDb(String sqlCriteria, String[] params, int limit);


}
