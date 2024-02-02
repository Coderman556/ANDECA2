package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class FinanceOverview extends AppCompatActivity implements FinanceTransactionDialog.FinanceTransactionDialogListener, View.OnClickListener {



    private DBHandler db;
    private RecyclerView mRecyclerView;
    private ArrayList<FinanceTransaction> mTransactions =  new ArrayList<FinanceTransaction>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_overview);
        RecyclerView recyclerView = findViewById(R.id.recentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

//        ######################################################################

//TODO: instead of bindCountriesData, call getAll and iterate through resultant list using for loop

        mTransactions = db.getAllFinanceTransactions();
        setUIRef();
    }

    private void setUIRef() {
        mRecyclerView = findViewById(R.id.recentRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        FinanceTransactionAdapter myAdapter = new FinanceTransactionAdapter(mTransactions, transaction -> {
            // Recycler item click opens the dialog for editing
            showFinanceTransactionDialog(transaction, false); // 'false' for edit
        });
        mRecyclerView.setAdapter(myAdapter);
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, FinanceTransaction transaction, boolean isNew) {
        if (isNew) {
            // Add the new transaction to the database
            db.addFinanceTransaction(transaction);
        } else {
            // Update the existing transaction in the database
            db.updateFinanceTransaction(transaction);
        }
        // Refresh the UI to reflect changes
        refreshTransactions();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Handle the case where the user cancels the dialog
    }

    @Override
    public void onDialogDeleteClick(DialogFragment dialog, FinanceTransaction transaction) {
        if (transaction != null) {
            // Perform the delete operation
            db.deleteFinanceTransaction(transaction.getId());

            // Refresh the UI (e.g., by reloading the data for the RecyclerView)
            refreshTransactions();

            // Optionally, show a confirmation message
            Toast.makeText(this, "Transaction deleted", Toast.LENGTH_SHORT).show();
        }
    }


    public void showFinanceTransactionDialog(FinanceTransaction transaction, boolean isNew) {
        FinanceTransactionDialog dialogFragment = FinanceTransactionDialog.newInstance(transaction, isNew);
        dialogFragment.show(getSupportFragmentManager(), "FinanceTransactionDialog");
    }


    private void refreshTransactions() {
        // Reload transactions from the database and update the RecyclerView
        mTransactions.clear();
        mTransactions.addAll(db.getAllFinanceTransactions());
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton) {
            // Plus icon clicked, open dialog for adding a new transaction
            showFinanceTransactionDialog(new FinanceTransaction(), true); // 'true' for new transaction
        } else if (v.getId() == R.id.searchTransaction) {
            Intent i = new Intent(this, FinanceViewAllTransactions.class);
            startActivity(i);
        }
    }

}