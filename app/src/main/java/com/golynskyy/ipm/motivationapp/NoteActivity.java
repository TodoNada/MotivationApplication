package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_ADD_REMINDER_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_REMINDERS_LIST_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_UPDATE_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_DELETE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_REMINDER;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_NOTE;

/**
 * Created by Dep5 on 28.07.2017.
 */

public class NoteActivity extends Activity {

    private long noteLocalId;

    TextView tvName;
    TextView tvDescription;
    TextView tvBeginDate;
    TextView tvEndDate;
    TextView tvRemindersCount;
    TextView tvImportancy;


    Button btnUpdateProgress;
    Button btnDeleteNote;
    Button btnAddReminder;
    Button btnShowReminders;


    private void initViews() {
        tvName = (TextView)findViewById(R.id.textViewName);
        tvDescription = (TextView)findViewById(R.id.textViewDescription);
        tvBeginDate = (TextView)findViewById(R.id.textViewBeginDate);
        tvEndDate = (TextView)findViewById(R.id.textViewEndDate);
        tvRemindersCount = (TextView)findViewById(R.id.textViewRemindersCount);
        tvImportancy = (TextView)findViewById(R.id.textViewImportancyLevel);

        btnDeleteNote = (Button) findViewById(R.id.buttonDeleteNote);
        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: realize deleting of current note
                //TODO: realize adapter refreshing without deleted note
                //TODO: realize deleting current note form DB and it's reminders
                Intent intent = new Intent();
                intent.putExtra("NOTE_ID", "" + noteLocalId);
                setResult(RESULT_CODE_DELETE_NOTE,intent);
                finish();
            }
        });
        btnUpdateProgress = (Button) findViewById(R.id.buttonUpdateProgress);
        btnUpdateProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, NoteUpdateProgressActivity.class);
                intent.putExtra("NOTE_ID", "" + noteLocalId);
                startActivityForResult(intent, REQUEST_CODE_FROM_UPDATE_NOTE_ACTIVITY);
            }
        });
        btnAddReminder = (Button) findViewById(R.id.buttonAddReminder);
        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, ReminderAddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FROM_ADD_REMINDER_ACTIVITY);
            }
        });
        btnShowReminders = (Button) findViewById(R.id.buttonShowReminders);
        btnShowReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, RemindersListActivity.class);
                intent.putExtra("NOTE_ID", "" + noteLocalId);
                startActivityForResult(intent, REQUEST_CODE_FROM_REMINDERS_LIST_ACTIVITY);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notes);
        initViews();

        Intent intent = getIntent();
        Log.d("NOTE ", "Intent IS HERE");
        noteLocalId = Long.parseLong(intent.getStringExtra("NOTE_ID"));
        //TODO: realize getting Note by noteLocalId from DB
        tvName.setText(""+noteLocalId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (requestCode == REQUEST_CODE_FROM_UPDATE_NOTE_ACTIVITY) {
            if (resultCode != RESULT_CODE_UPDATE_NOTE) {
                Log.d("ACTIVITY_NOTE ","NOTE UPDATE ACTIVITY CLOSED without changes");
                return;
            }
            String stringId = data.getStringExtra("NOTE_ID");
            noteLocalId = Long.parseLong(stringId);
            //TODO: apply refreshing of updated Note
            Log.d("ACTIVITY_NOTE", "UPDATED NOTE_ID = "+noteLocalId);
            return;
        }
        if (requestCode == REQUEST_CODE_FROM_ADD_REMINDER_ACTIVITY) {
            if (resultCode != RESULT_CODE_NEW_REMINDER) {
                Log.d("ACTIVITY_NOTE ","ADD REMINDER ACTIVITY CLOSED without changes");
                return;
            }
            //TODO: update note's reminder count
            Log.d("ACTIVITY_NOTE ","CREATED NEW REMINDER");
            return;
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
