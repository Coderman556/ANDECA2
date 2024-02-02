package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity{

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);
        backgroundImageView.setImageResource(R.drawable.homepagegradient);

        TextView greetingTextView = findViewById(R.id.greetingTextView);


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

        //OnClickListener for the searchbar
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // You can open a search activity, show suggestions, etc.
            }
        });

//onClickLister for closing/opening tips section
        ImageView closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tipsLayout = findViewById(R.id.dailyTipsLayout);
                ImageView closeButtonImage = (ImageView) v;

                if (tipsLayout.getVisibility() == View.VISIBLE) {
                    tipsLayout.setVisibility(View.GONE);
                    // Change the image to the 'open' state
                    closeButtonImage.setImageResource(R.drawable.ic_baseline_add_24);
                } else {
                    tipsLayout.setVisibility(View.VISIBLE);
                    // Change the image to the 'close' state
                    closeButtonImage.setImageResource(R.drawable.ic_baseline_close_24);
                }
            }
        });




        BottomNavigationHelper.setupBottomNavigation(this, R.id.home);

    }
}
