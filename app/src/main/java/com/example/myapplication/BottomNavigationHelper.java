package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHelper {

    public static void setupBottomNavigation(Activity activity, int selectedItemId) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                handleItemSelected(item, activity, bottomNavigationView);
                return true;
            }
        });
    }

    private static void handleItemSelected(MenuItem item, Activity activity, BottomNavigationView bottomNavigationView) {
        // Check if the selected item is already the current one
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
            return;
        }

        // Handle navigation item clicks here
        switch (item.getItemId()) {
            case R.id.home:
                // Handle the Home icon click
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.overridePendingTransition(0, 0);
                activity.finish();
                break;

            case R.id.notes:
                // Handle the Notes icon click
                activity.startActivity(new Intent(activity, Notes.class));
                activity.overridePendingTransition(0, 0);
                activity.finish();
                break;

            case R.id.fitness:
                // Handle the Fitness icon click
                activity.overridePendingTransition(0, 0);
                break;

            case R.id.finance:
                // Handle the Finance icon click
                break;
        }
    }
}

