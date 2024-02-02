package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Create an instance of DBHandler
        DBHandler dbHandler = new DBHandler(this);

        NotesNote yourNoteObject = new NotesNote();
        yourNoteObject.setId(1);
        yourNoteObject.setTitle("Your Note Title");
        yourNoteObject.setCont("Your Note Content");
        yourNoteObject.setIsFavourite(true);
        yourNoteObject.setLastOpened("2024-01-30 12:00:00");
        yourNoteObject.setDeleted(null);


        dbHandler.addNoteToDatabase(yourNoteObject);

        Thread splashThread = new Thread() {
            public void run() {
                try {
                    // sleep time in milliseconds (3000 = 3sec)
                    sleep(3000);
                } catch (InterruptedException e) {
                    // Trace the error
                    e.printStackTrace();
                } finally {
                    // Launch the MainActivity class
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        // To Start the thread
        splashThread.start();
    }
}
