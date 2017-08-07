package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.golynskyy.ipm.motivationapp.adapters.NotesAdapter;
import com.golynskyy.ipm.motivationapp.adapters.RemindersAdapter;
import com.golynskyy.ipm.motivationapp.database.LocalDbStorage;
import com.golynskyy.ipm.motivationapp.models.DatabaseStructure;
import com.golynskyy.ipm.motivationapp.models.Note;
import com.golynskyy.ipm.motivationapp.models.Notes;
import com.golynskyy.ipm.motivationapp.models.Reminder;
import com.golynskyy.ipm.motivationapp.models.Reminders;

import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_ADD_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_REMINDERS_LIST_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_REMINDER_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_DELETE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NOT_UPDATED_REMINDER;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_REMOVE_REMINDER;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_REMINDER;

/**
 * Created by Dep5 on 29.07.2017.
 */

public class RemindersListActivity extends Activity {

    private ListView lvReminders;

    private RemindersAdapter remindersAdapter;
    private Reminders reminders = new Reminders();
    private long noteLocalIdReminders = -1;
    private long reminderLocalIdList = -1;
    private LocalDbStorage localDbStorageReminders;


    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString("NOTE_ID", ""+noteLocalIdReminders);

        Intent intent = new Intent();
        intent.putExtras(bundle);

        setResult(RESULT_CODE_UPDATE_REMINDER, intent);

        super.onBackPressed();
    }

    // for notes getting from DB
    private boolean getRemindersFromDb(long noteId) {
        localDbStorageReminders.reopen();
        reminders.setDb(localDbStorageReminders.getDb());
        boolean remindersLoaded = reminders.loadFromDb(DatabaseStructure.columns.reminders.noteId +" = ?",new String[] {""+noteId},0);
        localDbStorageReminders.close();
        return remindersLoaded;
    }


    //initialize views
    private void initViews() {

        lvReminders = (ListView) findViewById(R.id.listViewReminders);
        lvReminders.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Reminder reminder = (Reminder)adapter.getItemAtPosition(position);
                reminderLocalIdList = reminder.getId();
                Log.d("REMINDER_LIST ACTIVITY", "Noteid= "+noteLocalIdReminders);
                Intent intent = new Intent(RemindersListActivity.this, ReminderActivity.class);
                intent.putExtra("NOTE_ID",""+noteLocalIdReminders);
                intent.putExtra("REMINDER_ID",""+reminderLocalIdList);
                startActivityForResult(intent, REQUEST_CODE_FROM_REMINDER_ACTIVITY);

            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reminders_list);

        Intent intent = getIntent();
        Log.d("LIST_REMINDERS ", "Intent IS HERE");
        noteLocalIdReminders = Long.parseLong(intent.getStringExtra("NOTE_ID"));

        //initialize local DB
        localDbStorageReminders = new LocalDbStorage(this);

        getRemindersFromDb(noteLocalIdReminders);

        remindersAdapter = new RemindersAdapter(this,reminders);

        //initialize views
        initViews();

        lvReminders.setAdapter(remindersAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == REQUEST_CODE_FROM_REMINDER_ACTIVITY) {

            String stringId = data.getStringExtra("NOTE_ID");
            noteLocalIdReminders = Long.parseLong(stringId);
            stringId = data.getStringExtra("REMINDER_ID");
            reminderLocalIdList = Long.parseLong(stringId);

            if (resultCode == RESULT_CODE_NOT_UPDATED_REMINDER) return;

            // update reminders list
            localDbStorageReminders.reopen();
            reminders.clear();
            reminders.setDb(localDbStorageReminders.getDb());
            getRemindersFromDb(noteLocalIdReminders);
            localDbStorageReminders.close();
            remindersAdapter.notifyDataSetChanged();

            if (resultCode == RESULT_CODE_REMOVE_REMINDER) {
                  Log.d("REMINDERS_LST ACTIVITY", "REMOVED REMINDER_ID = " + reminderLocalIdList);
                return;
            }

            if (resultCode == RESULT_CODE_UPDATE_REMINDER) {
                 Log.d("REMINDERS_LST ACTIVITY", "UPD NOTE_ID = " + noteLocalIdReminders);
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
