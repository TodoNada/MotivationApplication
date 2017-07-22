package com.golynskyy.ipm.motivationapp.basic;

import java.util.ArrayList;

/**
 * Created by Dep5 on 24.07.2017.
 */

public class BasicReminders extends ArrayList<BasicReminder> {


    public static BasicReminders fromArrayList(ArrayList<BasicReminder> basicArrayList)
    {
        BasicReminders list = new BasicReminders();
        for(int i=0; i<basicArrayList.size(); i++)
        {
            list.set(i, basicArrayList.get(i));
        }
        return list;
    }

    public BasicReminder get(int i)
    {
        return (BasicReminder) super.get(i);
    }


}
