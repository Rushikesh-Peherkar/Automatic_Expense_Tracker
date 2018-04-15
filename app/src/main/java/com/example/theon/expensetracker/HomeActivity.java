package com.example.theon.expensetracker;
import com.example.theon.expensetracker.Database.DBHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private ImageButton smsTrigger,addTrigger;
    private PieChart pieChart;
    private static final int REQUEST_READ_SMS = 2;
    private TextView allTransactions;
    private TextView detailedBudget;
    private TextView accountTile ;
    BarChart barChart;
    ArrayList<String> dates;
    ArrayList<String> months;
    Random random;
    ArrayList<BarEntry> barEntries;
    DBHelper dbHelper;
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> smsMessagesList;
    Spinner select_date_month;
ArrayList<String> ids;

    Integer[] imgid={
            R.drawable.ic_shopping_cart_black_84dp,
            R.drawable.ic_local_dining_black_84dp,
            R.drawable.ic_movie_filter_black_84dp,
            R.drawable.ic_local_gas_station_black_84dp
    };
    private static String TAG = "HomeActivity.class";
    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        smsListView = (ListView) findViewById(R.id.expenseslist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        select_date_month=(Spinner)findViewById(R.id.select_date_month);
        barChart = (BarChart) findViewById(R.id.bargraph);
        pieChart = (PieChart) findViewById(R.id.budget_chart);


        ArrayAdapter<CharSequence> charSequenceArrayAdapter=ArrayAdapter.createFromResource(this,R.array.spinner_bargraph,android.R.layout.simple_spinner_item);
        charSequenceArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        select_date_month.setAdapter(charSequenceArrayAdapter);

        select_date_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 1:
                        Toast.makeText(HomeActivity.this,"selected bar graph by MONTH",Toast.LENGTH_SHORT).show();
//                        GET the month's data of currencies+ see how to to it faster..
                        createRandomBarGraph_month();

                    case 0:
                        Toast.makeText(HomeActivity.this,"selected bar graph by day",Toast.LENGTH_SHORT).show();
//-------------**** do toggling tomorrow ask team mates and do it faster..

                        createRandomBarGraph("09/05/2017", "15/03/2018");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                createRandomBarGraph("09/05/2017", "15/03/2018");
            }
        });








        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS)) {
            Toast.makeText(this, "Permission needed to access your sms", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, 1);
        }

//        addTrigger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, ManualAdd.class);
//                startActivityForResult(intent,0);
//            }
//        });

        populateexpenses();
        createRandomBarGraph("2016/05/05", "2016/06/01");
        createBudgetGraph();


        accountTile =(EditText) findViewById(R.id.accounts);
        dbHelper = new DBHelper(this);

        Cursor res = dbHelper.getAllData();
        if(res.getCount()==0){
            Log.d("DATABASE:","EMPTY!!");
            return;
        } else {
            int cost = 0;
            int i =0;
            while (res.moveToNext()) {
                cost += Integer.parseInt(res.getString(0));
            }
            accountTile.setText("Accounts \nCurrent Spending = $543");

        }


        smsTrigger = findViewById(R.id.parsesms);
        smsTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,parseSMS.class);
                startActivityForResult(intent,0);
            }
        });
        accountTile.setEnabled(false);
//        allTransactions = findViewById(R.id.viewTransaction);
//        allTransactions.setOnClickListener(new View.onClickListener() {
//
//        });

//--------------------
//        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                inside the onclick of a list item
//
//                String itemAtPosition = smsListView.getItemAtPosition(position).toString();
//                String id_1=ids.get(position);
//
////                System.out.println(itemAtPosition);
//                Intent intent = new Intent(getApplicationContext(), detailedExpenseView.class);
//                    intent.putExtra("id_1",id_1);
//                    intent.putExtra("data_values",itemAtPosition);
//                startActivity(intent);
//            }
//        });

//-------------------------
//        pieChart = findViewById(R.id.pie);
//        pieChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), PieActivity.class);
//                startActivity(intent);
//            }
//        });




    }

    private void createBudgetGraph() {
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(6f, 0));
        entries.add(new Entry(1f, 1));
