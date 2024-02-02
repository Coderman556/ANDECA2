package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.WorkoutProgram;

import java.util.Random;

public class WorkoutActivity extends AppCompatActivity {

    private TextView workoutNameTextView;
    private TextView repTextView;
    private TextView exerciseNameTextView;
    private ProgressBar progressBar;
    private VideoView videoView;
    private Button doneButton;
    private Button previousButton;

    private String[] selectedWorkoutArray;
    private int currentExerciseIndex;
    private CountDownTimer countDownTimer;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workoutNameTextView = findViewById(R.id.nameWorkout);
        exerciseNameTextView = findViewById(R.id.exerciseName);
        repTextView = findViewById(R.id.repTxt);
        progressBar = findViewById(R.id.progressBar);
        videoView = findViewById(R.id.videoView);
        doneButton = findViewById(R.id.btnDone);
        previousButton = findViewById(R.id.btnPrevious);

        // Retrieve workout name and array from the intent
        String workoutName = getIntent().getStringExtra("workoutName");
        selectedWorkoutArray = getIntent().getStringArrayExtra("selectedWorkoutArray");

        // Set workout name
        workoutNameTextView.setText(workoutName);

        // Set random rep count between 8-12
        int randomRepCount = generateRandomRepCount();
        repTextView.setText("Reps: " + randomRepCount);

        showCurrentExercise();

        progressBar.setMax(100);
        startProgressBar();

        startVideo();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedWorkoutArray != null && currentExerciseIndex < selectedWorkoutArray.length - 1) {
                    currentExerciseIndex++;
                    showCurrentExercise();
                    resetProgressBar();
                    startProgressBar();
                } else {
                    Toast.makeText(WorkoutActivity.this, "End of Workout", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(WorkoutActivity.this, WorkoutProgram.class);
                    startActivity(i);
                    finish(); // Finish the current activity to prevent going back to it with the back button
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if it's not the first exercise
                if (currentExerciseIndex > 0) {
                    currentExerciseIndex--;
                    showCurrentExercise();
                } else {
                    Toast.makeText(WorkoutActivity.this, "Already at the first exercise", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int generateRandomRepCount() {
        // Generate a random number between 8 and 12
        Random random = new Random();
        return random.nextInt(5) + 8; // Generates random number from 8 to 12
    }

    private void startProgressBar() {
        // Set the total duration of the progress bar (20 seconds)
        final int totalDuration = 20000;

        countDownTimer = new CountDownTimer(totalDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Calculate the progress based on the remaining time
                int progress = (int) ((totalDuration - millisUntilFinished) * 100 / totalDuration);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                // Move to the next exercise or redirect to WorkoutProgram screen
                moveToNextExercise();
            }
        }.start();
    }

    private void moveToNextExercise() {
        if (currentExerciseIndex < selectedWorkoutArray.length - 1) {
            currentExerciseIndex++;
            updateExerciseName(selectedWorkoutArray[currentExerciseIndex]);
            resetProgressBar();
            startProgressBar();
        } else {
            // All exercises are done, redirect to WorkoutProgram screen
            Toast.makeText(WorkoutActivity.this, "End of Workout", Toast.LENGTH_SHORT).show();
            // You can use Intent to navigate to the WorkoutProgram activity
            Intent i = new Intent(WorkoutActivity.this, WorkoutProgram.class);
            startActivity(i);
            finish(); // Finish the current activity to prevent going back to it
        }
    }

    private void updateExerciseName(String exerciseName) {
        // Set the exercise name in the TextView
        exerciseNameTextView.setText(exerciseName);
    }

    private void showCurrentExercise() {
        // Display the current exercise name in the TextView
        if (selectedWorkoutArray != null && selectedWorkoutArray.length > 0) {
            currentExerciseIndex = 0; // Set the initial index to 0
            exerciseNameTextView.setText(selectedWorkoutArray[currentExerciseIndex]);
        }
    }

    private void resetProgressBar() {
        // Reset the progress bar to 0
        progress = 0;
        progressBar.setProgress(progress);
        // Cancel the previous countdown timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void startVideo() {
        // Set the video path
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.workout_video;
        Uri uri = Uri.parse(videoPath);

        // Set the video URI to the VideoView
        videoView.setVideoURI(uri);

        // Start video playback
        videoView.start();

        // Set up a listener to restart the video when it completes
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Restart the video
                videoView.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the countdown timer to avoid memory leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
