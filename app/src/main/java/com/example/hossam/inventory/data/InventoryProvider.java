package com.example.hossam.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.hossam.inventory.data.InventoryContract.ProductEntry;

public class InventoryProvider extends ContentProvider {

    private static final String TAG = InventoryProvider.class.getSimpleName();
    private InventoryDbHelper mDbHelper;
    private static final int PRODUCTS = 1;
    private static final int PRODUCT_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_PRODUCTS, PRODUCTS);

        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query, invalid URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Invalid URI " + uri + " with match" + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        validateProduct(values);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private void validateProduct(ContentValues values) {
        validateProductName(values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME));
        validateProductPrice(values.getAsDouble(ProductEntry.COLUMN_PRODUCT_PRICE));
        validateProductQuantity(values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        validateSupplierName(values.getAsString(ProductEntry.COLUMN_SUPPLIER_NAME));
        validateSupplierPhoneNumber(values.getAsString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER));
    }

    private void validateProductName(String productName) {
        if (TextUtils.isEmpty(productName)) {
            throw new IllegalArgumentException("product requires a name");
        }
    }

    private void validateProductPrice(Double price) {
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Product price must be a non negative value");
        }
    }

    private void validateProductQuantity(Integer quantity) {
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Product quantity must be a non negative value");
        }
    }

    private void validateSupplierName(String supplierName) {
        if (TextUtils.isEmpty(supplierName)) {
            throw new IllegalArgumentException("Product requires a supplier name");
        }
    }

    private void validateSupplierPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            throw new IllegalArgumentException("Product requires a supplier phone number");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        int rowsUpdated;
        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                rowsUpdated = updateProduct(uri, values, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsUpdated = updateProduct(uri, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update not support for " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private int updateProduct(Uri uri, ContentValues values, String selection,
                              String[] selectionArgs) {
        if (values == null || values.size() == 0) {
            return 0;
        }
        validateProductForUpdate(values);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private void validateProductForUpdate(ContentValues values) {
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            validateProductName(values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME));
        }
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) {
            validateProductPrice(values.getAsDouble(ProductEntry.COLUMN_PRODUCT_PRICE));
        }
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            validateProductQuantity(values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        }
        if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_NAME)) {
            validateSupplierName(values.getAsString(ProductEntry.COLUMN_SUPPLIER_NAME));
        }
        if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            validateSupplierPhoneNumber(values.getAsString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER));
        }
    }
}
