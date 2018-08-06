package com.example.hossam.inventory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hossam.inventory.data.InventoryDbHelper;
import com.example.hossam.inventory.data.InventoryContract.ProductEntry;

public class EditorActivity extends AppCompatActivity {
    private TextInputLayout mProductNameTextInput;
    private TextInputLayout mProductPriceTextInput;
    private TextInputLayout mProductQuantityTextInput;
    private TextInputLayout mSupplierNameTextInput;
    private TextInputLayout mSupplierPhoneNumberTextInput;

    private InventoryDbHelper mInventoryDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductNameTextInput = findViewById(R.id.product_name_text_input);
        mProductPriceTextInput = findViewById(R.id.product_price_text_input);
        mProductQuantityTextInput = findViewById(R.id.product_quantity_text_input);
        mSupplierNameTextInput = findViewById(R.id.supplier_name_text_input);
        mSupplierPhoneNumberTextInput = findViewById(R.id.supplier_phone_number_text_input);

        mInventoryDbHelper = new InventoryDbHelper(this);
    }

    private void insertProduct() {
        String productName = mProductNameTextInput.getEditText().getText().toString().trim();
        double productPrice = Double.parseDouble(mProductPriceTextInput.getEditText().getText().toString().trim());
        int productQuantity = Integer.parseInt(mProductQuantityTextInput.getEditText().getText().toString().trim());
        String supplierName = mSupplierNameTextInput.getEditText().getText().toString().trim();
        String supplierPhoneNumber = mSupplierPhoneNumberTextInput.getEditText().getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        SQLiteDatabase database = mInventoryDbHelper.getWritableDatabase();
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "New product added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            insertProduct();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
