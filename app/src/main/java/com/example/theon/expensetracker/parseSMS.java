package com.example.theon.expensetracker;

import android.*;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.theon.expensetracker.Database.DBHelper;

public class parseSMS extends AppCompatActivity {

    ArrayAdapter arrayAdapter;
    ArrayList<String> smsMessagesList;
    ArrayList<String> banksList;
    private static final int REQUEST_DISPLAY_SMS=4;

    public  static final int RequestPermissionCode  = 1 ;

    private DBHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsesms);


        ImageView dollar=(ImageView)findViewById(R.id.loadDollar);
        Animation rotate = AnimationUtils.loadAnimation(parseSMS.this, R.anim.rotate_move_down);
        dollar.startAnimation(rotate);

        /*Intent intent1 = new Intent(getApplicationContext(), display_sms_detail.class);
        startActivityForResult(intent1, REQUEST_DISPLAY_SMS);*/
        //SMS READING CODE
        smsMessagesList = new ArrayList<String>();
        banksList = new ArrayList<String>();
        banksList.add("Discover");
        smsMessagesList.clear();

        if (ActivityCompat.shouldShowRequestPermissionRationale(parseSMS.this, android.Manifest.permission.READ_SMS)) {
            Toast.makeText(parseSMS.this, "Permission needed to access your sms", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(parseSMS.this, new String[]{android.Manifest.permission.READ_SMS}, RequestPermissionCode);
        }

        //if (ActivityCompat.shouldShowRequestPermissionRationale(parseSMS.this, android.Manifest.permission.READ_SMS)) {
            dbHelper = new DBHelper(this);
            dbHelper.deleteAll();
            refreshSmsInbox();
        //}
        Cursor res = dbHelper.getAllData();
        if(res.getCount()==0){
            Log.d("DATABASE:","EMPTY!!!!!!!!!!!!!!!!!");
            return;
        }
        while (res.moveToNext()) {
            StringBuffer entry = new StringBuffer();
            entry.append("id"+res.getString(0)+"\n");

            entry.append("Bank : " + res.getString(1) + "\n");
            entry.append("Location : " + res.getString(2) + "\n");
            entry.append("Date : " + res.getString(3) + "\n");
            entry.append("Cost : " + res.getString(4) + "\n");
            entry.append("Category : " + res.getString(5) + "\n");
            entry.append("Type : " + res.getString(6));

            smsMessagesList.add(entry.toString());
        }

        try {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //start your activity here
                    //Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    //startActivity(intent);
                    finish();
                }

            }, 3000L);
        }
        catch (Exception e) {

        }
    }

    private void refreshSmsInbox() {

        Uri uri = Uri.parse("content://sms/inbox");

        Cursor cursor = getContentResolver().query(uri, new String[]{"_id", "address", "date", "body"}, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String id=cursor.getString(0);
            String address = cursor.getString(1);
            String date = cursor.getString(2);
            String body = cursor.getString(3);

            String[] sms_each = body.split("\\s");
            ArrayList<String> al = new ArrayList<String>(Arrays.asList(sms_each));

            String message = "";
            if(banksList.contains(sms_each[0])) {

                String bank = sms_each[0];
                String location = "";
                String date1 = "";
                String cost = "";
                String category = "";
                String type1;

                if(al.contains("at")) {
                    int i = al.indexOf("at");
                    if(i+1 < al.size()) {
                        location+=sms_each[i+1];
                    }
                    if(i+2 < al.size()) {
                        //location+=" "+sms_each[i+2];
                        if (sms_each[i+2].contains("STORE")) {
                            category = "Shopping";
                        } else if(sms_each[i+2].contains("RESTAURANT")) {
                            category = "Dining";
                        } else if(sms_each[i+2].contains("GAS")) {
                            category = "Gas";
                        } else if(sms_each[i+2] == "CINEMAS") {
                            category = "Entertainment";
                        } else {
                            category = "Others";
                        }
                    }
                }
                if(al.contains("on")) {
                    int i = al.indexOf("on");
                    if(i+1 < al.size()) {
                        date1+=sms_each[i+1];
                    }
                    if(i+2 < al.size()) {
                        date1+=" "+sms_each[i+2];
                    }
                    if(i+3 < al.size()) {
                        date1+=" "+sms_each[i+3];
                    }
                }
                for(int i=0;i<al.size();i++) {
                    if(sms_each[i].contains("$")) {
                        cost = sms_each[i].replace("$","").replace(",","");
                        break;
                    }
                }
                if(al.contains("Transaction")) {
                    type1 = "debit";
                } else {
                    type1 = "credit";
                }

                if(location != "" && date1 != "") {
                    boolean isSuccessfullyInserted = dbHelper.insertData(bank, location, date1, cost, category, type1);
                    if (isSuccessfullyInserted) {
                        Log.d("Hi",isSuccessfullyInserted + "");
                    }
                }

            }

        }
    }


    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }
//end of sms parsing code
}




