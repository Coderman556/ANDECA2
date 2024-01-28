package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class FinanceSaving extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_savings);

//        // Assuming 'X' is an object you're checking.
//        Object X = // ... your object;
//
//                CardView myCardView = findViewById(R.id.myCardView);
//        Button myButton = findViewById(R.id.myButton);
//
//        if (X != null) {
//            // If X is not null, show the CardView and hide the Button.
//            myCardView.setVisibility(View.VISIBLE);
//            myButton.setVisibility(View.GONE);
//        } else {
//            // If X is null, hide the CardView and show the Button.
//            myCardView.setVisibility(View.GONE);
//            myButton.setVisibility(View.VISIBLE);
//        }

        DBHandler db = new DBHandler(this);
        List<FinanceSnB> SnBs = db.getAllSnB();

        for (FinanceSnB SnB : SnBs) {
            if (!(SnB.getGoalType().isEmpty()) && !(SnB.getGoalPeriod().isEmpty())) {
//                if(SnB.getGoalPeriod().equals("daily")) {
//                    CardView cardview = findViewById(R.id.card_daily_budget);
//                    cardview.setVisibility(View.VISIBLE);
//                }
                switch (SnB.getGoalPeriod()) {
                    case "daily":
                        CardView dailycardview = findViewById(R.id.card_daily_budget);
                        dailycardview.setVisibility(View.VISIBLE);

                        Button dailybutton = findViewById(R.id.button_daily_budget);
                        dailybutton.setVisibility(View.GONE);
                        break;
                    case "weekly":
                        CardView weeklycardview = findViewById(R.id.card_weekly_budget);
                        weeklycardview.setVisibility(View.VISIBLE);

                        Button weeklybutton = findViewById(R.id.button_weekly_budget);
                        weeklybutton.setVisibility(View.GONE);
                        break;
                    case "monthly":
                        CardView monthlycardview = findViewById(R.id.card_monthly_budget);
                        monthlycardview.setVisibility(View.VISIBLE);

                        Button monthlybutton = findViewById(R.id.button_monthly_budget);
                        monthlybutton.setVisibility(View.GONE);
                        break;
                }
            }
        }

    }
}