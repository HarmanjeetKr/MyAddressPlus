package com.example.harmanjeet.a2_myaddressplus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressTableHandler {

    public static final String TABLE_MYADDR = "myaddress";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INTIALS = "intials";
    public static final String COLUMN_FNAME = "fname";
    public static final String COLUMN_LNAME = "lname";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PROVINCE = "prov";
    public static final String COLUMN_COUNTRY = "cntry";
    public static final String COLUMN_PCODE = "pcode";


       private static final String DATABASE_CREATE = " create table "
               + TABLE_MYADDR
               +"("
               + COLUMN_ID + " integer primary key autoincrement, "
               + COLUMN_INTIALS + " text not null, "
               + COLUMN_FNAME + " text not null, "
               + COLUMN_LNAME + " text not null, "
               + COLUMN_ADDRESS + " text not null, "
               + COLUMN_PROVINCE + " text not null, "
               + COLUMN_COUNTRY + " text not null, "
               + COLUMN_PCODE + " text not null "
               + ");";

    public static void onCreate (SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade (SQLiteDatabase database, int oldVersion,
                                  int newVersion){
        Log.w(AddressTableHandler.class.getName(), "Upgrading database from version "
        + oldVersion + "to" + newVersion + "which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MYADDR);
        onCreate(database);
    }

}
