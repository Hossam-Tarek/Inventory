package com.example.hossam.inventory.data;

import android.provider.BaseColumns;

public class InventoryContract {

    private InventoryContract() {}

    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "product-name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplier-name";
        public static final String COLUMN_SUPPLIER_PHONE_NUMBER = "phone-number";
    }
}
