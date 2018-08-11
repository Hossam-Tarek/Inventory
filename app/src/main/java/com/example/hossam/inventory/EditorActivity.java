package com.example.hossam.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.hossam.inventory.data.InventoryContract.ProductEntry;

public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = EditorActivity.class.getSimpleName();

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

    private Uri mProductUri;

    private int mActivityMode;
    private static final int EDIT_MODE = 0;
    private static final int SAVE_MODE = 1;

    private static final int PRODUCT_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mProductUri = intent.getData();

        if (mProductUri != null) {
            mActivityMode = EDIT_MODE;
            setTitle(getString(R.string.editor_activity_title_edit_product));
            getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        } else {
            mActivityMode = SAVE_MODE;
            setTitle(getString(R.string.editor_activity_title_new_product));
            invalidateOptionsMenu();
        }

        mProductNameTextInput = findViewById(R.id.product_name_text_input);
        mProductPriceTextInput = findViewById(R.id.product_price_text_input);
        mProductQuantityTextInput = findViewById(R.id.product_quantity_text_input);
        mSupplierNameTextInput = findViewById(R.id.supplier_name_text_input);
        mSupplierPhoneNumberTextInput = findViewById(R.id.supplier_phone_number_text_input);
    }

    private void saveProduct() {
        if (!checkInput()) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, mProductName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, mProductPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mProductQuantity);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, mSupplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, mSupplierPhoneNumber);

        switch (mActivityMode) {
            case SAVE_MODE:
                Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
                if (ContentUris.parseId(uri) == -1) {
                    Toast.makeText(this, getString(R.string.add_product_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.add_product_successful),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                int rowsUpdated = getContentResolver().update(mProductUri, values,
                        null, null);

                if (rowsUpdated != 0) {
                    Toast.makeText(this, getString(R.string.update_product_successful),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.update_product_failed),
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    private String getInput(TextInputLayout textInputLayout) {
        return textInputLayout.getEditText().getText().toString().trim();
    }

    private boolean validateProductName() {
        String productNameText = getInput(mProductNameTextInput);

        if (TextUtils.isEmpty(productNameText)) {
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

        if (TextUtils.isEmpty(productPriceText)) {
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

        if (TextUtils.isEmpty(productQuantityText)) {
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

        if (TextUtils.isEmpty(supplierNameText)) {
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

        if (TextUtils.isEmpty(supplierPhoneNumberText)) {
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
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                finish();
                return true;
            case R.id.action_delete:
                Log.v(TAG, "delete menu option clicked");
                showDeleteConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mActivityMode == SAVE_MODE) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_msg_delete_product);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteProduct();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        int rowsDeleted = getContentResolver().delete(mProductUri, null, null);
        if (rowsDeleted != 0) {
            Toast.makeText(this, getString(R.string.delete_product_successful),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.delete_product_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                mProductUri,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return;
        }

        int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
        int phoneNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

        mProductNameTextInput.getEditText()
                .setText(cursor.getString(productNameColumnIndex));
        mProductPriceTextInput.getEditText()
                .setText(String.valueOf(cursor.getDouble(priceColumnIndex)));
        mProductQuantityTextInput.getEditText()
                .setText(String.valueOf(cursor.getInt(quantityColumnIndex)));
        mSupplierNameTextInput.getEditText()
                .setText(cursor.getString(supplierNameColumnIndex));
        mSupplierPhoneNumberTextInput.getEditText()
                .setText(cursor.getString(phoneNumberColumnIndex));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductNameTextInput.getEditText().setText(null);
        mProductPriceTextInput.getEditText().setText(null);
        mProductQuantityTextInput.getEditText().setText(null);
        mSupplierNameTextInput.getEditText().setText(null);
        mSupplierPhoneNumberTextInput.getEditText().setText(null);
    }
}
