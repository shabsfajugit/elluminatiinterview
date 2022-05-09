package com.interview.elluminati.database;



import static com.interview.elluminati.activity.MainActivity.frmBottomCart;
import static com.interview.elluminati.activity.MainActivity.tvCountCart;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "elluminati.db";
    public static final String TABLE_NAME = "packitems";
    public static final String ICOL_1 = "ID";
    public static final String ICOL_2 = "PID";
    public static final String ICOL_3 = "name";
    public static final String ICOL_4 = "descc";
    public static final String ICOL_5 = "qty";
    public static final String ICOL_6 = "total";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PID TEXT , name TEXT , descc TEXT , qty TEXT ,total TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(MyCart rModel) {
        if (getID(rModel.getId(), rModel.getTotal()) == -1) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ICOL_2, rModel.getId());
            contentValues.put(ICOL_3, rModel.getName());
            contentValues.put(ICOL_4, rModel.getDesc());
            contentValues.put(ICOL_5, rModel.getQty());
            contentValues.put(ICOL_6, rModel.getTotal());
            long result = db.insert(TABLE_NAME, null, contentValues);
            if (result == -1) {
                return false;
            } else {
                Cursor resw = getAllData();
                frmBottomCart.setVisibility(View.VISIBLE);
                tvCountCart.setText("" + resw.getCount());
                return true;
            }
        } else {
            return updateData(rModel.getId(), rModel.getTotal(), rModel.getQty());
        }
    }

    @SuppressLint("Range")
    private int getID(String pid, String cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"PID"}, "PID =? AND total =? ", new String[]{pid, cost}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("PID"));
        return -1;
    }

    @SuppressLint("Range")
    public int getCard(String pid, String cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"qty"}, "PID =? AND total =? ", new String[]{pid, cost}, null, null, null, null);
        if (c.moveToFirst()) { //if the row exist then return the id
            return c.getInt(c.getColumnIndex("qty"));
        } else {
            return -1;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, String cost, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ICOL_5, status);
        db.update(TABLE_NAME, contentValues, "PID = ? AND total =?", new String[]{id, cost});
        Cursor res = getAllData();
        return true;
    }

    public void deleteCard() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    public Integer deleteRData(String id, String cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer a = db.delete(TABLE_NAME, "PID = ? AND total =?", new String[]{id, cost});
        Cursor res = getAllData();
        return a;
    }


}