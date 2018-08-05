package com.example.hossam.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.hossam.inventory.data.InventoryDbHelper;
import com.example.hossam.inventory.data.InventoryContract.ProductEntry;

public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        readData();
    }

    private void readData() {
        InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase database = inventoryDbHelper.getReadableDatabase();

        String[] columns = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };

        Cursor cursor = database.query(
                ProductEntry.TABLE_NAME,
                columns,
                null, null, null, null, null
        );

        try {
            TextView debugTextView = findViewById(R.id.debug_text_view);
            debugTextView.setText("Number of rows in product table: " + cursor.getCount() + "\n\n");
            debugTextView.append( ProductEntry._ID + "-" +
                    ProductEntry.COLUMN_PRODUCT_NAME + "-" +
                    ProductEntry.COLUMN_PRODUCT_PRICE + "-" +
                    ProductEntry.COLUMN_PRODUCT_QUANTITY + "-" +
                    ProductEntry.COLUMN_SUPPLIER_NAME + "-" +
                    ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + "\n"
            );

            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                debugTextView.append( "\n" + cursor.getInt(idColumnIndex) + "-" +
                        cursor.getString(productNameColumnIndex) + "-" +
                        cursor.getDouble(productPriceColumnIndex) + "-" +
                        cursor.getInt(productQuantityColumnIndex) + "-" +
                        cursor.getString(supplierNameColumnIndex) + "-" +
                        cursor.getString(supplierPhoneNumberColumnIndex)
                );
            }
        } finally {
            cursor.close();
        }
    }
}
