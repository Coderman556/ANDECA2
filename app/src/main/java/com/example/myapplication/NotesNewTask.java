package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.Date;
import java.util.List;

public class NotesNewTask extends AppCompatActivity {

    private EditText descEditText;
    private NotesTask selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        initWidgets();
        checkForEditTask();
        BottomNavigationHelper.setupBottomNavigation(this, R.id.notes);

    }

    private void initWidgets() {
        descEditText = findViewById(R.id.descriptionEditText);
    }

    private void checkForEditTask() {
        Intent previousIntent = getIntent();

        int passedTaskID = previousIntent.getIntExtra("TASK_ID", -1);
        selectedTask = NotesTask.getTaskForID(passedTaskID);

        if (selectedTask != null) {
            // Populate the UI with existing task details for editing
            descEditText.setText(selectedTask.getCont());
        }
    }



    public void saveTask(View view) {
        DBHandler dBhandler = DBHandler.instanceOfDatabase(this);
        String cont = String.valueOf(descEditText.getText());

        if (selectedTask == null) {
            // Creating a new task with the current date
            int id = NotesTask.taskArrayList.size();
            Date date = new Date();
            NotesTask newTask = new NotesTask(id, cont, date);
            NotesTask.taskArrayList.add(newTask);
            dBhandler.addTaskToDatabase(newTask);
        } else {
            // Editing an existing task
            selectedTask.setCont(cont);
            dBhandler.updateTaskInDB(selectedTask);
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTaskListArray();
    }


    private void populateTaskListArray() {
        NotesTask.taskArrayList.clear();
        DBHandler dBhandler = DBHandler.instanceOfDatabase(this);
        List<NotesTask> tasks = dBhandler.populateTaskListArray();
        NotesTask.taskArrayList.addAll(tasks);
    }
}

