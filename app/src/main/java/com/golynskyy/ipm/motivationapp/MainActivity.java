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

import com.golynskyy.ipm.motivationapp.models.Note;
import com.golynskyy.ipm.motivationapp.adapters.NotesAdapter;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.R.attr.name;
import static com.golynskyy.ipm.motivationapp.R.id.tvName;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_ADD_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.REQUEST_CODE_FROM_NOTE_ACTIVITY;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_DELETE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_NEW_NOTE;
import static com.golynskyy.ipm.motivationapp.models.Codes.RESULT_CODE_UPDATE_NOTE;
import static com.golynskyy.ipm.motivationapp.models.DatabaseStructure.columns.reminders.noteId;

public class MainActivity extends Activity {

    private ListView lvNotes;
    private Button btnAddNewNote;

    private NotesAdapter notesAdapter;
    private long noteLocalId = -1;

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

        lvNotes.setAdapter(notesAdapter);

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


        Note note1 = new Note();
        Note note2 = new Note();
        note1.setId(120);
        note2.setId(115);
        note1.setName("name1");
        note2.setName("name2");
        note1.setDescription("descr1");
        note2.setDescription("descr2");
        note1.setStatus(80);
        note2.setStatus(40);
        note1.setBeginDate(100);
        note2.setBeginDate(200);
        note1.setLastModified(150);
        note1.setEndDate(200);
        note2.setLastModified(250);
        note2.setEndDate(500);

        ArrayList<Note> al = new ArrayList<Note>();
        al.add(note1);
        al.add(note2);
        notesAdapter = new NotesAdapter(this,al);

         initViews();

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
