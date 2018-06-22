package com.example.mohaned.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.mohaned.inventoryapp.data.BookContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    BookCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG_TAG, "you have clicked the listItem");
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                Uri uri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, id);
                intent.setData(uri);
                startActivity(intent);
                Log.i(LOG_TAG, "this item id is: " + id);
            }
        });

        cursorAdapter = new BookCursorAdapter(this, null);

        listView.setAdapter(cursorAdapter);

        getLoaderManager().initLoader(0, null, this);

    }

    private void insertBook() {

        Log.i(LOG_TAG, "we are in the insertBook method");

        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_BOOK_NAME, "Book name");
        values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Supplier name");
        values.put(BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, "123-456-789");
        values.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, 20);
        values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, 300);

        Log.i(LOG_TAG, "the values have created + " + values);

        Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);

        Log.i(LOG_TAG, "this is insertBook method with newUri: " + newUri);
    }

    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(BookContract.BookEntry.CONTENT_URI, null, null);
        Log.i(LOG_TAG, rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_insert_placeholder_book:
                insertBook();
                return true;
            case R.id.action_delete_all_books:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] project = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_BOOK_NAME,
                BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE,
                BookContract.BookEntry.COLUMN_BOOK_QUANTITY,
                BookContract.BookEntry.COLUMN_BOOK_PRICE
        };

        Log.i(LOG_TAG, "the string array of project has been created");

        return new CursorLoader(this, BookContract.BookEntry.CONTENT_URI, project, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
