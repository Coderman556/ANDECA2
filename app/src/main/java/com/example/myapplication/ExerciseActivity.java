package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private TextView workoutNameTextView;
    private TextView totalExerciseTextView;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        recyclerView = findViewById(R.id.exerciseRecyclerView);
        workoutNameTextView = findViewById(R.id.workoutName);
        totalExerciseTextView = findViewById(R.id.totalExercise);
        startButton = findViewById(R.id.startButton);

        // Set OnClickListener for the "Start" button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWorkout();
            }
        });

        // Retrieve the selected workout array
        String[] selectedWorkoutArray = getIntent().getStringArrayExtra("selectedWorkoutArray");

        // Display the selected workout array in the RecyclerView
        if (selectedWorkoutArray != null) {
            List<String> workoutList = Arrays.asList(selectedWorkoutArray);
            adapter = new ExerciseAdapter(workoutList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            // Update workout name and total exercises count
            String workoutName = getWorkoutNameFromIntent();
            updateWorkoutInfo(workoutName, workoutList.size());
        }
    }

    private String getWorkoutNameFromIntent() {
        return getIntent().getStringExtra("workoutName");
    }

    private void updateWorkoutInfo(String workoutName, int totalExercises) {
        workoutNameTextView.setText(workoutName);
        totalExerciseTextView.setText("Total Exercises: " + totalExercises);
    }

    private void startWorkout() {
        String[] selectedWorkoutArray = getIntent().getStringArrayExtra("selectedWorkoutArray");

        // Retrieve the workout name
        String workoutName = getWorkoutNameFromIntent();

        // Check if the workout array and name are not null
        if (selectedWorkoutArray != null && workoutName != null) {
            startWorkoutActivity(selectedWorkoutArray, workoutName);
        } else {
            Toast.makeText(this, "Unable to start workout. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startWorkoutActivity(String[] selectedWorkoutArray, String workoutName) {
        Intent intent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
        intent.putExtra("selectedWorkoutArray", selectedWorkoutArray);
        intent.putExtra("workoutName", workoutName); // Pass the workout name
        startActivity(intent);
    }

}
