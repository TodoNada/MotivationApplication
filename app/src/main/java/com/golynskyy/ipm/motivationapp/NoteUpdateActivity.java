package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.golynskyy.ipm.motivationapp.database.LocalDbStorage;
import com.golynskyy.ipm.motivationapp.models.DatabaseStructure;
import com.golynskyy.ipm.motivationapp.models.Note;
import com.golynskyy.ipm.motivationapp.models.Notes;

import java.util.Calendar;

import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_DELETE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_NOTE;
import static com.golynskyy.ipm.motivationapp.util.Utils.formatDateTimeToString;

/**
 * Created by Dep5 on 28.07.2017.
 */

public class NoteUpdateActivity extends Activity {
    private long noteLocalIdUpdate = -1;
    private Note currentNoteUpdate;
    private LocalDbStorage localDbStorageNoteUpdate;

    private TextView tvNameUpdate;
    private TextView tvDescriptionUpdate;
    private TextView tvBeginDateUpdate;
    private TextView tvEndDateUpdate;
    private TextView tvRemindersCountUpdate;
    private TextView tvImportancyUpdate;

    private ProgressBar pbTimeProgressUpdate;
    private SeekBar sbTaskProgressUpdate;


    private Button btnUpdateProgressUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_progress_note);

        Intent intent = getIntent();
        Log.d("NOTE ", "Intent IS HERE");
        noteLocalIdUpdate = Long.parseLong(intent.getStringExtra("NOTE_ID"));

        //initialize db
        localDbStorageNoteUpdate = new LocalDbStorage(this);

        // if note can not be loaded then no initialization
        if (!getNoteFromDatabase(noteLocalIdUpdate)) return;

        //initialize views
        initViews();



    }


    private boolean getNoteFromDatabase(long id) {
        localDbStorageNoteUpdate.reopen();
        Notes notesUpdate = new Notes(localDbStorageNoteUpdate.getDb());
        boolean notesSelected = notesUpdate.loadFromDb(DatabaseStructure.columns.note.id+" = ?",new String[] {""+id},0);
        currentNoteUpdate = (Note)notesUpdate.get(0);
        localDbStorageNoteUpdate.close();
        return notesSelected;
    }

    private boolean updateNoteInDatabase() {
        localDbStorageNoteUpdate.reopen();
        currentNoteUpdate.setDb(localDbStorageNoteUpdate.getDb());
        boolean noteUpdated = currentNoteUpdate.update();
        localDbStorageNoteUpdate.close();
        return noteUpdated;
    }

    private void initViews() {
        tvNameUpdate = (TextView)findViewById(R.id.textViewNameUpdate);
            tvNameUpdate.setText(currentNoteUpdate.getName());
        tvDescriptionUpdate = (TextView)findViewById(R.id.textViewDescriptionUpdate);
            tvDescriptionUpdate.setText(currentNoteUpdate.getDescription());
        tvBeginDateUpdate = (TextView)findViewById(R.id.textViewBeginDateUpdate);
            tvBeginDateUpdate.setText(formatDateTimeToString(currentNoteUpdate.getBeginDate()));
        tvEndDateUpdate = (TextView)findViewById(R.id.textViewEndDateUpdate);
            tvEndDateUpdate.setText(formatDateTimeToString(currentNoteUpdate.getEndDate()));
        tvRemindersCountUpdate = (TextView)findViewById(R.id.textViewRemindersCountUpdate);
            tvRemindersCountUpdate.setText(""+currentNoteUpdate.getReminders());
        tvImportancyUpdate = (TextView)findViewById(R.id.textViewImportancyLevelUpdate);
            tvImportancyUpdate.setText(""+currentNoteUpdate.getType());
        pbTimeProgressUpdate = (ProgressBar)findViewById(R.id.progressBarTimeProgressUpdate);
            pbTimeProgressUpdate.setProgress(currentNoteUpdate.getStatus());
        sbTaskProgressUpdate = (SeekBar)findViewById(R.id.seekBarProgressOfTaskUpdate);
            sbTaskProgressUpdate.setProgress(currentNoteUpdate.getStatus());
        //TODO: realize checking if now time is not in range beginTime..endTime
        Calendar calendar = Calendar.getInstance();
        long nowTime = calendar.getTimeInMillis();
        pbTimeProgressUpdate.setProgress((int) (100*(nowTime - currentNoteUpdate.getBeginDate())/(currentNoteUpdate.getEndDate() - currentNoteUpdate.getBeginDate())));

        btnUpdateProgressUpdate = (Button) findViewById(R.id.buttonUpdateProgressUpdate);
        btnUpdateProgressUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set progress and last modified time of task in current note
                Calendar calendar = Calendar.getInstance();
                long nowTime = calendar.getTimeInMillis();
                currentNoteUpdate.setLastModified(nowTime);
                currentNoteUpdate.setStatus(sbTaskProgressUpdate.getProgress());
                if (!updateNoteInDatabase()) Log.d("ACTIVITY UPDATE NOTE", "note not updated");
                Intent intent = new Intent();
                intent.putExtra("NOTE_ID", "" + noteLocalIdUpdate);
                setResult(RESULT_CODE_UPDATE_NOTE,intent);
                finish();
            }
        });

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
