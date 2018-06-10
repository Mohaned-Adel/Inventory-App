package com.example.mohaned.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mohaned.inventoryapp.data.BookContract;
import com.example.mohaned.inventoryapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    private BookDbHelper mDbHelper;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "THE button has been clicked");

                insertBook();
//                displayDatabaseInfo();
            }
        });

        mDbHelper = new BookDbHelper(this);

//        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        displayDatabaseInfo();
    }

    private void insertBook() {

        Log.i(LOG_TAG, "you are in the insert method");

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Log.i(LOG_TAG, "you finished from DbHelper");

        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_BOOK_NAME, "Placeholder name");
        values.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, 30);
        values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, 200);
        values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "person placeholder name");
        values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, "123-456-789");

        long newRowId = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);

        Log.i(LOG_TAG, "there is a problem with insertBook method");
    }

//    private void displayDatabaseInfo() {
//
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        String[] project = {
//                BookContract.BookEntry._ID,
//                BookContract.BookEntry.COLUMN_BOOK_NAME,
//                BookContract.BookEntry.COLUMN_BOOK_PRICE,
//                BookContract.BookEntry.COLUMN_BOOK_QUANTITY,
//                BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
//                BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE
//        };
//
//        Cursor cursor = db.query(
//                BookContract.BookEntry.TABLE_NAME,
//                project,
//                null, null,
//                null, null, null
//        );
//
//        TextView displayView = (TextView) findViewById(R.id.text_view);
//        try {
//            displayView.setText("Number of rows in Books database table: " + cursor.getCount() + " Books.\n\n");
//            displayView.append(
//                    BookContract.BookEntry._ID + " - " +
//                            BookContract.BookEntry.COLUMN_BOOK_NAME + " - " +
//                            BookContract.BookEntry.COLUMN_BOOK_PRICE + " - " +
//                            BookContract.BookEntry.COLUMN_BOOK_QUANTITY + " - " +
//                            BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME +
//                            BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + ".\n"
//            );
//
//            // Figure out the index of each column
//            int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
//            int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
//            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
//            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
//            int supplierNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
//            int supplierNumberColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);
//
//            // Iterate through all the returned rows in the cursor
//            while (cursor.moveToNext()) {
//                // Use that index to extract the String or Int value of the word
//                // at the current row the cursor is on.
//                int currentId = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                int currentPrice = cursor.getInt(priceColumnIndex);
//                int currentQuantity = cursor.getInt(quantityColumnIndex);
//                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
//                String currentSupplierNumber = cursor.getString(supplierNumberColumnIndex);
//
//                // Display the values from each column of the current row in the cursor in the TextView
//                displayView.append("\n" +
//                        currentId + " - " +
//                        currentName + " - " +
//                        currentPrice + " - " +
//                        currentQuantity + " - " +
//                        currentSupplierName + " - " +
//                        currentSupplierNumber + ".");
//            }
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid
//            cursor.close();
//        }
//
//    }
}
