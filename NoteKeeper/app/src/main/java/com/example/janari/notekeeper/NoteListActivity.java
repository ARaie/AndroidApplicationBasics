package com.example.janari.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

//parem klik ja New -> Activity -> Gallery

public class NoteListActivity extends AppCompatActivity {

    private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //loome intenti nimega startActivity. Annab edasi this väärtuse noteActicity classile
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
            }
        });

        initializeDisplayContent();
    }

    @Override//anname arrayAdapterile teada et data on muutunud
    protected void onResume() {
        super.onResume();
        mAdapterNotes.notifyDataSetChanged();
    }


    // täita note list

    private void initializeDisplayContent() {
        //reference to listView. list_notes oli välja ID nimi
        final ListView listNotes = (ListView) findViewById(R.id.list_notes);

        //et saada sinna kontekst sisse DataManagerist ja just notsid
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        //kasutame jälle array adapterit
        mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        //et seostada adapter listViewga
        listNotes.setAdapter(mAdapterNotes);

        //kui kasutaja klikib ja valib midagi listViewst.
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // see on kokku surutud aga pm seostab view clikeriga

                //intent identifitseerib tegevuse millest me laustame. Activityd on tegelikult tüüp kontekstist ja teine on class
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);


                //nüüd kui keegi teeb valiku meie listist siis antakse position meile, pannakse ta intent extrasse ja saadetakse teise intenti et alustada uut activityt

                //et saata info ühest activityst teise intendiga
//                NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);<<seda pole enam vaja
                //selle NOTE jaoks pidi tegema NoteActivitisse tegema public static finali
                //ja kui nüüd kasutaja teeb valiku listist, see note on nüüd pakitud intenti ja saadetud üle noteActiviti
                //See oli enne NOTE_INFO. enne oli note aga nüüd sai position
                intent.putExtra(NoteActivity.NOTE_POSITION, position);
                //käivitab activity
                startActivity(intent);
                
            }
        });
    }

}
