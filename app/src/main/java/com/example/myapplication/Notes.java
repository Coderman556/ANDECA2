package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;

public class Notes extends AppCompatActivity
{
    private ListView noteListView;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        noteListView = findViewById(R.id.noteListView);
        calendarView = findViewById(R.id.calendarView);

        loadFromDBToMemory();

        setNoteAdapter();
        setOnClickListener();
        setCalendarClickListener();

        BottomNavigationHelper.setupBottomNavigation(this, R.id.notes);

    }


    private void initWidgets()
    {
        noteListView = findViewById(R.id.noteListView);
    }

    private void loadFromDBToMemory() {
        DBHandler dbHandler = DBHandler.instanceOfDatabase(this);
        dbHandler.populateNoteListArray();
    }

    private void setNoteAdapter() {
        NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(), NotesNote.nonDeletedNotes());
        noteListView.setAdapter(noteAdapter);
    }


    private void setOnClickListener()
    {
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                NotesNote selectedNote = (NotesNote) noteListView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), NoteNew.class);
                editNoteIntent.putExtra(NotesNote.NOTE_EDIT_EXTRA, selectedNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }

    private void setCalendarClickListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                NotesTask task = getTaskOnDate(year, month, day);

                if (task != null) {
                    // If a task exists, go to the new task activity with task details
                    Intent newTaskIntent = new Intent(getApplicationContext(), NotesNewTask.class);
                    newTaskIntent.putExtra("YEAR", year);
                    newTaskIntent.putExtra("MONTH", month);
                    newTaskIntent.putExtra("DAY", day);
                    newTaskIntent.putExtra("TASK_ID", task.getId()); // Pass task ID to retrieve details
                    startActivity(newTaskIntent);
                } else {
                    // If no task exists, go to the new task activity
                    Intent newTaskIntent = new Intent(getApplicationContext(), NotesNewTask.class);
                    newTaskIntent.putExtra("YEAR", year);
                    newTaskIntent.putExtra("MONTH", month);
                    newTaskIntent.putExtra("DAY", day);
                    startActivity(newTaskIntent);
                }
            }
        });
    }

    // Check if there is a task on the specified date
    private NotesTask getTaskOnDate(int year, int month, int day) {
        for (NotesTask task : NotesTask.taskArrayList) {
            Date taskDate = task.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(taskDate);

            int taskYear = calendar.get(Calendar.YEAR);
            int taskMonth = calendar.get(Calendar.MONTH);
            int taskDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (taskYear == year && taskMonth == month && taskDay == day) {
                return task;
            }
        }
        return null;
    }

    public void newNote(View view)
    {
        Intent newNoteIntent = new Intent(this, NoteNew.class);
        startActivity(newNoteIntent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setNoteAdapter();
    }

}