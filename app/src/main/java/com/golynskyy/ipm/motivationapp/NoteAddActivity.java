package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.golynskyy.ipm.motivationapp.database.LocalDbStorage;
import com.golynskyy.ipm.motivationapp.dialogs.DialogDateAndTime;
import com.golynskyy.ipm.motivationapp.models.Note;
import com.golynskyy.ipm.motivationapp.models.Notes;

import java.util.Calendar;

import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_UPDATE_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_NOTE;
import static com.golynskyy.ipm.motivationapp.util.Utils.formatDateTimeToString;
import static com.golynskyy.ipm.motivationapp.util.Utils.formatNowDateTimeToString;

/**
 * Created by Dep5 on 28.07.2017.
 */

public class NoteAddActivity extends Activity implements DialogDateAndTime.DateTimeSettedDialogListener {

    private long noteLocalIdAdd = -1;
    private Note currentNoteAdd;
    private LocalDbStorage localDbStorageNoteAdd;
    private long timeStampBeginDate;
    private long timeStampEndDate;



    private Button btnAddNewTaskAdd;
    private Button btnSetTaskBeginDateAdd;
    private Button btnSetTaskEndDateAdd;

    private EditText etNameAdd;
    private EditText etDetailsAdd;
    private TextView tvTaskBeginDateAdd;
    private TextView tvTaskEndDateAdd;

    private Spinner spinnerImportancyAdd;
    private SeekBar seekBarTaskProgressAdd;


    protected DialogFragment dialogFragmentSetStartDate;
    protected DialogFragment dialogFragmentSetEndDate;


    private boolean putNoteIntoDatabase(long id) {
        //TODO: realize checking Note info validity
        localDbStorageNoteAdd.reopen();
        currentNoteAdd = new Note(localDbStorageNoteAdd.getDb());
        Calendar calendar = Calendar.getInstance();
        long nowTime = calendar.getTimeInMillis();
        currentNoteAdd.setDateCreated(nowTime);
        currentNoteAdd.setLastModified(nowTime);
        currentNoteAdd.setBeginDate(timeStampBeginDate);
        currentNoteAdd.setEndDate(timeStampEndDate);
        //other note fields
        currentNoteAdd.setName(etNameAdd.getText().toString());
        currentNoteAdd.setDescription(etDetailsAdd.getText().toString());
        currentNoteAdd.setStatus(seekBarTaskProgressAdd.getProgress());
        currentNoteAdd.setReminders(0);
        currentNoteAdd.setTags("tag");
        currentNoteAdd.setAlarmIndex(0);
        currentNoteAdd.setType(spinnerImportancyAdd.getSelectedItemPosition());
        boolean noteAdded = currentNoteAdd.insert();
        localDbStorageNoteAdd.close();
            if (noteAdded) noteLocalIdAdd = currentNoteAdd.getId();
        return noteAdded;
    }



    private void initViews() {

      tvTaskBeginDateAdd = (TextView)findViewById(R.id.textViewBeginDateAdd);
        tvTaskBeginDateAdd.setText(formatNowDateTimeToString());
      tvTaskEndDateAdd = (TextView)findViewById(R.id.textViewEndDateAdd);
        tvTaskEndDateAdd.setText(formatNowDateTimeToString());
      etNameAdd = (EditText)findViewById(R.id.editTextNameNoteAdd);
      etDetailsAdd = (EditText)findViewById(R.id.editTextDescriptionNoteAdd);

      spinnerImportancyAdd  = (Spinner)findViewById(R.id.spinnerImportance);

      seekBarTaskProgressAdd = (SeekBar)findViewById(R.id.seekBarTaskProgressInitialNoteAdd);

      btnAddNewTaskAdd = (Button) findViewById(R.id.buttonNoteAddTask);
      btnAddNewTaskAdd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //put new note into DB
              if (! putNoteIntoDatabase(0)) Log.d("NOTEADD ACTIVITY", "note was not added to DB");
              Intent intent = new Intent();
              intent.putExtra("NOTE_ID",""+noteLocalIdAdd);
              Log.d("NOTEADD ACTIVITY","note added id = "+noteLocalIdAdd);
              setResult(RESULT_CODE_NEW_NOTE, intent);
              finish();
          }
      });

      btnSetTaskBeginDateAdd = (Button)findViewById(R.id.buttonSetBeginTimeAdd);
      btnSetTaskBeginDateAdd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialogFragmentSetStartDate.show(getFragmentManager(),"dialogFragmentSetStartDate");
          }
      });

      btnSetTaskEndDateAdd = (Button)findViewById(R.id.buttonSetEndTimeAdd);
      btnSetTaskEndDateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dialogFragmentSetEndDate.show(getFragmentManager(),"dialogFragmentSetEndDate");
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_note);

        Intent intent = getIntent();
        Log.d("NOTE ADD", "Intent IS HERE");

        //initialize db
        localDbStorageNoteAdd = new LocalDbStorage(this);

        //initialize views
        initViews();

        //create dialogs
        dialogFragmentSetStartDate = new DialogDateAndTime();
        dialogFragmentSetEndDate = new DialogDateAndTime();

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

    @Override
    public void onFinishDateTimeDialogDialog(long timeStamp, String tag) {
        if (tag.equalsIgnoreCase("dialogFragmentSetStartDate")) {
           timeStampBeginDate = timeStamp;
            tvTaskBeginDateAdd.setText(formatDateTimeToString(timeStamp));
            return;
        }
        if (tag.equalsIgnoreCase("dialogFragmentSetEndDate")) {
            timeStampBeginDate = timeStamp;
            tvTaskEndDateAdd.setText(formatDateTimeToString(timeStamp));
            return;
        }
    }
}
