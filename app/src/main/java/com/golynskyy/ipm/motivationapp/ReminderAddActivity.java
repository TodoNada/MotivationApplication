package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.golynskyy.ipm.motivationapp.database.LocalDbStorage;
import com.golynskyy.ipm.motivationapp.dialogs.DialogDateAndTime;
import com.golynskyy.ipm.motivationapp.models.DatabaseStructure;
import com.golynskyy.ipm.motivationapp.models.Note;
import com.golynskyy.ipm.motivationapp.models.Notes;
import com.golynskyy.ipm.motivationapp.models.Reminder;

import java.util.Calendar;

import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_REMINDER;
import static com.golynskyy.ipm.motivationapp.util.Utils.formatDateTimeToString;

/**
 * Created by Dep5 on 29.07.2017.
 */

public class ReminderAddActivity extends Activity implements DialogDateAndTime.DateTimeSettedDialogListener{

    private long noteLocalIdAddReminder = -1;
    private Reminder currentReminderAdd;
    private Note currentNoteAddReminder;
    private LocalDbStorage localDbStorageReminderAdd;

    private long timeStampTargetDateTime;

    private Button btnAddNewReminderAdd;
    private Button btnSetReminderTargetDateTimeAdd;

    private EditText etNameReminderAdd;
    private EditText etDetailsReminderAdd;
    private TextView tvReminderDateAdd;

    private Spinner spReminderTypeAdd;

    private Switch swSoundReminderAdd;
    private Switch swLigthReminderAdd;
    private Switch swVibrationReminderAdd;

    protected DialogFragment dialogFragmentSetTargetDateTime;

    private boolean getNoteFromDatabase(long id) {
        localDbStorageReminderAdd.reopen();
        Notes notes = new Notes(localDbStorageReminderAdd.getDb());
        boolean noteSelected = notes.loadFromDb(DatabaseStructure.columns.note.id+" = ?",new String[] {""+id},0);
        currentNoteAddReminder = (Note)notes.get(0);
        localDbStorageReminderAdd.close();
        return noteSelected;
    }


    private boolean putReminderIntoDatabase(long noteId) {
        //TODO: realize checking Reminder info validity

        //load note from id
        getNoteFromDatabase(noteLocalIdAddReminder);

        //increase reminders count and try to update current reminder note
        localDbStorageReminderAdd.reopen();
        currentNoteAddReminder.setDb(localDbStorageReminderAdd.getDb());
        currentNoteAddReminder.setReminders(currentNoteAddReminder.getReminders()+1);
            if (! currentNoteAddReminder.update()) {
                Log.d("REMINDER_ADD ACTIVITY"," can not to update note with id = " + noteLocalIdAddReminder + " reminders count");
                localDbStorageReminderAdd.close();
                return false;
            }
        //for testing - reminder's target date will be set with now date
        Calendar calendar = Calendar.getInstance();
        long nowTime = calendar.getTimeInMillis();
        //create new reminder and try to insert to DB
        currentReminderAdd = new Reminder(localDbStorageReminderAdd.getDb());
        currentReminderAdd.setType(spReminderTypeAdd.getSelectedItemPosition());
        currentReminderAdd.setTitle(etNameReminderAdd.getText().toString());
        currentReminderAdd.setDetails(etDetailsReminderAdd.getText().toString());
        //gap will be used later, I know It :)
        currentReminderAdd.setGap(100);
        currentReminderAdd.setTargetDate(timeStampTargetDateTime);
        currentReminderAdd.setSound((swSoundReminderAdd.isChecked() == false)? -1 : 0);
        currentReminderAdd.setLight((swLigthReminderAdd.isChecked() == false)? -1 : 0);
        currentReminderAdd.setVibrate((swVibrationReminderAdd.isChecked() == false)? -1 : 0);
        //important! set note_id for reminder
        currentReminderAdd.setNoteId(noteLocalIdAddReminder);
        //try to insert new reminder into db
        boolean reminderAdded = currentReminderAdd.insert();

        localDbStorageReminderAdd.close();

        return reminderAdded;
    }


    private void initViews() {

        tvReminderDateAdd = (TextView)findViewById(R.id.textViewTargetDateValueReminderAdd);
        Calendar calendar = Calendar.getInstance();
        tvReminderDateAdd.setText(formatDateTimeToString(calendar.getTimeInMillis()));
        etNameReminderAdd = (EditText)findViewById(R.id.editTextNameReminderAdd);
        etDetailsReminderAdd = (EditText)findViewById(R.id.editTextDetailsReminderAdd);

        spReminderTypeAdd  = (Spinner)findViewById(R.id.spinnerTypeOfReminderAdd);

        swLigthReminderAdd = (Switch)findViewById(R.id.switchLightAdd);
        swSoundReminderAdd = (Switch)findViewById(R.id.switchSoundAdd);
        swVibrationReminderAdd = (Switch)findViewById(R.id.switchVibrationAdd);

        btnAddNewReminderAdd = (Button) findViewById(R.id.buttonAddReminderAdd);
        btnAddNewReminderAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put new reminder into DB
                if (! putReminderIntoDatabase(noteLocalIdAddReminder))
                {
                    Log.d("REMINDER_ADD ACTIVITY", "reminder was not added to DB");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("NOTE_ID",""+noteLocalIdAddReminder);
                Log.d("REMINDERADD ACTIVITY","reminder added to note id = "+noteLocalIdAddReminder+" with type = "+currentReminderAdd.getType());
                setResult(RESULT_CODE_NEW_REMINDER, intent);
                finish();
            }
        });

        btnSetReminderTargetDateTimeAdd = (Button)findViewById(R.id.buttonSetDateTimeTargetReminderAdd);
        btnSetReminderTargetDateTimeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragmentSetTargetDateTime.show(getFragmentManager(),"dialogFragmentSetTargetDateTime");
            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_reminder);


        Intent intent = getIntent();
        Log.d("ADD_REMINDER ", "Intent IS HERE");
        noteLocalIdAddReminder = Long.parseLong(intent.getStringExtra("NOTE_ID"));

        //initialize db
        localDbStorageReminderAdd = new LocalDbStorage(this);

        initViews();

        //create dialog
        dialogFragmentSetTargetDateTime = new DialogDateAndTime();

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

    @Override
    public void onFinishDateTimeDialogDialog(long timeStamp, String tag) {
        if (tag.equalsIgnoreCase("dialogFragmentSetTargetDateTime")) {
            timeStampTargetDateTime = timeStamp;
            tvReminderDateAdd.setText(formatDateTimeToString(timeStamp));
            return;
        }
    }
}
