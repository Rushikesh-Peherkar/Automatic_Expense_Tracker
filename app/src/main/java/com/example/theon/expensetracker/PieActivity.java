package com.example.theon.expensetracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.graphics.Color;

import android.util.Log;

import com.example.theon.expensetracker.Database.DBHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;


import java.util.ArrayList;
import java.util.List;

public class PieActivity extends AppCompatActivity {

    private static String TAG = "PieActivity";


    PieChart pieChart1;
    PieChart pieChart2;
    PieChart pieChart3;
    PieChart pieChart4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_pie);
        Log.d(TAG, "onCreate: starting to create chart");

        pieChart1 = (PieChart) findViewById(R.id.budget_month_chart);
        pieChart2 = (PieChart) findViewById(R.id.budget_dinner_chart);
        pieChart3 = (PieChart) findViewById(R.id.budget_gas_chart);
        pieChart4 = (PieChart) findViewById(R.id.budget_others_chart);
        DBHelper db = new DBHelper(this);
        Cursor cur = db.getExpenseByCategory();
        createBudgetGraph(pieChart1,"March Budget","#93b2ff", 6f,1f );
        createBudgetGraph(pieChart2,"Dinner","#ff9c91" , 2f,1f );
        createBudgetGraph(pieChart3,"Gas","#93ffaf", 3f,1f );
        createBudgetGraph(pieChart4,"Others","#ff93f7", 8f,1f );

    }


    private void createBudgetGraph(PieChart pieChart, String Description, String color, Float x, Float y) {
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(x, 0));
        entries.add(new Entry(y, 1));
//        entries.add(new Entry(3f, 2));
//        entries.add(new Entry(2f, 3));

        pieChart.setDescription(Description);
        pieChart.setRotationEnabled(true);
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(Color.parseColor(color));
        colours.add(Color.WHITE);

//        colours.add(Color.parseColor("#A23645"));
//        colours.add(Color.parseColor("#93A63A"));

        pieChart.setUsePercentValues(true);
//        pieChart.setHoleColor(Color.BLACK);
        pieChart.setCenterTextColor(Color.WHITE);
        //pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Budget");
        pieChart.setCenterTextSize(10);

        PieDataSet dataset = new PieDataSet(entries,"category");

        dataset.setSliceSpace(2);
        dataset.setValueTextSize(12);
        dataset.setColors(colours);


        ArrayList<String> labels = new ArrayList<String>();
        labels.add(Description);
        labels.add("Remaining");
//        Legend legend = pieChart.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        PieData data = new PieData(labels, dataset);
        pieChart.setData(data);
        pieChart.animateY(5000);

    }






}