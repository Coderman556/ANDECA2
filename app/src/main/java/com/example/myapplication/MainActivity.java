package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import androidx.appcompat.widget.SearchView;


public class MainActivity extends AppCompatActivity{

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);
        backgroundImageView.setImageResource(R.drawable.homepagegradient); // Replace with your image resource

        TextView greetingTextView = findViewById(R.id.greetingTextView);

        // Determine the time of day and set the greeting
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 6 && hour < 12) {
            greetingTextView.setText("Good Morning\nJohn.");
        } else if (hour >= 12 && hour < 18) {
            greetingTextView.setText("Good Afternoon\nJohn.");
        } else {
            greetingTextView.setText("Good Evening\nJohn.");
        }

        SearchView searchView = findViewById(R.id.searchView);

        // Set OnClickListener for the entire SearchView
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // You can open a search activity, show suggestions, etc.
            }
        });
    }

}
