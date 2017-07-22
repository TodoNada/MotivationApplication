package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_UPDATE_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_NOTE;

/**
 * Created by Dep5 on 28.07.2017.
 */

public class NoteAddActivity extends Activity {

    private Button btnAddNewTask;
    private Button btnSetTaskBeginDate;
    private Button btnSetTaskEndDate;

    private EditText etName;
    private EditText etDetails;
    private TextView tvTaskBeginDate;
    private TextView tvTaskEndDate;

    private Spinner spinnerImportancy;
    private SeekBar seekBarTaskProgress;


    private void initViews() {

      tvTaskBeginDate = (TextView)findViewById(R.id.textViewBeginDateAdd);
      tvTaskEndDate = (TextView)findViewById(R.id.textViewEndDateAdd);
      etName = (EditText)findViewById(R.id.editTextNameNoteAdd);
      etDetails = (EditText)findViewById(R.id.editTextDescriptionNoteAdd);

      spinnerImportancy  = (Spinner)findViewById(R.id.spinnerImportance);
      //TODO: add data selector ot spinner

      seekBarTaskProgress = (SeekBar)findViewById(R.id.seekBarTaskProgressInitial);

      btnAddNewTask = (Button) findViewById(R.id.buttonAddNewNote);
      btnAddNewTask.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //TODO: realize checking Note info validity
              //TODO: realize inserting new note into DB
              Intent intent = new Intent();
              setResult(RESULT_CODE_NEW_NOTE, intent);
              finish();
          }
      });

      btnSetTaskBeginDate = (Button)findViewById(R.id.buttonSetBeginTime);
      btnSetTaskBeginDate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //TODO: realize DateTime Picker Dialog
          }
      });

      btnSetTaskEndDate = (Button)findViewById(R.id.buttonSetEndTime);
      btnSetTaskEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: realize DateTime Picker Dialog
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_note);
        Intent intent = getIntent();
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
