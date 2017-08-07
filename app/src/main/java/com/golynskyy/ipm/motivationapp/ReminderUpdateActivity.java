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

import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_REMINDER;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_REMINDER;
import static com.golynskyy.ipm.motivationapp.util.Utils.formatDateTimeToString;

/**
 * Created by Dep5 on 29.07.2017.
 */

public class ReminderUpdateActivity extends Activity {

    private long noteLocalIdUpdateReminder = -1;
    private long reminderLocalIdUpdate = -1;

    private Reminder currentReminderUpdate;
    private Note currentNoteUpdateReminder;
    private LocalDbStorage localDbStorageReminderUpdate;


    private Button btnUpdateReminderUpdate;
    private Button btnSetReminderDateUpdate;
    private Button btnSetReminderTimeUpdate;

    private EditText etNameReminderUpdate;
    private EditText etDetailsReminderUpdate;
    private TextView tvReminderTargetDateUpdate;

    private Spinner spReminderTypeUpdate;

    private Switch swSoundReminderUpdate;
    private Switch swLightReminderUpdate;
    private Switch swVibrationReminderUpdate;


    private void initViews() {

        tvReminderTargetDateUpdate = (TextView)findViewById(R.id.textViewTargetDateValueReminderUpdate);
        tvReminderTargetDateUpdate.setText(formatDateTimeToString(currentReminderUpdate.getTargetDate()));
        etNameReminderUpdate = (EditText)findViewById(R.id.editTextNameReminderUpdate);
        etNameReminderUpdate.setText(currentReminderUpdate.getTitle());
        etDetailsReminderUpdate = (EditText)findViewById(R.id.editTextDetailsReminderUpdate);
        etDetailsReminderUpdate.setText(currentReminderUpdate.getDetails());

        spReminderTypeUpdate  = (Spinner)findViewById(R.id.spinnerTypeOfReminderUpdate);
        spReminderTypeUpdate.setSelection(currentReminderUpdate.getType());

        swLightReminderUpdate = (Switch)findViewById(R.id.switchLightUpdate);
        swLightReminderUpdate.setChecked(currentReminderUpdate.getLight() != -1);
        swSoundReminderUpdate = (Switch)findViewById(R.id.switchSoundUpdate);
        swSoundReminderUpdate.setChecked(currentReminderUpdate.getSound() != -1);
        swVibrationReminderUpdate = (Switch)findViewById(R.id.switchVibrationUpdate);
        swVibrationReminderUpdate.setChecked(currentReminderUpdate.getVibrate() != -1);

        btnUpdateReminderUpdate = (Button) findViewById(R.id.buttonUpdateReminderUpdate);
        btnUpdateReminderUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put new reminder into DB
                if (!updateReminderInDatabase(noteLocalIdUpdateReminder))
                {
                    Log.d("REMINDER_UPD ACTIVITY", "reminder was not updated in DB");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("NOTE_ID",""+noteLocalIdUpdateReminder);
                intent.putExtra("REMINDER_ID",""+reminderLocalIdUpdate);
                Log.d("REMINDER_UPD ACTIVITY","reminder updated in DB with id = "+reminderLocalIdUpdate);
                setResult(RESULT_CODE_UPDATE_REMINDER, intent);
                finish();
            }
        });

        btnSetReminderDateUpdate = (Button)findViewById(R.id.buttonSetDateReminderUpdate);
        btnSetReminderDateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: realize Date Picker Dialog
            }
        });

        btnSetReminderTimeUpdate = (Button)findViewById(R.id.buttonSetTimeReminderUpdate);
        btnSetReminderTimeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: realize Time Picker Dialog
            }
        });

    }


    private boolean getNoteFromDatabase(long id) {
        localDbStorageReminderUpdate.reopen();
        Notes notes = new Notes(localDbStorageReminderUpdate.getDb());
        boolean noteSelected = notes.loadFromDb(DatabaseStructure.columns.note.id+" = ?",new String[] {""+id},0);
        currentNoteUpdateReminder = (Note)notes.get(0);
        localDbStorageReminderUpdate.close();
        return noteSelected;
    }

    private boolean getReminderFromDatabase(long id) {
        localDbStorageReminderUpdate.reopen();
        Reminders reminders = new Reminders(localDbStorageReminderUpdate.getDb());
        boolean reminderSelected = reminders.loadFromDb(DatabaseStructure.columns.reminders.id+" = ?",new String[] {""+id},0);
        currentReminderUpdate = (Reminder)reminders.get(0);
        localDbStorageReminderUpdate.close();
        return reminderSelected;
    }

    private boolean updateReminderInDatabase(long noteId) {
        //TODO: realize checking Reminder info validity

        //for testing - reminder's target date will be set with now date
        Calendar calendar = Calendar.getInstance();
        long nowTime = calendar.getTimeInMillis();
        //create new reminder and try to insert to DB
        currentReminderUpdate.setType(spReminderTypeUpdate.getSelectedItemPosition());
        currentReminderUpdate.setTitle(etNameReminderUpdate.getText().toString());
        currentReminderUpdate.setDetails(etDetailsReminderUpdate.getText().toString());
        //gap will be used later, I know It :)
        currentReminderUpdate.setGap(100);
        currentReminderUpdate.setTargetDate(nowTime+100000000);
        currentReminderUpdate.setSound((swSoundReminderUpdate.isChecked() == false)? -1 : 0);
        currentReminderUpdate.setLight((swLightReminderUpdate.isChecked() == false)? -1 : 0);
        currentReminderUpdate.setVibrate((swVibrationReminderUpdate.isChecked() == false)? -1 : 0);
        //important! set note_id for reminder
        currentReminderUpdate.setNoteId(noteId);
        //try to update reminder in db

        Log.d("Type = ",""+currentReminderUpdate.getType());

        localDbStorageReminderUpdate.reopen();
        currentReminderUpdate.setDb(localDbStorageReminderUpdate.getDb());
        boolean reminderUpdated = currentReminderUpdate.update();
        localDbStorageReminderUpdate.close();

        return reminderUpdated;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_reminder);

        Intent intent = getIntent();
        noteLocalIdUpdateReminder = Long.parseLong(intent.getStringExtra("NOTE_ID"));
        reminderLocalIdUpdate = Long.parseLong(intent.getStringExtra("REMINDER_ID"));
        Log.d("REMINDER_UPD ACTIVITY ", "Intent IS HERE NOTE_ID="+noteLocalIdUpdateReminder+" reminder ID="+reminderLocalIdUpdate);

        //initialize db
        localDbStorageReminderUpdate = new LocalDbStorage(this);

        // if reminder can not be loaded then no initialization
        if (!getReminderFromDatabase(reminderLocalIdUpdate)) return;

        initViews();


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
