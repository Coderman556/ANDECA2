package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FinanceSnBDetails extends AppCompatActivity implements View.OnClickListener{

    private String extraType;
    private String extraPeriod;
    private double extraAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_snb_details);

        extraType = getIntent().getStringExtra("DisplayableType");
        extraPeriod = getIntent().getStringExtra("DisplayablePeriod");
        extraAmount = getIntent().getDoubleExtra("DisplayableAmount", 0.0);

        if (extraType.equals("saving")) {
            if (extraPeriod.equals("daily")) {
                TextView textview = findViewById(R.id.cardTitle);
                textview.setText("Daily Savings");
                EditText amount = findViewById(R.id.txtGoal);
                amount.setText(String.valueOf(extraAmount));
            } else if (extraPeriod.equals("weekly")) {
                TextView textview = findViewById(R.id.cardTitle);
                textview.setText("Weekly Savings");
                EditText amount = findViewById(R.id.txtGoal);
                amount.setText(String.valueOf(extraAmount));
            } else if (extraPeriod.equals("monthly")) {
                TextView textview = findViewById(R.id.cardTitle);
                textview.setText("Monthly Savings");
                EditText amount = findViewById(R.id.txtGoal);
                amount.setText(String.valueOf(extraAmount));
            } else {
                Toast.makeText(getApplicationContext(), "Failed to get Period within savings", Toast.LENGTH_LONG).show();
            }
        } else if (extraType.equals("budget")) {

            findViewById(R.id.txtSavingsFormula).setVisibility(View.GONE);

            if (extraPeriod.equals("daily")) {
                TextView textview = findViewById(R.id.cardTitle);
                textview.setText("Daily Budget");
                EditText amount = findViewById(R.id.txtGoal);
                amount.setText(String.valueOf(extraAmount));
            } else if (extraPeriod.equals("weekly")) {
                TextView textview = findViewById(R.id.cardTitle);
                textview.setText("Weekly Budget");
                EditText amount = findViewById(R.id.txtGoal);
                amount.setText(String.valueOf(extraAmount));
            } else if (extraPeriod.equals("monthly")) {
                TextView textview = findViewById(R.id.cardTitle);
                textview.setText("Monthly Budget");
                EditText amount = findViewById(R.id.txtGoal);
                amount.setText(String.valueOf(extraAmount));
            } else {
                Toast.makeText(getApplicationContext(), "Failed to get Period within budget", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to get Type", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        Log.d("testing ", "onClick is initialized");
        if (view.getId() == R.id.submit_button) {
            Log.d("testing 0 ", "onClick is registered");
            DBHandler db = new DBHandler(this);
            EditText editTextNumber = findViewById(R.id.txtGoal);
            double editTextValue;
            try {
                editTextValue = Double.parseDouble(editTextNumber.getText().toString());
            } catch (NumberFormatException e) {
                editTextValue = 0.0; // Default to 0 if parsing fails
            }

            FinanceSnB param = new FinanceSnB();

            if (extraAmount == 0 && editTextValue != 0) {
                Log.d("testing 1 ", "(extraAmount == 0 && editTextValue != 0) = true");
                param.setGoalType(extraType);
                param.setGoalPeriod(extraPeriod);
                param.setGoalAmount(editTextValue);

//            FinanceSnB param
                long result = db.addSnB(param);

                if (result == -1) {
                    // Insertion failed
                    Toast.makeText(this, "Failed to add SnB", Toast.LENGTH_LONG).show();
                } else {
                    // Insertion successful
                    Toast.makeText(this, "SnB added successfully with row id: " + result, Toast.LENGTH_SHORT).show();
                }
            } else if (editTextValue == 0) {
                Log.d("testing 2 ", "(editTextValue == 0) = true");
//            params -> goal.type, goal.period
                db.deleteSnB(extraType, extraPeriod);

                Toast.makeText(getApplicationContext(), "There isn't any data so goal is deleted!", Toast.LENGTH_SHORT).show();

            } else {
                Log.d("testing 3 ", "All has failed and we're updating the value");
                param.setGoalAmount(editTextValue);
                param.setGoalPeriod(extraPeriod);
                param.setGoalType(extraType);

//            FinanceSnB param
                int rowsAffected = db.updateSnB(param);
                if (rowsAffected > 0) {
                    Toast.makeText(getApplicationContext(), "Successful Data Update!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unsuccessful Data Update...", Toast.LENGTH_LONG).show();
                }
            }

        }
}

}