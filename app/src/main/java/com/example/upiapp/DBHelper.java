package com.example.upiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

        DB.execSQL("create Table transactiondetails(id INTEGER primary key AUTOINCREMENT, transactiondate TEXT, transStatus TEXT,amount INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists transactiondetails");
    }

    public Boolean insertTransactionData(String transactiondate, String transStatus,int amount)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("transactiondate", transactiondate);
        contentValues.put("transStatus", transStatus);
        contentValues.put("amount", amount);
        //contentValues.put("upiId", upiId);
        long result=DB.insert("transactiondetails", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getdata ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from transactiondetails", null);
        return cursor;

    }
    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from transactiondetails");
        db.close();
    }


}

