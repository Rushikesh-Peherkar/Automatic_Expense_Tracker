package com.example.theon.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theon.expensetracker.Database.DBHelper;

import java.util.ArrayList;

public class detailedExpenseView extends AppCompatActivity {
    EditText detailedView_For,detailedView_DateSpent,detailedView_BankName,detailedView_Cost,detailedView_Category;
    TextView detailedView_showSpecificSms;
    DBHelper dbHelper;
    Button detailedView_edit,detailedView_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_expense_view);


        detailedView_BankName = (EditText) findViewById(R.id.detailedView_BankName);
        detailedView_Cost = (EditText) findViewById(R.id.detailedView_Cost);
        detailedView_DateSpent = (EditText) findViewById(R.id.detailedView_DateSpent);
        detailedView_For = (EditText) findViewById(R.id.detailedView_For);
        detailedView_Category=(EditText)findViewById(R.id.detailedView_Category);


        detailedView_edit = (Button) findViewById(R.id.detailedView_edit);
        detailedView_save = (Button) findViewById(R.id.detailedView_save);


        Intent intent = getIntent();
        final String id=intent.getStringExtra("id_1");

        String contents = intent.getStringExtra("data_values");
        String[] values = contents.split("\\n");

        String location = values[0];
        String cost = "$"+values[1];

        String[] values1 = values[2].split("\\-");
        String bank = values1[0], date = values1[1];
        String category=values[3];
        final String type=values[4];

        detailedView_BankName.setText(bank);
        detailedView_BankName.setFocusable(false);

        detailedView_Cost.setText(cost);
        detailedView_Cost.setFocusable(false);
        detailedView_For.setText(location);
        detailedView_For.setFocusable(false);
        detailedView_DateSpent.setText(date);
        detailedView_DateSpent.setFocusable(false);
        detailedView_Category.setText(category);
        detailedView_Category.setFocusable(false);




        detailedView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cost="",for_what="",date="",bank="",category="";

                detailedView_Cost.setFocusableInTouchMode(true);
                cost =detailedView_Cost.getText().toString();
                detailedView_For.setFocusableInTouchMode(true);
                for_what=detailedView_For.getText().toString();
                detailedView_DateSpent.setFocusableInTouchMode(true);
                date=detailedView_DateSpent.getText().toString();
                detailedView_BankName.setFocusableInTouchMode(true);
                bank=detailedView_BankName.getText().toString();
                detailedView_Category.setFocusableInTouchMode(true);
                category=detailedView_Category.getText().toString();

                detailedView_Cost.setText(cost);

                detailedView_For.setText(for_what);

                detailedView_DateSpent.setText(date);

                detailedView_BankName.setText(bank);

                detailedView_Category.setText(category);

                if (cost.isEmpty() || for_what.isEmpty() || date.isEmpty() || bank.isEmpty() || category.isEmpty()) {
                    Toast.makeText(detailedExpenseView.this, "please fill all the values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        detailedView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper=new DBHelper(getApplicationContext());

               int return_rows= dbHelper.updateData(id,detailedView_BankName.toString(),detailedView_For.toString(),detailedView_DateSpent.toString(),detailedView_Cost.toString(),detailedView_Category.toString(),type);

               if(return_rows!=0) {
                   Toast.makeText(detailedExpenseView.this, "Data successfully updated", Toast.LENGTH_LONG).show();
               }
                else{
                   Toast.makeText(detailedExpenseView.this, "Data not updated", Toast.LENGTH_LONG).show();
               }
            }
        });













    }
}