//        entries.add(new Entry(3f, 2));
//        entries.add(new Entry(2f, 3));

        pieChart.setDescription("March Budget");
        pieChart.setRotationEnabled(true);
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(Color.parseColor("#93ceff"));
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
        labels.add("Monthly Budget");
        labels.add("Remaining");
//        Legend legend = pieChart.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        PieData data = new PieData(labels, dataset);
        pieChart.setData(data);
        pieChart.animateY(5000);

    }

    public void perform_action(View v)
    {   allTransactions = (TextView) findViewById(R.id.viewTransaction);
        allTransactions.setTextColor(Color.BLUE);
        Intent intent = new Intent(getApplicationContext(),ExpenseActivity.class);
            startActivity(intent);


        //assign the textview forecolor

    }

    public void perform_action_budgets(View v) {
        detailedBudget = (TextView) findViewById(R.id.categoryBudget);
        detailedBudget.setTextColor(Color.BLUE);
        Intent intent = new Intent(getApplicationContext(),PieActivity.class);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setResult(RESULT_OK, null);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.account) {
            // Handle the camera action
        } else if (id == R.id.balance || id == R.id.logout) {
            // handle logout here
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();

        populateexpenses();
        createRandomBarGraph("2016/05/05", "2016/06/01");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getStringExtra("methodName").equals("myMethod")){
            Toast.makeText(this, "Refreshed!", Toast.LENGTH_LONG).show();
            populateexpenses();
            createRandomBarGraph("2016/05/05", "2016/06/01");

        }
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
        while (res.moveToNext() && i < 2) {
            StringBuffer entry = new StringBuffer();
            ids.add(res.getString(0));

            entry.append(res.getString(2) + "\n");
            entry.append("$"+res.getString(4) + "\n");
            entry.append(res.getString(1) + " - "+res.getString(3)+"\n");
            entry.append( res.getString(5) + "\n");
            entry.append(res.getString(6));
            i++;
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

    public void createRandomBarGraph_month() {
        ArrayList<BarEntry> barEntries_month=new ArrayList<BarEntry>();
        DBHelper dbHelper = new DBHelper(this);
        Cursor res = dbHelper.getAllData();
        if (res.getCount() == 0) {
            Log.d("DATABASE:", "EMPTY!!!!!!!!!!!!!!!!!");
            return;
        }


        int i = 0;
        barEntries = new ArrayList<>();
        months = new ArrayList<>();
//        only to get all ids of messages

        while (res.moveToNext()) {
            StringBuffer entry = new StringBuffer();
            entry.append("Bank : " + res.getString(1) + "\n");
            entry.append("Location : " + res.getString(2) + "\n");
            entry.append("Date : " + res.getString(3) + "\n");
            entry.append("Cost : " + res.getString(4) + "\n");
            entry.append("Category : " + res.getString(5) + "\n");
            entry.append("Type : " + res.getString(6));
            Log.d("Debugging", res.getString(3) + " chec " + res.getString(4));


            if (res.getString(4).length() != 0 && res.getString(3).length() != 0) {

//                barEntries.add(new BarEntry(Float.parseFloat(res.getString(4).replace("$", "")), i));
                dates.add(res.getString(3).replace(",", " "));
            }
            i++;
        }
        for (int j = 0; j < dates.size(); j++) {
            String[] date_split = dates.get(j).split("\\s");

//          split 0...month

            if (date_split[0].equals("January")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("October")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("February")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("March")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("April")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("May")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("June")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("July")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("August")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("September")) {
                months.add(date_split[0]);
            } else if (date_split[0].equals("November")) {
                months.add(date_split[0]);
            } else {

                months.add("December");
            }



        }

//get unique months
        HashSet hs=new HashSet();
        hs.addAll(months);
        months.clear();
        months.addAll(hs);

        for(int j=0;j<months.size();j++) {

            Cursor sum_month = dbHelper.getMonthExpenses(months.get(j));

            while (sum_month.moveToNext()) {

                if (sum_month.getString(0).length() != 0) {
                    Log.d("query answer is", sum_month.getString(0));
//                answer returning 0 for all
                    barEntries_month.add(new BarEntry(Float.parseFloat(sum_month.getString(0).replace("$", "")), 0));
                }
            }
        }


//split 2 by year. not done


        //Collections.reverse(dates);
        //Collections.reverse(barEntries);



        barChart.invalidate();

//        bar graph per date expenses view
        BarDataSet barDataSet = new BarDataSet(barEntries_month,"currency");
        BarData barData = new BarData(months, barDataSet);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(barData);
        barChart.setDescription("Expenses Graph");
        barChart.setDescriptionTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        barChart.setDescriptionTextSize(9f);
        /* clear the existing chart and redraw new values , ERROR IN THIS CHART IS NOT UPDATING. see this command... but x,y values are correct. */
        barChart.notifyDataSetChanged();


        }




    public void createRandomBarGraph(String Date1, String Date2){

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//
//        try {
//            Date date1 = simpleDateFormat.parse(Date1);
//            Date date2 = simpleDateFormat.parse(Date2);
//
//            Calendar mDate1 = Calendar.getInstance();
//            Calendar mDate2 = Calendar.getInstance();
//            mDate1.clear();
//            mDate2.clear();
//
//            mDate1.setTime(date1);
//            mDate2.setTime(date2);
//
//            dates = new ArrayList<>();
//            dates = getList(mDate1,mDate2);
//
//            barEntries = new ArrayList<>();
//            float max = 0f;
//            float value = 0f;
//            random = new Random();
//            for(int j = 0; j< dates.size();j++){
//                max = 100f;
//                value = random.nextFloat();
//                Log.d(TAG, Float.toString(value));
//                value = value * max;
//                Log.d(TAG, "After " + Float.toString(value));
//                barEntries.add(new BarEntry(value,j));
//            }
//
//        }catch(ParseException e){
//            e.printStackTrace();
//        }
        DBHelper dbHelper = new DBHelper(this);

        Cursor res = dbHelper.getAllData();
        if(res.getCount()==0){
            Log.d("DATABASE:","EMPTY!!!!!!!!!!!!!!!!!");
            return;
        }
    int i=0;
        barEntries = new ArrayList<>();
        dates = new ArrayList<>();
//        only to get all ids of messages

        while (res.moveToNext()) {
            StringBuffer entry = new StringBuffer();
            entry.append("Bank : " + res.getString(1) + "\n");
            entry.append("Location : " + res.getString(2) + "\n");
            entry.append("Date : " + res.getString(3) + "\n");
            entry.append("Cost : " + res.getString(4) + "\n");
            entry.append("Category : " + res.getString(5) + "\n");
            entry.append("Type : " + res.getString(6));


            try {
                if(res.getString(4).length() != 0 && res.getString(3).length() != 0 )
                {
                    barEntries.add(new BarEntry(Float.parseFloat(res.getString(4).replace("$","").replace(",","")),i));
                    dates.add(res.getString(3).replace(",",""));
                }
            }
            catch (Exception e){
                Log.d("EXCEPTION", e.toString());
            }

            i++;
        }




//        //Collections.reverse(dates);
//        //Collections.reverse(barEntries);
        for(int a = 0; a < dates.size();a++) {
            Log.d("reverse" , dates.get(a));
        }

        //barChart.clear();
//        bar graph per date expenses view
        BarDataSet barDataSet = new BarDataSet(barEntries,"currency");
        BarData barData = new BarData(dates, barDataSet);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(barData);
        barChart.setDescription("Expenses Graph");
        barChart.setDescriptionTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        barChart.setDescriptionTextSize(9f);

//        oncliick of an bar entry
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                System.out.println("value_Y_axis"+e.getVal()+"x_index"+e.getXIndex());
                Float f1=e.getVal();

//create new intent and show data there
        Intent intent=new Intent(HomeActivity.this,bar_chart_day.class);
            intent.putExtra("yaxis_value",f1.toString());
            startActivity(intent);
            }

            @Override
            public void onNothingSelected() {
//do nothing
            }
    });
    }




    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<String>();
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                +cld.get(Calendar.DAY_OF_MONTH);
        try{
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate =  new SimpleDateFormat("yyy/MM/dd").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }
}


