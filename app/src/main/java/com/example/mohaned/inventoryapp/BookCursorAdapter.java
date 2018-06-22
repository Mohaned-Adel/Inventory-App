package com.example.mohaned.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohaned.inventoryapp.data.BookContract;
import com.example.mohaned.inventoryapp.data.BookDbHelper;

/**
 * Created by Mohaned on 2018-06-21.
 */

public class BookCursorAdapter extends CursorAdapter implements View.OnClickListener {

    private TextView nameField;
    private TextView suppNameField;
    private TextView suppNumField;
    private TextView quantityField;
    private int currentQuantity;
    private TextView priceField;
    private Button decreaseButton;
    private int position;
    private long id;
    private BookDbHelper mDbHelper;
    private SQLiteDatabase database;
    private int newQuantity;

    private static final String LOG_TAG = BookCursorAdapter.class.getSimpleName();

    public BookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mDbHelper = new BookDbHelper(context);

        nameField = (TextView) view.findViewById(R.id.display_book_name);
        suppNameField = (TextView) view.findViewById(R.id.display_supplier_name);
        suppNumField = (TextView) view.findViewById(R.id.display_supplier_number);
        quantityField = (TextView) view.findViewById(R.id.display_quantity);
        priceField = (TextView) view.findViewById(R.id.display_price);
        decreaseButton = (Button) view.findViewById(R.id.decrease_button);
        position = cursor.getPosition();

        // Figure out the index of each column
        int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
        int supplierNumberColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

        // Iterate through all the returned rows in the cursor
        // Use that index to extract the String or Int value of the word
        // at the current row the cursor is on.
        String currentName = cursor.getString(nameColumnIndex);
        int currentPrice = cursor.getInt(priceColumnIndex);
        currentQuantity = cursor.getInt(quantityColumnIndex);
        String currentSupplierName = cursor.getString(supplierNameColumnIndex);
        String currentSupplierNumber = cursor.getString(supplierNumberColumnIndex);

        // Display the values from each column of the current row in the cursor in the TextView
        nameField.setText(currentName);
        suppNameField.setText(currentSupplierName);
        suppNumField.setText(currentSupplierNumber);
        quantityField.setText(String.valueOf(currentQuantity));
        priceField.setText(String.valueOf(currentPrice));

        decreaseButton.setOnClickListener(decreaseButtonClick);

        swapCursor(cursor);
    }

    private View.OnClickListener decreaseButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            id = position + 1;
            Log.i(LOG_TAG, "here you are in the decrease button click with position: " + position);
            Log.i(LOG_TAG, "here you are in the decrease button click with id: " + id);
            newQuantity = currentQuantity - 1;
            quantityField.setText(String.valueOf(newQuantity));
            Uri newUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, position);
            Log.i(LOG_TAG, "here is the new quantity: " + newQuantity);
            Log.i(LOG_TAG, "and here is the new Uri: " + newUri);

            ContentValues values = new ContentValues();
            values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, newQuantity);

            database = mDbHelper.getWritableDatabase();

            int rowsUpdated = database.update(BookContract.BookEntry.TABLE_NAME, values, null, null);
            Log.i(LOG_TAG, "rows: " + rowsUpdated);
        }
    };

    @Override
    public void onClick(View v) {

    }
}
