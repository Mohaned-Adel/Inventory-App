package com.example.mohaned.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohaned.inventoryapp.data.BookContract;

/**
 * Created by Mohaned on 2018-06-19.
 */

public class AddActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = AddActivity.class.getSimpleName();

    private EditText mNameField, mSuppNameField, mSuppNumField, mQuantityField, mPriceField;

    private TextView mDecreaseClick, mIncreaseClick, mOrderClick;

    private String telNumber,
            nameValue, supplierNameValue, supplierNumValue, quantityValue, priceValue;

    private int quantity, price;

    private Uri mCurrentUri;

    private boolean mBookHasChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();

        mCurrentUri = intent.getData();

        Log.i(LOG_TAG, "this is the uri with the id: " + mCurrentUri);

        mNameField = (EditText) findViewById(R.id.book_name_field);
        mSuppNameField = (EditText) findViewById(R.id.supplier_name_field);
        mSuppNumField = (EditText) findViewById(R.id.supplier_number_field);
        mQuantityField = (EditText) findViewById(R.id.book_quantity_field);
        mPriceField = (EditText) findViewById(R.id.book_price_field);
        mDecreaseClick = (TextView) findViewById(R.id.add_decrease_text);
        mIncreaseClick = (TextView) findViewById(R.id.add_increase_text);
        mOrderClick = (TextView) findViewById(R.id.add_order_text);

        if (mCurrentUri == null) {
            setTitle("Add a Book");
            invalidateOptionsMenu();
            mDecreaseClick.setVisibility(View.GONE);
            mIncreaseClick.setVisibility(View.GONE);
            mOrderClick.setVisibility(View.GONE);
        } else {
            setTitle("Edit a Book");

            getLoaderManager().initLoader(0, null, this);
        }

        mDecreaseClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityValue = mQuantityField.getText().toString().trim();
                if (!TextUtils.isEmpty(quantityValue)) {
                    Log.i(LOG_TAG, "you clicked the minus text");
                    quantity = Integer.parseInt(quantityValue);

                    Log.i(LOG_TAG, "the quantity is: " + quantity);
                    quantity--;
                    Log.i(LOG_TAG, "the quantity is: " + quantity);

                    mQuantityField.setText(String.valueOf(quantity));
                }

            }
        });

        mIncreaseClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityValue = mQuantityField.getText().toString().trim();
                Log.i(LOG_TAG, "you clicked the increase button: " + quantityValue);
                if (!TextUtils.isEmpty(quantityValue)) {
                    Log.i(LOG_TAG, "you in the if");
                    quantity = Integer.parseInt(quantityValue);

                    quantity++;

                    mQuantityField.setText(String.valueOf(quantity));
                }
            }
        });

        mOrderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telNumber = mSuppNumField.getText().toString().trim();
                if (!TextUtils.isEmpty(telNumber)) {
                    Log.i(LOG_TAG, "order button clicked");

                    Intent orderIntent = new Intent(Intent.ACTION_DIAL);
                    orderIntent.setData(Uri.parse("tel:" + telNumber));
                    startActivity(orderIntent);
                }
            }
        });

        mNameField.setOnTouchListener(mTouchListener);
        mPriceField.setOnTouchListener(mTouchListener);
        mQuantityField.setOnTouchListener(mTouchListener);
        mSuppNumField.setOnTouchListener(mTouchListener);
        mSuppNameField.setOnTouchListener(mTouchListener);
        mDecreaseClick.setOnTouchListener(mTouchListener);
        mIncreaseClick.setOnTouchListener(mTouchListener);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mBookHasChanged = true;
            return false;
        }
    };



    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButton ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.unsaved_changes_dialog)
                .setPositiveButton(R.string.discard, discardButton)
                .setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {

        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        showUnsavedChangesDialog(discardButton);
    }

    private void saveBook() {

        Log.i(LOG_TAG, "you are in the saveBook method");

        nameValue = mNameField.getText().toString().trim();
        supplierNameValue = mSuppNameField.getText().toString().trim();
        supplierNumValue = mSuppNumField.getText().toString().trim();
        quantityValue = mQuantityField.getText().toString().trim();
        priceValue = mPriceField.getText().toString().trim();


        Log.i(LOG_TAG, "you got all the data: " + quantityValue);

        if (TextUtils.isEmpty(nameValue) && TextUtils.isEmpty(supplierNameValue) && TextUtils.isEmpty(supplierNumValue)
                && TextUtils.isEmpty(quantityValue) && TextUtils.isEmpty(priceValue)) {
            Toast.makeText(this, R.string.all_data_check, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nameValue)) {
            Log.i(LOG_TAG, "before the name Toast");
            Toast.makeText(this, R.string.book_name_check, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(supplierNumValue) || TextUtils.isEmpty(supplierNameValue)) {
            Toast.makeText(this, R.string.supp_check, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(quantityValue)) {
            Toast.makeText(this, R.string.quantity_check, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(priceValue)) {
            Toast.makeText(this, R.string.price_check, Toast.LENGTH_SHORT).show();
            return;
        }


        quantity = Integer.parseInt(quantityValue);

        if (quantity <= 0) {
            Toast.makeText(this, R.string.quantity_check, Toast.LENGTH_SHORT).show();
            return;
        }

        price = Integer.parseInt(priceValue);

        if (price <= 0) {
            Toast.makeText(this, R.string.price_check, Toast.LENGTH_SHORT).show();
        } else {
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
                    finish();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
                if (rowsAffected == 0) {
                    Toast.makeText(this, "Error with saving Book", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
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

    private void showDeleteBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.delete_book_dialog)
                .setPositiveButton(R.string.delete_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBook();
                    }
                })
                .setNegativeButton(R.string.cancel_button_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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
                Log.i(LOG_TAG, "you clicked the save button");
                saveBook();
                Log.i(LOG_TAG, "you finish the saveBook method");
                return true;
            case R.id.delete_book:
                showDeleteBookDialog();
                return true;
            case android.R.id.home:
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }

                DialogInterface.OnClickListener discard = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(AddActivity.this);
                    }
                };

                showUnsavedChangesDialog(discard);
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
