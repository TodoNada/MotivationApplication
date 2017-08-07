package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.golynskyy.ipm.motivationapp.database.LocalDbStorage;
import com.golynskyy.ipm.motivationapp.models.DatabaseStructure;
import com.golynskyy.ipm.motivationapp.models.Note;
import com.golynskyy.ipm.motivationapp.models.Notes;
import com.golynskyy.ipm.motivationapp.models.Reminder;
import com.golynskyy.ipm.motivationapp.models.Reminders;

import java.util.Calendar;

import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_ADD_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_UPDATE_REMINDER_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_DELETE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_REMINDER;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NOT_UPDATED_REMINDER;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_REMOVE_REMINDER;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_REMINDER;
import static com.golynskyy.ipm.motivationapp.util.Utils.formatDateTimeToString;

/**
 * Created by Dep5 on 28.07.2017.
 */

public class ReminderActivity extends Activity {

    private int codeReminder = RESULT_CODE_NOT_UPDATED_REMINDER;

    private long noteLocalIdReminder = -1;
    private long reminderLocalId = -1;

    private Note currentNoteReminder;
    private Reminder currentReminder;
    private LocalDbStorage localDbStorageReminder;


    private Button btnChangeReminder;
    private Button btnRemoveReminder;

    private TextView tvNameReminder;
    private TextView tvDetailsReminder;
    private TextView tvReminderTargetDate;
    private TextView tvReminderType;


    private Switch swSoundReminder;
    private Switch swLigthReminder;
    private Switch swVibrationReminder;


    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString("NOTE_ID", ""+noteLocalIdReminder);
        bundle.putString("REMINDER_ID", ""+reminderLocalId);

        Intent intent = new Intent();
        intent.putExtras(bundle);

        setResult(codeReminder, intent);
        super.onBackPressed();
    }

    private boolean getReminderFromDatabase(long id) {
        localDbStorageReminder.reopen();
        Reminders reminders = new Reminders(localDbStorageReminder.getDb());
        boolean reminderSelected = reminders.loadFromDb(DatabaseStructure.columns.reminders.id+" = ?",new String[] {""+id},0);
        currentReminder = (Reminder)reminders.get(0);
        Log.d("REMND TYPE = ",""+currentReminder.getType());
        localDbStorageReminder.close();
        return reminderSelected;
    }


    private boolean getNoteFromDatabase(long id) {
        localDbStorageReminder.reopen();
        Notes notes = new Notes(localDbStorageReminder.getDb());
        boolean noteSelected = notes.loadFromDb(DatabaseStructure.columns.note.id+" = ?",new String[] {""+id},0);
        currentNoteReminder = (Note)notes.get(0);
        localDbStorageReminder.close();
        return noteSelected;
    }


    private boolean removeReminderFromDatabase() {

        //load note from id
        getNoteFromDatabase(noteLocalIdReminder);

        //try to delete reminder from db
        localDbStorageReminder.reopen();
        currentReminder.setDb(localDbStorageReminder.getDb());
        if  (!currentReminder.remove()) {
            Log.d("REMINDER ACTIVITY"," can not to remove reminder with id = " + reminderLocalId);
            localDbStorageReminder.close();
            return false;
        }

        //TODO: realize checking if count of reminders in note is > 1

        //decrease reminders count and try to update current reminder note
        currentNoteReminder.setDb(localDbStorageReminder.getDb());
        currentNoteReminder.setReminders(currentNoteReminder.getReminders()-1);
        if (! currentNoteReminder.update()) {
            Log.d("REMINDER ACTIVITY"," can not to update note with id = " + noteLocalIdReminder + " reminders count");
            localDbStorageReminder.close();
            return false;
        }

        localDbStorageReminder.close();

        return true;
    }



    private void initViews() {

        tvReminderTargetDate = (TextView) findViewById(R.id.textViewTargetDateValueReminderShow);
        tvReminderTargetDate.setText(formatDateTimeToString(currentReminder.getTargetDate()));
        tvNameReminder = (TextView) findViewById(R.id.textViewNameReminderShow);
        tvNameReminder.setText(currentReminder.getTitle());
        tvDetailsReminder = (TextView) findViewById(R.id.textViewDetailsReminderShow);
        tvDetailsReminder.setText(currentReminder.getDetails());
        tvReminderType = (TextView) findViewById(R.id.textViewTypeValueReminderShow);
        tvReminderType.setText("" + currentReminder.getType());

        swLigthReminder = (Switch) findViewById(R.id.switchLightShow);
        swLigthReminder.setChecked(currentReminder.getLight() != -1);
        swSoundReminder = (Switch) findViewById(R.id.switchSoundShow);
        swSoundReminder.setChecked(currentReminder.getSound() != -1);
        swVibrationReminder = (Switch) findViewById(R.id.switchVibrationShow);
        swVibrationReminder.setChecked(currentReminder.getVibrate() != -1);

        btnChangeReminder = (Button) findViewById(R.id.buttonChangeReminderShow);
        btnChangeReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to update reminder window
                Intent intent = new Intent(ReminderActivity.this, ReminderUpdateActivity.class);
                intent.putExtra("NOTE_ID", "" + noteLocalIdReminder);
                intent.putExtra("REMINDER_ID", "" + reminderLocalId);
                startActivityForResult(intent, REQUEST_CODE_FROM_UPDATE_REMINDER_ACTIVITY);
            }
        });

        btnRemoveReminder = (Button) findViewById(R.id.buttonRemoveReminderShow);
        btnRemoveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!removeReminderFromDatabase()) {
                    Log.d("REMINDER ACTIVITY", "reminder not deleted");
                    return;
                }
                Log.d("REMINDER ACTIVITY", "reminder with id = " + reminderLocalId + " was deleted");
                Intent intent = new Intent();
                intent.putExtra("NOTE_ID", "" + noteLocalIdReminder);
                intent.putExtra("REMINDER_ID", "" + reminderLocalId);
                setResult(RESULT_CODE_REMOVE_REMINDER,intent);
                finish();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reminders);

        Intent intent = getIntent();
        noteLocalIdReminder = Long.parseLong(intent.getStringExtra("NOTE_ID"));
        reminderLocalId = Long.parseLong(intent.getStringExtra("REMINDER_ID"));

        //initialize db
        localDbStorageReminder = new LocalDbStorage(this);

        // if reminder can not be loaded then no initialization
        if (!getReminderFromDatabase(reminderLocalId)) return;

        initViews();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // if (data == null) return;

        if (resultCode == RESULT_CANCELED) {
            Log.d("REMINDER_ACTIVITY ", "OTHER ACTIVITIES CLOSED without changes");
            codeReminder = RESULT_CODE_NOT_UPDATED_REMINDER;
            return;
        }
        if (requestCode == REQUEST_CODE_FROM_UPDATE_REMINDER_ACTIVITY) {
            if (resultCode == RESULT_CODE_UPDATE_REMINDER) {
                codeReminder = RESULT_CODE_UPDATE_REMINDER;
                noteLocalIdReminder = Long.parseLong(data.getStringExtra("NOTE_ID"));
                reminderLocalId = Long.parseLong(data.getStringExtra("REMINDER_ID"));
                // if reminder can not be loaded then no initialization
                if (!getReminderFromDatabase(reminderLocalId)) return;

                initViews();
                Log.d("REMINDER ACTIVITY ", "was updated reminder with NOTE_ID="+currentReminder.getNoteId()+" reminder ID="+currentReminder.getId());

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
