package com.example.hossam.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hossam.inventory.data.InventoryContract.ProductEntry;

public class InventoryDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "inventory.db";
    private static final String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + "(" +
            ProductEntry._ID + " PRIMARY KEY AUTOINCREMENT, " +
            ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_PRODUCT_PRICE + " REAL NOT NULL DEFAULT 0.0, " +
            ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
            ProductEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
            ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL);";

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
