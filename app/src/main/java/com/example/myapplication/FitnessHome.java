package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FitnessHome extends AppCompatActivity {

    private LineChart lineChart;
    private TextView heightTextView, weightTextView, ageTextView, nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_home);

        Button startBtn = findViewById(R.id.startBtn);

        heightTextView = findViewById(R.id.heightTxt);
        weightTextView = findViewById(R.id.weightTxt);
        ageTextView = findViewById(R.id.ageTxt);
        nameTextView = findViewById(R.id.userName);

        DBHandler dbHandler = new DBHandler(this);

        User newUser = new User();
        newUser.setName("John Doe");
        newUser.setAge(25);
        newUser.setHeight(180.0f);
        newUser.setWeight(70.0f);

        dbHandler.addUser(newUser.getName(), newUser.getAge(), newUser.getHeight(), newUser.getWeight());

        User user = (User) dbHandler.getUserById(1);
        if (user != null) {
            heightTextView.setText(String.valueOf(user.getHeight()) + "cm");
            weightTextView.setText(String.valueOf(user.getWeight()) + "kg");
            ageTextView.setText(String.valueOf(user.getAge()) + "yo");
            nameTextView.setText(String.valueOf(user.getName()));
        }
        dbHandler.close();

        Log.d("testing 0: ", user.getName());


        lineChart = findViewById(R.id.chart);

        Description description = new Description();
        description.setText("Your Activity");
        description.setPosition(150f, 15f);
        lineChart.setDescription(description);

        // Customize X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Arrays.asList("May", "June", "July", "August", "September", "October")));
        xAxis.setLabelCount(6);
        xAxis.setGranularity(1f);

        // Disable Y-axis
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setEnabled(false);

        // Create a single-line data entry (activity values)
        List<Entry> activityEntries = new ArrayList<>();
        activityEntries.add(new Entry(0, 20f));
        activityEntries.add(new Entry(1, 60f));
        activityEntries.add(new Entry(2, 10f));
        activityEntries.add(new Entry(3, 40f));
        activityEntries.add(new Entry(4, 50f));
        activityEntries.add(new Entry(5, 30f));

        // Create a LineDataSet with the activity entries
        LineDataSet activityDataSet = new LineDataSet(activityEntries, "Your Activity");
        activityDataSet.setColor(Color.BLUE);
        activityDataSet.setLineWidth(2f);
        activityDataSet.setDrawCircles(true);
        activityDataSet.setDrawValues(false);

        // Create LineData with the activity DataSet
        LineData lineData = new LineData(activityDataSet);

        // Set the data to the chart
        lineChart.setData(lineData);

        // Customize grid lines
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setDrawGridLines(true);

        // Invalidate the chart to refresh
        lineChart.invalidate();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FitnessHome.this, WorkoutProgram.class);
                startActivity(intent);
            }
        });


        BottomNavigationHelper.setupBottomNavigation(this, R.id.fitness);

    }
}
