package com.example.mohaned.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohaned.inventoryapp.data.BookContract;

/**
 * Created by Mohaned on 2018-06-21.
 */

public class BookCursorAdapter extends CursorAdapter {

    private TextView nameField;
    private TextView suppNameField;
    private TextView suppNumField;
    private TextView quantityField;
    private TextView priceField;
    private Button decreaseButton;
    private Button increaseButton;
    private Button order;

    private static final String LOG_TAG = BookCursorAdapter.class.getSimpleName();

    public BookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        nameField = (TextView) view.findViewById(R.id.display_book_name);
        suppNameField = (TextView) view.findViewById(R.id.display_supplier_name);
        suppNumField = (TextView) view.findViewById(R.id.display_supplier_number);
        quantityField = (TextView) view.findViewById(R.id.display_quantity);
        priceField = (TextView) view.findViewById(R.id.display_price);
        decreaseButton = (Button) view.findViewById(R.id.decrease_button);
        increaseButton = (Button) view.findViewById(R.id.increase_button);
        order = (Button) view.findViewById(R.id.order_button);

        // Figure out the index of each column
        int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
        int supplierNumberColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

        // Iterate through all the returned rows in the cursor
        // Use that index to extract the String or Int value of the word
        // at the current row the cursor is on.
        final String productId = cursor.getString(idColumnIndex);
        String currentName = cursor.getString(nameColumnIndex);
        int currentPrice = cursor.getInt(priceColumnIndex);
        final int currentQuantity = cursor.getInt(quantityColumnIndex);
        String currentSupplierName = cursor.getString(supplierNameColumnIndex);
        final String currentSupplierNumber = cursor.getString(supplierNumberColumnIndex);

        // Display the values from each column of the current row in the cursor in the TextView
        nameField.setText(currentName);
        suppNameField.setText(currentSupplierName);
        suppNumField.setText(currentSupplierNumber);
        quantityField.setText(String.valueOf(currentQuantity));
        priceField.setText(String.valueOf(currentPrice));

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "decrease button clicked");

                int quantity = currentQuantity;

                if (currentQuantity <= 0) {
                    Toast.makeText(context, "product out of stock", Toast.LENGTH_SHORT).show();
                } else {
                    quantity--;
                }

                ContentValues values = new ContentValues();
                values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, quantity);

                Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, Long.parseLong(productId));
                Log.i(LOG_TAG, "current Uri with the id is: " + currentUri);

                context.getContentResolver().update(currentUri, values, null, null);
            }
        });

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "increase button Clicked");

                int quantity = currentQuantity;

                quantity++;

                ContentValues values = new ContentValues();
                values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, quantity);

                Uri currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, Long.parseLong(productId));
                Log.i(LOG_TAG, "current Uri with the id is: " + currentUri);

                context.getContentResolver().update(currentUri, values, null, null);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "contact button clicked");

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + currentSupplierNumber));

                Log.i(LOG_TAG, "the data is: " + intent.getData());
                context.startActivity(intent);
            }
        });
    }

}
