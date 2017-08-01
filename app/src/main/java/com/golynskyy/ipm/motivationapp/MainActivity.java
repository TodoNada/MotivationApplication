package com.golynskyy.ipm.motivationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.golynskyy.ipm.motivationapp.database.LocalDbStorage;
import com.golynskyy.ipm.motivationapp.models.Note;
import com.golynskyy.ipm.motivationapp.models.Notes;
import com.golynskyy.ipm.motivationapp.adapters.NotesAdapter;

import java.util.Calendar;

import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_ADD_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_DELETE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_NOTE;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity {

    private ListView lvNotes;
    private Button btnAddNewNote;

    private NotesAdapter notesAdapter;
    private Notes notes = new Notes();
    private long noteLocalId = -1;
    private LocalDbStorage localDbStorage;

    // for testing delete later
    private void testAddingToDb() {
        localDbStorage.reopen();
        SQLiteDatabase sqLiteDatabase = localDbStorage.getDb();
        notes.setDb(sqLiteDatabase);
        for (int i = 1; i < 9; i++ ) {
            Note note = new Note(sqLiteDatabase);
            note.setId(i);
            note.setName(" Note "+i);
            note.setDescription(" Description "+i);
            note.setType(i % 3);
            note.setStatus(i * 10);
            note.setReminders(0);
            note.setAlarmIndex((i % 3) + 1);
            note.setTags("Tag "+ i);

            Calendar calendar = Calendar.getInstance();
            long nowTime = calendar.getTimeInMillis();
            note.setDateCreated(nowTime);
            note.setBeginDate(nowTime+i*100+i);
            note.setEndDate(nowTime + i*1000000);
            note.setLastModified(nowTime);
            note.insert();
            notes.add(note);
            }

        localDbStorage.close();
    }

    // for testing
    private void testGetNotesFromDb() {
        localDbStorage.reopen();
        SQLiteDatabase sqLiteDatabase = localDbStorage.getDb();
        notes.setDb(sqLiteDatabase);
        notes.loadFromDb("",new String[] {},0);
        localDbStorage.close();
    }

    private void initViews() {
        lvNotes = (ListView) findViewById(R.id.listViewNotes);
        btnAddNewNote = (Button) findViewById(R.id.buttonAddNewNote);
        btnAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this/*getApplication().getApplicationContext()*/, NoteAddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FROM_ADD_NOTE_ACTIVITY);
            }
        });



        lvNotes.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Note note = (Note)adapter.getItemAtPosition(position);
                noteLocalId = note.getId();
                Log.d("MAIN ", "Noteid= "+noteLocalId);
                Intent intent = new Intent(MainActivity.this/*getApplication().getApplicationContext()*/, NoteActivity.class);
                intent.putExtra("NOTE_ID",""+noteLocalId);
                startActivityForResult(intent, REQUEST_CODE_FROM_NOTE_ACTIVITY);

            }
        });
    }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Log.d("MAIN" , " STARTED");


        localDbStorage = new LocalDbStorage(this);

       // testAddingToDb();
           testGetNotesFromDb();

        notesAdapter = new NotesAdapter(this,notes);

         initViews();

        lvNotes.setAdapter(notesAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
     //   if (resultCode == RESULT_CANCELED) return;
        if (requestCode == REQUEST_CODE_FROM_NOTE_ACTIVITY) {
                if (resultCode == RESULT_CODE_UPDATE_NOTE) {
                    String stringId = data.getStringExtra("NOTE_ID");
                    noteLocalId = Long.parseLong(stringId);
                    //TODO: update adapter with updated note
                    Log.d("MAIN", "UPDATED NOTE_ID = "+noteLocalId);
                    return;
                }
                 if (resultCode == RESULT_CODE_DELETE_NOTE) {
                    String stringId = data.getStringExtra("NOTE_ID");
                    noteLocalId = Long.parseLong(stringId);
                    //TODO: update adapter after deleted note
                     notesAdapter.notifyDataSetChanged();
                    Log.d("MAIN", "Deleted NOTE_ID = "+noteLocalId);
                    return;
                }
                Log.d("MAIN ","NOTE ACTIVITY CLOSED without changes");
                return;

        }
        if (requestCode == REQUEST_CODE_FROM_ADD_NOTE_ACTIVITY) {
            if (resultCode != RESULT_CODE_NEW_NOTE) {
                Log.d("MAIN ","NOTE ADD ACTIVITY CLOSED without changes");
                return;
            }
            //TODO: update adapter with new note
            Log.d("MAIN ","CREATED NEW NOTE");
            return;
        }

    }

}
