package com.example.mohaned.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohaned.inventoryapp.data.BookContract;

/**
 * Created by Mohaned on 2018-06-19.
 */

public class AddActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = AddActivity.class.getSimpleName();

    private EditText mNameField;

    private EditText mSuppNameField;

    private EditText mSuppNumField;

    private EditText mQuantityField;

    private EditText mPriceField;


    private Uri mCurrentUri;

    private boolean mBookHasChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();

        mCurrentUri = intent.getData();

        Log.i(LOG_TAG, "this is the uri with the id: " + mCurrentUri);

        if (mCurrentUri == null) {
            setTitle("Add a Book");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit a Book");

            getLoaderManager().initLoader(0, null, this);
        }

        mNameField = (EditText) findViewById(R.id.book_name_field);
        mSuppNameField = (EditText) findViewById(R.id.supplier_name_field);
        mSuppNumField = (EditText) findViewById(R.id.supplier_number_field);
        mQuantityField = (EditText) findViewById(R.id.book_quantity_field);
        mPriceField = (EditText) findViewById(R.id.book_price_field);
    }

    private void saveBook() {

        String nameValue = mNameField.getText().toString().trim();
        String supplierNameValue = mSuppNameField.getText().toString().trim();
        String supplierNumValue = mSuppNumField.getText().toString().trim();
        String quantityValue = mQuantityField.getText().toString().trim();
        String priceValue = mPriceField.getText().toString().trim();

        if (mCurrentUri == null &&
                TextUtils.isEmpty(nameValue) && TextUtils.isEmpty(supplierNameValue) &&
                TextUtils.isEmpty(supplierNumValue) && TextUtils.isEmpty(quantityValue) &&
                TextUtils.isEmpty(priceValue)) {
            return;
        }

        int quantity = 0;

        if (!TextUtils.isEmpty(quantityValue)) {
            quantity = Integer.parseInt(quantityValue);
        }

        int price = 0;
        if (!TextUtils.isEmpty(priceValue)) {
            price = Integer.parseInt(priceValue);
        }

        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_BOOK_NAME, nameValue);
        values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameValue);
        values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, supplierNumValue);
        values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, price);

        if (mCurrentUri == null) {
            Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "error with saving the book", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "the book saved successful" , Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, "Error with saving Book", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void deleteBook() {

        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error with deleting Book", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_book);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_book_action:
                saveBook();
                finish();
                return true;
            case R.id.delete_book:
                deleteBook();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_BOOK_NAME,
                BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE,
                BookContract.BookEntry.COLUMN_BOOK_QUANTITY,
                BookContract.BookEntry.COLUMN_BOOK_PRICE };

        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToNext()) {
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
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            String currentSupplierName = cursor.getString(supplierNameColumnIndex);
            String currentSupplierNumber = cursor.getString(supplierNumberColumnIndex);

            // Display the values from each column of the current row in the cursor in the TextView
            mNameField.setText(currentName);
            mSuppNameField.setText(currentSupplierName);
            mSuppNumField.setText(currentSupplierNumber);
            mQuantityField.setText(String.valueOf(currentQuantity));
            mPriceField.setText(String.valueOf(currentPrice));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameField.setText("");
        mSuppNameField.setText("");
        mSuppNumField.setText("");
        mQuantityField.setText("");
        mPriceField.setText("");
    }
}
