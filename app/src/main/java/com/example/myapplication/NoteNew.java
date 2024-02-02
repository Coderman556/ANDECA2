package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.Date;

public class NoteNew extends AppCompatActivity {

    private EditText titleEditText, descEditText;
    private Button deleteButton;
    private ToggleButton favoriteToggleButton;  // Add ToggleButton reference
    private NotesNote selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        initWidgets();
        checkForEditNote();
        BottomNavigationHelper.setupBottomNavigation(this, R.id.notes);
    }

    private void initWidgets() {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        deleteButton = findViewById(R.id.deleteNoteButton);
        favoriteToggleButton = findViewById(R.id.favoriteToggleButton);  // Initialize ToggleButton
    }

    private void checkForEditNote() {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(NotesNote.NOTE_EDIT_EXTRA, -1);
        selectedNote = NotesNote.getNoteForID(passedNoteID);

        if (selectedNote != null) {
            titleEditText.setText(selectedNote.getTitle());
            descEditText.setText(selectedNote.getCont());
            favoriteToggleButton.setChecked(selectedNote.getIsFavourite());  // Set ToggleButton state
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveNote(View view) {
        DBHandler dBhandler = DBHandler.instanceOfDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String cont = String.valueOf(descEditText.getText());
        boolean isFavorite = favoriteToggleButton.isChecked();

        if(selectedNote == null) {
            int id = NotesNote.noteArrayList.size();
            NotesNote newNote = new NotesNote(id, title, cont);
            newNote.setIsFavourite(isFavorite);
            NotesNote.noteArrayList.add(newNote);
            dBhandler.addNoteToDatabase(newNote);
        } else {
            selectedNote.setTitle(title);
            selectedNote.setCont(cont);
            selectedNote.setIsFavourite(isFavorite);
            dBhandler.updateNoteInDB(selectedNote);
        }

        finish();
    }

    public void deleteNote(View view) {
        selectedNote.setDeleted(new Date());
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.deleteNoteFromDB(selectedNote.getId());
        finish();
    }

}
