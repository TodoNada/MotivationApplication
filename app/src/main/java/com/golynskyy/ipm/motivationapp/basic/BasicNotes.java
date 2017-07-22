package com.golynskyy.ipm.motivationapp.basic;

import java.util.ArrayList;

/**
 * Created by Dep5 on 24.07.2017.
 */

public class BasicNotes extends ArrayList<BasicNote> {

    public static BasicNotes fromArrayList(ArrayList<BasicNote> basicArrayList)
    {
        BasicNotes list = new BasicNotes();
        for(int i=0; i<basicArrayList.size(); i++)
        {
            list.set(i, basicArrayList.get(i));
        }
        return list;
    }


    public BasicNote get(int i)
    {
        return (BasicNote) super.get(i);
    }

}
