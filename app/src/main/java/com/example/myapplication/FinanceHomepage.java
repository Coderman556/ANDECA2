package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class FinanceHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_homepage);
        DBHandler db = new DBHandler(this);
        List<FinanceSnB> SnBList = db.getDisplayableSnB();

        for (FinanceSnB snb : SnBList) {
//            TODO: inflate CardView/Button with relevant data.
            if (snb.getGoalType().equals("saving")) {
                Button button = findViewById(R.id.button_savings_goal);
                button.setVisibility(View.GONE);

                CardView cardview = findViewById(R.id.cvSavings);
                cardview.setVisibility(View.VISIBLE);

                TextView title = findViewById(R.id.txtSavings);
                title.setText(snb.getGoalPeriod() + " " + snb.getGoalType());
            }
            if (snb.getGoalType().equals("budget")){
                Button button = findViewById(R.id.button_savings_goal);
                button.setVisibility(View.GONE);

                CardView cardview = findViewById(R.id.cvBudget);
                cardview.setVisibility(View.VISIBLE);
//                TODO: change the data displayed programmatically depending on what the get method returns.

                TextView title = findViewById(R.id.txtBudget);
                title.setText(snb.getGoalPeriod() + " " + snb.getGoalType());
            }
        }
    }

}