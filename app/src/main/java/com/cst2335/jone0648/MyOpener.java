package com.cst2335.jone0648;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyOpener extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mylist.dbs";
    public static final String TABLE_NAME = "mylist_datas";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM1";
    public static final String COL3 = "sOrR";


    public MyOpener(ChatRoomActivity context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " ITEM1 TEXT," +"sOrR TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void onUpgrades(SQLiteDatabase db) {
        db.execSQL("DROP TABLE if EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public int getRows() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        String [] columns = {MyOpener.COL1, MyOpener.COL2,MyOpener.COL3};
        return db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
    }



}