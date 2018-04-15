package com.example.theon.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theon.expensetracker.Database.DBHelper;

public class bar_chart_day extends AppCompatActivity {

    EditText detailedView_For,detailedView_DateSpent,detailedView_BankName,detailedView_Cost,detailedView_Category;
    TextView detailedView_showSpecificSms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart_day);
        DBHelper dbHelper;

        detailedView_BankName = (EditText) findViewById(R.id.bar_chart_day_detailedView_BankName);
        detailedView_Cost = (EditText) findViewById(R.id.bar_chart_day_detailedView_Cost);
        detailedView_DateSpent = (EditText) findViewById(R.id.bar_chart_day_detailedView_DateSpent);
        detailedView_For = (EditText) findViewById(R.id.bar_chart_day_detailedView_For);
        detailedView_Category=(EditText)findViewById(R.id.bar_chart_day_detailedView_Category);



        Intent intent = getIntent();
        String contents = intent.getStringExtra("yaxis_value");
        System.out.println("cost value here is "+ contents);
        dbHelper=new DBHelper(getApplicationContext());
        String cost="",for_what="",date="",bank="",category="";
        Cursor res=dbHelper.getDataByCost(contents);

        while(res.moveToNext())
        {
            StringBuffer entry = new StringBuffer();
            bank=res.getString(1);
            cost=res.getString(4);
            date=res.getString(3);
            for_what=res.getString(2);
            category=res.getString(5);


        }

        detailedView_BankName.setText(bank);
        detailedView_BankName.setFocusable(false);
        detailedView_Cost.setText(cost);
        detailedView_Cost.setFocusable(false);
        detailedView_For.setText(for_what);
        detailedView_For.setFocusable(false);
        detailedView_DateSpent.setText(date);
        detailedView_DateSpent.setFocusable(false);
        detailedView_Category.setText(category);
        detailedView_Category.setFocusable(false);


    }
}
