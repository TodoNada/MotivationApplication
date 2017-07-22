package com.golynskyy.ipm.motivationapp.basic;

import static android.R.attr.type;

/**
 * Created by Dep5 on 24.07.2017.
 */

public class BasicReminder {


    protected long id;
    protected long targetDate;
    protected int type;
    protected long noteId;
    protected long gap;
    protected String title;
    protected String details;
    protected int vibrate;
    protected int sound;
    protected int light;


    public BasicReminder () {
        targetDate = 0;
        type = -1;
        gap = 0;
        title = "";
        details = "";
        vibrate = -1;
        sound = -1;
        light = -1;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(long targetDate) {
        this.targetDate = targetDate;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getGap() {
        return gap;
    }

    public void setGap(long gap) {
        this.gap = gap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }





}
