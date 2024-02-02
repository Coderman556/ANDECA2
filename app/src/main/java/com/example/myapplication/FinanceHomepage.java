package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FinanceHomepage extends AppCompatActivity implements View.OnClickListener{

    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_homepage);

        List<FinanceTransaction> transactionsList = new ArrayList<>();
        db = new DBHandler(this);
//        editButton = findViewById(R.id.editButton); // on recyclerItem onClick

//        ######################################################################
//        Code for Bar Graph
        // Assuming db is your DBHandler instance
        List<FinanceTransaction> transactions = db.getAllFinanceTransactions();

// Step 2: Aggregate totals per category
        HashMap<String, Float> categoryTotals = new HashMap<>();
        for (FinanceTransaction transaction : transactions) {
            String category = transaction.getCategory();
            float amount = (float) transaction.getPrice(); // Assuming getPrice() returns a double
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0f) + amount);
        }

// Step 3: Generate chart data
        List<BarEntry> entries = new ArrayList<>();
        List<String> categories = new ArrayList<>(categoryTotals.keySet());
        Collections.sort(categories); // Sort categories if needed

        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            float amount = categoryTotals.get(category);
            entries.add(new BarEntry(i, amount));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Category Spending");
        dataSet.setColor(Color.LTGRAY);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.2f); // Adjust as necessary

        BarChart barChart = findViewById(R.id.barChart);
// Customize the chart as before...
        barChart.setTouchEnabled(false);
// Customize the X-axis to display category names
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));
// Customize the X-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Only integer values for labels
        xAxis.setGranularityEnabled(true); // Enable granularity
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10f);
        xAxis.setLabelCount(categories.size()); // Set the label count to match the number of categories
        xAxis.setCenterAxisLabels(false);


// Customize the Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawLabels(true); // show Y-axis labels
        leftAxis.setDrawAxisLine(true); // show Y-axis line
        leftAxis.setDrawGridLines(false); // hide background grid lines
        leftAxis.setTextColor(Color.BLACK); // Set the color of the axis labels to black
        leftAxis.setTextSize(10f); // Set the size of the axis labels
        leftAxis.setAxisMinimum(0f); // Start at zero

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false); // No right axis

        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
// Final chart setup
        barChart.setData(barData);
        barChart.invalidate(); // Refresh the chart

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
        BottomNavigationHelper.setupBottomNavigation(this, R.id.finance);
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        List<FinanceSnB> displayableSnBs = db.getDisplayableSnB();

        // Update the UI for each goal type
        updateUIForGoalType(displayableSnBs, "saving", R.id.button_savings_goal, R.id.cvSavings, R.id.txtSavings, R.id.savings_display_amount);
        updateUIForGoalType(displayableSnBs, "budget", R.id.button_budget_goal, R.id.cvBudget, R.id.txtBudget, R.id.budget_display_amount);
    }

    private void updateUIForGoalType(List<FinanceSnB> snbs, String goalType, int buttonId, int cardViewId, int textViewId, int amountTextViewId) {
        Button button = findViewById(buttonId);
        CardView cardView = findViewById(cardViewId);
        TextView textView = findViewById(textViewId);
        TextView amountTextView = findViewById(amountTextViewId); // Amount TextView

        for (FinanceSnB snb : snbs) {
            if (snb.getGoalType().equalsIgnoreCase(goalType)) {
                // Data is present for this goal type, show the card view
                button.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
                textView.setText(snb.getGoalPeriod() + " " + snb.getGoalType());
                amountTextView.setText("$" + String.format(Locale.getDefault(), "%.2f", snb.getGoalAmount())); // Format the amount to 2 decimal places

                return; // We found the data, no need to check further
            }
        }
        // No data is present for this goal type, show the button
        cardView.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
//      TODO: create a cardview to display budget and savings & map the onClick
        if (id == R.id.button_budget_goal) {
          Intent i = new Intent(this, FinanceSnB_page.class);
          i.putExtra("DataToView", "budget");
          startActivity(i);
        } else if (id == R.id.button_savings_goal) {
            Intent i = new Intent(this, FinanceSnB_page.class);
            i.putExtra("DataToView", "saving");
            startActivity(i);
        } else if (id == R.id.btnLogExpense) {
//            TODO: Redirect to create log expense activity
//            Intent i = new Intent(this, );
//            startActivity(i);
        } else if (id == R.id.cvSpendings) {
            Intent i = new Intent(this, FinanceOverview.class);
            startActivity(i);
        }
//        TODO: Link edit button in CardView to SnBDetails page
    }


}