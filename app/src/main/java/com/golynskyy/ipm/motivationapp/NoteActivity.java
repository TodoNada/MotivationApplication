package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.golynskyy.ipm.motivationapp.database.LocalDbStorage;
import com.golynskyy.ipm.motivationapp.dialogs.DialogDateAndTime;
import com.golynskyy.ipm.motivationapp.models.DatabaseStructure;
import com.golynskyy.ipm.motivationapp.models.Note;
import com.golynskyy.ipm.motivationapp.models.Notes;
import com.golynskyy.ipm.motivationapp.models.Reminder;
import com.golynskyy.ipm.motivationapp.models.Reminders;


import java.util.Calendar;
import java.util.Iterator;

import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_ADD_REMINDER_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_REMINDERS_LIST_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_UPDATE_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_DELETE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_REMINDER;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_REMINDER;
import static com.golynskyy.ipm.motivationapp.util.Utils.formatDateTimeToString;

/**
 * Created by Dep5 on 28.07.2017.
 */

public class NoteActivity extends Activity {

    private long noteLocalId = -1;
    private Note currentNote;
    private LocalDbStorage localDbStorageNote;

    private TextView tvName;
    private TextView tvDescription;
    private TextView tvBeginDate;
    private TextView tvEndDate;
    private TextView tvRemindersCount;
    private TextView tvImportancy;

    private ProgressBar pbTaskProgress;
    private ProgressBar pbTimeProgress;

    private Button btnUpdateProgress;
    private Button btnDeleteNote;
    private Button btnAddReminder;
    private Button btnShowReminders;


    private void initViews() {

        tvName = (TextView)findViewById(R.id.textViewName);
            tvName.setText(currentNote.getName());
        tvDescription = (TextView)findViewById(R.id.textViewDescription);
            tvDescription.setText(currentNote.getDescription());
        tvBeginDate = (TextView)findViewById(R.id.textViewBeginDate);
            tvBeginDate.setText(formatDateTimeToString(currentNote.getBeginDate()));
        tvEndDate = (TextView)findViewById(R.id.textViewEndDate);
            tvEndDate.setText(formatDateTimeToString(currentNote.getEndDate()));
        tvRemindersCount = (TextView)findViewById(R.id.textViewRemindersCount);
            tvRemindersCount.setText(""+currentNote.getReminders());
        tvImportancy = (TextView)findViewById(R.id.textViewImportancyLevel);
            tvImportancy.setText(""+currentNote.getType());
        pbTaskProgress = (ProgressBar)findViewById(R.id.progressBarTaskPercentage);
            pbTaskProgress.setProgress(currentNote.getStatus());
        pbTimeProgress = (ProgressBar)findViewById(R.id.progressBarTimeProgress);
            //TODO: realize checking if now time is not in range beginTime..endTime
            Calendar calendar = Calendar.getInstance();
            long nowTime = calendar.getTimeInMillis();
            pbTimeProgress.setProgress((int) (100*(nowTime - currentNote.getBeginDate())/(currentNote.getEndDate() - currentNote.getBeginDate())));

        btnDeleteNote = (Button) findViewById(R.id.buttonDeleteNote);
        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deleteNoteFromDatabase()) {
                    Log.d("ACTIVITY NOTE", "note not deleted");
                    return;
                }
                if (!deleteNoteRemindersFromDatabase(noteLocalId)) {
                    Log.d("ACTIVITY NOTE", "reminders for note were not deleted");
                    return;
                }

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
                Intent intent = new Intent(NoteActivity.this, NoteUpdateActivity.class);
                intent.putExtra("NOTE_ID", "" + noteLocalId);
                startActivityForResult(intent, REQUEST_CODE_FROM_UPDATE_NOTE_ACTIVITY);
            }
        });
        btnAddReminder = (Button) findViewById(R.id.buttonAddReminder);
        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, ReminderAddActivity.class);
                intent.putExtra("NOTE_ID", "" + noteLocalId);
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

    private boolean getNoteFromDatabase(long id) {
        localDbStorageNote.reopen();
        Notes notes = new Notes(localDbStorageNote.getDb());
        boolean notesSelected = notes.loadFromDb(DatabaseStructure.columns.note.id+" = ?",new String[] {""+id},0);
        currentNote = (Note)notes.get(0);
        localDbStorageNote.close();
        return notesSelected;
    }


    private boolean deleteNoteFromDatabase() {
        localDbStorageNote.reopen();
        currentNote.setDb(localDbStorageNote.getDb());
        boolean noteDeleted = currentNote.remove();
        localDbStorageNote.close();
        return noteDeleted;
    }

    private boolean deleteNoteRemindersFromDatabase(long noteId) {
        localDbStorageNote.reopen();
        Reminders reminders = new Reminders(localDbStorageNote.getDb());
        reminders.loadFromDb(DatabaseStructure.columns.reminders.noteId+" = ?",new String[] {""+noteId},0);
        Iterator<Reminder> iteratorReminders = reminders.iterator();
        try {
            while (iteratorReminders.hasNext()) {
                Reminder reminderIter = iteratorReminders.next();
                reminderIter.setDb(localDbStorageNote.getDb());
                Log.d("NOTE ACTIVITY", "try to delete reminder with ID = "+reminderIter.getId());
                if (!reminderIter.remove()) return false;
            }
        }
         catch (Exception e) {
             e.printStackTrace();
         }
            finally {
            localDbStorageNote.close();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notes);


        Intent intent = getIntent();
        Log.d("NOTE ", "Intent IS HERE");
        noteLocalId = Long.parseLong(intent.getStringExtra("NOTE_ID"));

        //initialize db
        localDbStorageNote = new LocalDbStorage(this);

        // if note can not be loaded then no initialization
        if (!getNoteFromDatabase(noteLocalId)) return;

        //initialize views
        initViews();

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
            if (!getNoteFromDatabase(noteLocalId)) return;

            //initialize views for updated note
            initViews();

            Log.d("ACTIVITY_NOTE", "UPDATED NOTE_ID = "+noteLocalId);
            return;
        }
        if (requestCode == REQUEST_CODE_FROM_ADD_REMINDER_ACTIVITY) {
            if (resultCode != RESULT_CODE_NEW_REMINDER) {
                Log.d("ACTIVITY_NOTE ","ADD REMINDER ACTIVITY CLOSED without changes");
                return;
            }

            String stringId = data.getStringExtra("NOTE_ID");
            noteLocalId = Long.parseLong(stringId);
            if (!getNoteFromDatabase(noteLocalId)) return;

            //initialize views for note with updated reminders
            initViews();

            Log.d("ACTIVITY_NOTE ","CREATED NEW REMINDER");
            return;
        }
        if (requestCode == REQUEST_CODE_FROM_REMINDERS_LIST_ACTIVITY) {
            if (resultCode == RESULT_CODE_UPDATE_REMINDER) {
                String stringId = data.getStringExtra("NOTE_ID");
                noteLocalId = Long.parseLong(stringId);
                if (!getNoteFromDatabase(noteLocalId)) return;

                //initialize views for note with updated reminders
                initViews();

                Log.d("ACTIVITY_NOTE ","UPDATED REMINDERS COUNT");
                return;
            }

            Log.d("ACTIVITY_NOTE ","UNKNOWN REQUEST CODE");
            return;
        }

    }


    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString("NOTE_ID", ""+noteLocalId);

        Intent intent = new Intent();
        intent.putExtras(bundle);

        setResult(RESULT_CODE_UPDATE_NOTE, intent);
        super.onBackPressed();
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
