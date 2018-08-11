package com.example.hossam.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.hossam.inventory.data.InventoryContract.ProductEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    private static final String TAG = InventoryCursorAdapter.class.getSimpleName();

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView NameTextView = view.findViewById(R.id.product_name_text_view);
        TextView priceTextView = view.findViewById(R.id.price_text_view);
        TextView quantityTextView = view.findViewById(R.id.quantity_text_view);
        Button sellButton = view.findViewById(R.id.sell_button);

        String name = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
        NameTextView.setText(name);

        double price = cursor.getDouble(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
        priceTextView.setText(String.valueOf(price));

        final int quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        quantityTextView.setText(String.valueOf(quantity));

        final long id = cursor.getLong(cursor.getColumnIndex(ProductEntry._ID));
        sellButton.setFocusable(false);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity <= 0) {
                    return;
                }

                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity - 1);
                int rowsUpdated = context.getContentResolver().update(
                        ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id),
                        values,
                        null,
                        null
                );

                if (rowsUpdated == 0) {
                    Log.e(TAG, "Error with updating quantity value of the product");
                }
            }
        });
    }
}
