package com.example.theon.expensetracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ExpenseTracker.db";
    public static final String TABLE_NAME = "expenses";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "BANK";
    public static final String COL_3 = "LOCATION";
    public static final String COL_4 = "DATE";
    public static final String COL_5 = "COST";
    public static final String COL_6 = "CATEGORY";
    public static final String COL_7 = "TYPE";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +" " +
                "(" +
                ""+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                ""+COL_2+" TEXT," +
                ""+COL_3+" TEXT," +
                ""+COL_4+" TEXT," +
                ""+COL_5+" TEXT," +
                ""+COL_6+" TEXT," +
                ""+COL_7+" TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String bank, String location, String date, String cost, String category, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, bank);
        contentValues.put(COL_3, location);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, cost);
        contentValues.put(COL_6, category);
        contentValues.put(COL_7, type);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME+ " ORDER BY "+COL_1+" DESC", null);
        return res;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }

    public Integer updateData(String id, String bank, String location, String date, String cost, String category, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, bank);
        contentValues.put(COL_3, location);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, cost);
        contentValues.put(COL_6, category);
        contentValues.put(COL_7, type);
        return db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }

    public Cursor getData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE " + COL_1 +"="+id,null);


    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public Cursor sortByDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " +TABLE_NAME +" ORDER BY " + COL_4 , null);
        return res;
    }

    public Cursor getBalance() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COST FROM " +TABLE_NAME + " WHERE DATE LIKE '%March%'" , null);
        return res;
    }

    public Cursor getExpenseByCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " +COL_6+ ",SUM("+COL_5+") FROM " +TABLE_NAME+ " GROUP BY " +COL_6, null);

        return res;
    }

//get sum of currencies for one month
    public Cursor getMonthExpenses(String month){
//        returns sum as 0, as others
        SQLiteDatabase db = this.getWritableDatabase();
     return db.rawQuery("SELECT SUM("+COL_5+") FROM "+TABLE_NAME+" WHERE "+ COL_4+" LIKE '"+month+"%'",null);
    }

    public  Cursor getDataByCost(String cost)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_5+"='"+cost+"'",null);
    }

//    public Cursor getCost(String cost) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_5+"='"+cost+"'",null);
//
//    }

}


