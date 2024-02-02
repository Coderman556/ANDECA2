package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

public class FinanceHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_homepage);
        DBHandler db = new DBHandler(this);
        List<FinanceSnB> SnBList = db.getAllSnB();

        for (FinanceSnB snb : SnBList) {
//            TODO: Investigate creating a break condition to stop iterating through saving/budget
            if (snb.getGoalType().equals("saving")) {
                if (snb.getGoalPeriod().equals("daily")) {
//                    TODO: Inflate CardView with daily data
                } else if (snb.getGoalPeriod().equals("weekly")) {
//                    TODO: Inflate CardView with weekly data
                } else if (snb.getGoalPeriod().equals("monthly")) {
//                    TODO: Inflate CardView with monthly data
                } else {
//                    TODO: Display Button to add a Savings Goal
                }
            } else {
                if (snb.getGoalPeriod().equals("daily")) {
//                    TODO: Inflate CardView with daily data
                } else if (snb.getGoalPeriod().equals("weekly")) {
//                    TODO: Inflate CardView with weekly data
                } else if (snb.getGoalPeriod().equals("monthly")) {
//                    TODO: Inflate CardView with monthly data
                } else {
//                    TODO: Display Button to add a Savings Goal
                }
            }
        }
    }

}