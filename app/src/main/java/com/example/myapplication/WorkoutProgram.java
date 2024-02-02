package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WorkoutProgram extends AppCompatActivity {

    private String[] squatWorkouts;
    private String[] chestWorkouts;
    private String[] tricepWorkouts;
    private String[] absWorkouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_program);

        squatWorkouts = new String[]{"Back Squat", "Front Squat", "Sumo Squat", "Split Squat"};
        chestWorkouts = new String[]{"Bench Press", "Incline Dumbbell Press", "Chest Fly", "Push-ups"};
        tricepWorkouts = new String[]{"Tricep Dips", "Skull Crushers", "Tricep Kickbacks", "Close Grip Bench Press"};
        absWorkouts = new String[]{"Plank", "Russian Twists", "Leg Raises", "Mountain Climbers"};

        // Set onClickListener for each workout
        findViewById(R.id.squatWorkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startExerciseActivity(squatWorkouts, "Squat Workout");
            }
        });

        findViewById(R.id.chestWorkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startExerciseActivity(chestWorkouts, "Chest Workout");
            }
        });

        findViewById(R.id.tricepWorkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startExerciseActivity(tricepWorkouts, "Tricep Workout");
            }
        });

        findViewById(R.id.absWorkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startExerciseActivity(absWorkouts, "Abs Workout");
            }
        });
    }

    private void startExerciseActivity(String[] selectedWorkoutArray, String workoutName) {
        Intent intent = new Intent(WorkoutProgram.this, ExerciseActivity.class);
        intent.putExtra("selectedWorkoutArray", selectedWorkoutArray);
        intent.putExtra("workoutName", workoutName);
        startActivity(intent);
    }
}
