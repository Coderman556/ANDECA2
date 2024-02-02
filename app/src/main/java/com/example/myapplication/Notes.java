package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Notes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);
        backgroundImageView.setImageResource(R.drawable.homepagegradient);

        SearchView searchView = findViewById(R.id.searchView);

        //OnClickListener for the searchbar
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // You can open a search activity, show suggestions, etc.
            }
        });

        // Inside your activity or fragment
        RecyclerView recentRecyclerView = findViewById(R.id.recentRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recentRecyclerView.setLayoutManager(layoutManager);

// Set up your RecyclerView adapter and populate data accordingly
//        RecentAdapter adapter = new RecentAdapter(/* pass your data here */);
//        recentRecyclerView.setAdapter(adapter);


        BottomNavigationHelper.setupBottomNavigation(this, R.id.notes);

    }
}