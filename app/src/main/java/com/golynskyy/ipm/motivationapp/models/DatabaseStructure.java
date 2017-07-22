package com.golynskyy.ipm.motivationapp.models;

/**
 * Created by Dep5 on 22.07.2017.
 */

public final class DatabaseStructure {

    public static final class tables {
        public static final String notes = "notes";
        public static final String reminders = "reminders";
    }

    public static final class columns {

        public static final class note {
            public static final String id = "id";
            public static final String noteName = "name";
            public static final String noteDesc = "description";
            public static final String status = "status";
            public static final String tags = "tags";
            public static final String reminders = "reminders";
            public static final String type = "type";
            public static final String dateCreated = "date_created";
            public static final String lastModified = "last_modified";
            public static final String beginDate = "begin_date";
            public static final String endDate = "end_date";
            public static final String alarmIndex = "alarm_index";
        }


        public static final class reminders {
            public static final String id = "id";
            public static final String type = "type";
            public static final String targetDate = "target_date";
            public static final String noteId = "note_id";
            public static final String gap = "gap";
            public static final String title = "title";
            public static final String details = "details";
            public static final String vibrate = "vibrate";
            public static final String sound = "sound";
            public static final String light = "light";
        }
    }

}
