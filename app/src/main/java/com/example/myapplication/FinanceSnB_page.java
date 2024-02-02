package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FinanceSnB_page extends AppCompatActivity implements View.OnClickListener {

    private String viewableData;
    private List<FinanceSnB> SnBs;

    // Class-wide variables for views
    private CardView dailycardview, weeklycardview, monthlycardview;
    private TextView dailyTextView, weeklyTextView, monthlyTextView;
    private Button dailybutton, weeklybutton, monthlybutton;
    private TextView dailyDisplayAmount, weeklyDisplayAmount, monthlyDisplayAmount;
    private DBHandler db = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_snb_page);


        SnBs = db.getAllSnB();

        viewableData = getIntent().getStringExtra("DataToView"); // Type of goal

        // Assign values to class-wide variables
        dailycardview = findViewById(R.id.card_daily_display);
        weeklycardview = findViewById(R.id.card_weekly_display);
        monthlycardview = findViewById(R.id.card_monthly_display);

        dailyTextView = findViewById(R.id.card_daily_text);
        weeklyTextView = findViewById(R.id.card_weekly_text);
        monthlyTextView = findViewById(R.id.card_monthly_text);

        dailybutton = findViewById(R.id.button_daily);
        weeklybutton = findViewById(R.id.button_weekly);
        monthlybutton = findViewById(R.id.button_monthly);

        dailyDisplayAmount = findViewById(R.id.daily_display_amount);
        weeklyDisplayAmount = findViewById(R.id.weekly_display_amount);
        monthlyDisplayAmount = findViewById(R.id.monthly_display_amount);

        // Set text for buttons based on viewableData
        dailybutton.setText("Daily " + viewableData);
        weeklybutton.setText("Weekly " + viewableData);
        monthlybutton.setText("Monthly " + viewableData);

        // hide all card views and show all buttons initially
        dailycardview.setVisibility(View.GONE);
        weeklycardview.setVisibility(View.GONE);
        monthlycardview.setVisibility(View.GONE);

        dailybutton.setVisibility(View.VISIBLE);
        weeklybutton.setVisibility(View.VISIBLE);
        monthlybutton.setVisibility(View.VISIBLE);

        // Setting onClick listeners
        dailybutton.setOnClickListener(this);
        weeklybutton.setOnClickListener(this);
        monthlybutton.setOnClickListener(this);

        updateUIBasedOnSnBs();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUIBasedOnSnBs();
    }

    private void updateUIBasedOnSnBs() {
        SnBs = db.getAllSnB();
        boolean dailyFound = false, weeklyFound = false, monthlyFound = false;

        for (FinanceSnB SnB : SnBs) {
            if (SnB.getGoalType().equals(viewableData)) {
                switch (SnB.getGoalPeriod()) {
                    case "daily":
                        dailyFound = true;
                        if (SnB.getGoalAmount() != 0) {
                            dailycardview.setVisibility(View.VISIBLE);
                            dailyTextView.setText("Daily " + viewableData);
                            dailyDisplayAmount.setText("$" + String.valueOf(SnB.getGoalAmount()));
                            dailybutton.setVisibility(View.GONE);
                        }
                        break;
                    case "weekly":
                        weeklyFound = true;
                        if (SnB.getGoalAmount() != 0) {
                            weeklycardview.setVisibility(View.VISIBLE);
                            weeklyTextView.setText("Weekly " + viewableData);
                            weeklyDisplayAmount.setText("$" + String.valueOf(SnB.getGoalAmount()));
                            weeklybutton.setVisibility(View.GONE);
                        }
                        break;
                    case "monthly":
                        monthlyFound = true;
                        if (SnB.getGoalAmount() != 0) {
                            monthlycardview.setVisibility(View.VISIBLE);
                            monthlyTextView.setText("Monthly " + viewableData);
                            monthlyDisplayAmount.setText("$" + String.valueOf(SnB.getGoalAmount()));
                            monthlybutton.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        }

        // Handle cases where data is missing, so we revert to showing the button
        if (!dailyFound) {
            dailycardview.setVisibility(View.GONE);
            dailybutton.setVisibility(View.VISIBLE);
        }
        if (!weeklyFound) {
            weeklycardview.setVisibility(View.GONE);
            weeklybutton.setVisibility(View.VISIBLE);
        }
        if (!monthlyFound) {
            monthlycardview.setVisibility(View.GONE);
            monthlybutton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        String goalPeriod = "nuh uh";
        if (view.getId() == R.id.button_daily || view.getId() == R.id.ebtnDaily) goalPeriod = "daily";
        else if (view.getId() == R.id.button_weekly || view.getId() == R.id.ebtnWeekly) goalPeriod = "weekly";
        else if (view.getId() == R.id.button_monthly || view.getId() == R.id.ebtnMonthly) goalPeriod = "monthly";

        Log.d("Testing 0", String.valueOf(SnBs.size()));
        Intent intent = new Intent(this, FinanceSnBDetails.class);
        for (FinanceSnB snb : SnBs) {
            // Ensure case-insensitive comparison and trim potential leading/trailing spaces
            Log.d("Testing 1", snb.getGoalType() + " " + snb.getGoalPeriod());
            Log.d("Testing 2", viewableData + " " + goalPeriod);
            if (snb.getGoalType().trim().equalsIgnoreCase(viewableData) && snb.getGoalPeriod().trim().equalsIgnoreCase(goalPeriod)) {
                intent.putExtra("DisplayableType", snb.getGoalType());
                intent.putExtra("DisplayablePeriod", snb.getGoalPeriod());
                intent.putExtra("DisplayableAmount", snb.getGoalAmount());

                break; // Exit after finding the match
            } else {
                Log.d("Debug", "No matching SnB found. Type: " + viewableData + ", Period: " + goalPeriod);
                FinanceSnB newSnB = new FinanceSnB();
                newSnB.setGoalType(viewableData); // Set the type from the intent extra
                newSnB.setGoalPeriod(goalPeriod); // Set the period based on the clicked button
                newSnB.setGoalAmount(0); // Default value, assuming zero indicates a new entry to be filled

                // Start FinanceSnBDetails activity with the new SnB
                intent.putExtra("DisplayableType", newSnB.getGoalType());
                intent.putExtra("DisplayablePeriod", newSnB.getGoalPeriod());
                intent.putExtra("DisplayableAmount", newSnB.getGoalAmount());
            }
        }
        startActivity(intent);
    }
}
