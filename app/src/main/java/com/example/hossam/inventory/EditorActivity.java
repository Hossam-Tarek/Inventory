package com.example.hossam.inventory;

import android.content.ContentValues;
import android.content.Intent;
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

    private String mProductName;
    private double mProductPrice;
    private int mProductQuantity;
    private String mSupplierName;
    private String mSupplierPhoneNumber;

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
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, mProductName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, mProductPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mProductQuantity);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, mSupplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, mSupplierPhoneNumber);

        SQLiteDatabase database = mInventoryDbHelper.getWritableDatabase();
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "New product added", Toast.LENGTH_SHORT).show();
        }
    }

    private String getInput(TextInputLayout textInputLayout) {
        return textInputLayout.getEditText().getText().toString().trim();
    }

    private boolean isInputEmpty(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean validateProductName() {
        String productNameText = getInput(mProductNameTextInput);

        if (isInputEmpty(productNameText)) {
            mProductNameTextInput.setError(getString(R.string.err_msg_product_name));
            return false;
        } else if (productNameText.length() > 50) {
            mProductNameTextInput.setError(getString(R.string.err_msg_text_too_long));
            return false;
        }

        mProductName = productNameText;
        mProductNameTextInput.setError(null);
        return true;
    }

    private boolean validateProductPrice() {
        String productPriceText = getInput(mProductPriceTextInput);

        if (isInputEmpty(productPriceText)) {
            mProductPriceTextInput.setError(getString(R.string.err_msg_product_price));
            return false;
        }

        try {
            mProductPrice = Double.parseDouble(productPriceText);
        } catch (NumberFormatException e) {
            mProductPriceTextInput.setError(getString(R.string.err_msg_non_valid_number));
            return false;
        }

        mProductPriceTextInput.setError(null);
        return true;
    }

    private boolean validateProductQuantity() {
        String productQuantityText = getInput(mProductQuantityTextInput);

        if (isInputEmpty(productQuantityText)) {
            mProductQuantityTextInput.setError(getString(R.string.err_msg_product_quantity));
            return false;
        }

        try {
            mProductQuantity = Integer.parseInt(productQuantityText);
        } catch (NumberFormatException e) {
            mProductQuantityTextInput.setError(getString(R.string.err_msg_non_valid_number));
            return false;
        }

        mProductQuantityTextInput.setError(null);
        return true;
    }

    private boolean validateSupplierName() {
        String supplierNameText = getInput(mSupplierNameTextInput);

        if (isInputEmpty(supplierNameText)) {
            mSupplierNameTextInput.setError(getString(R.string.err_msg_supplier_name));
            return false;
        } else if (supplierNameText.length() > 50) {
            mSupplierNameTextInput.setError(getString(R.string.err_msg_text_too_long));
            return false;
        }

        mSupplierName = supplierNameText;
        mSupplierNameTextInput.setError(null);
        return true;
    }

    private boolean validateSupplierPhoneNumber() {
        String supplierPhoneNumberText = getInput(mSupplierPhoneNumberTextInput);

        if (isInputEmpty(supplierPhoneNumberText)) {
            mSupplierPhoneNumberTextInput.setError(getString(R.string.err_msg_supplier_phone_number));
            return false;
        }

        mSupplierPhoneNumber = supplierPhoneNumberText;
        mSupplierPhoneNumberTextInput.setError(null);
        return true;
    }

    private boolean checkInput() {
        if (validateProductName() & validateProductPrice() & validateProductQuantity() &
                validateSupplierName() & validateSupplierPhoneNumber()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (checkInput()) {
                insertProduct();
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
