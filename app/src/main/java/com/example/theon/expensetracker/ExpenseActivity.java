package com.example.theon.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.theon.expensetracker.Database.DBHelper;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {
    ListView smsListView;
    DBHelper dbHelper;
    ArrayList<String> smsMessagesList;
    ArrayList<String> ids;

    Integer[] imgid={
            R.drawable.ic_shopping_cart_black_84dp,
            R.drawable.ic_local_dining_black_84dp,
            R.drawable.ic_movie_filter_black_84dp,
            R.drawable.ic_local_gas_station_black_84dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        smsListView = (ListView) findViewById(R.id.allExpenseslist);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        populateexpenses();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseActivity.this, ManualAdd.class);
                startActivityForResult(intent,0);
            }
        });

        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                inside the onclick of a list item

                String itemAtPosition = smsListView.getItemAtPosition(position).toString();
                String id_1=ids.get(position);

//                System.out.println(itemAtPosition);
                Intent intent = new Intent(getApplicationContext(), detailedExpenseView.class);
                intent.putExtra("id_1",id_1);
                intent.putExtra("data_values",itemAtPosition);
                startActivity(intent);
            }
        });

    }


    public void populateexpenses(){
        dbHelper = new DBHelper(this);

        smsMessagesList = new ArrayList<String>();
        smsMessagesList.clear();

        Cursor res = dbHelper.getAllData();
        if(res.getCount()==0){
            Log.d("DATABASE:","EMPTY!!");
            return;
        }


        ids=new ArrayList<>();
        int i =0;
        while (res.moveToNext()) {
            StringBuffer entry = new StringBuffer();
            ids.add(res.getString(0));

            entry.append(res.getString(2) + "\n");
            entry.append("$"+res.getString(4) + "\n");
            entry.append(res.getString(1) + " - "+res.getString(3)+"\n");
            entry.append( res.getString(5) + "\n");
            entry.append(res.getString(6));
            smsMessagesList.add(entry.toString());
        }
        CustomListAdapter adapter=new CustomListAdapter(this, smsMessagesList, imgid);
        //smsListView=(ListView)findViewById(R.id.list);
        smsListView.setAdapter(adapter);
        /*arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.listlayputexpenses,
                R.id.Itemname,smsMessagesList);
        smsListView.setAdapter(arrayAdapter);*/
    }
    public void onResume(){
        super.onResume();
        populateexpenses();
        // put your code here...
    }
}
