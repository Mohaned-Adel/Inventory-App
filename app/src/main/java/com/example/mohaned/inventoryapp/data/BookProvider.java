package com.example.mohaned.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Mohaned on 2018-06-21.
 */

public class BookProvider extends ContentProvider {

    private static final String LOG_TAG = BookProvider.class.getSimpleName();

    private BookDbHelper mDbhelper;

    private static final int BOOKS = 100;

    private static final int BOOKS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);

        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOKS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbhelper = new BookDbHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbhelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case BOOKS_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(
                getContext().getContentResolver(),
                BookContract.BookEntry.CONTENT_URI
        );

        return cursor;
    }

    @Override
    public Uri insert( Uri uri, ContentValues values) {

        Log.i(LOG_TAG, "we are in the insert method with uri: " + uri);

        // check that the name is not null
        String name = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Book must contain a name");
        }

        // check that the quantity is not negative or null
        int quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive number not equal 0");
        }

        int price = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        if (price <= 0) {
            throw new IllegalArgumentException("price must be positive number not equal 0");
        }

        Log.i(LOG_TAG, "after the check of the name and quantity");

        final int match = sUriMatcher.match(uri);

        Log.i(LOG_TAG, "the match has been created: " + match);

        switch (match) {
            case BOOKS:
                Log.i(LOG_TAG, "here in the Books case + " + BOOKS);
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {

        Log.i(LOG_TAG, "then we move to insertBook method from case");

        SQLiteDatabase database = mDbhelper.getWritableDatabase();

        long id = database.insert(BookContract.BookEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for" + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOKS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_NAME)) {
            // check that the name is not null
            String name = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Book must contain a name");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_QUANTITY)) {
            // check that the quantity is not negative or null
            int quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity must be positive number not equal 0");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_PRICE)) {
            int price = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_PRICE);
            if (price <= 0) {
                throw new IllegalArgumentException("price must be positive number not equal 0");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbhelper.getWritableDatabase();

        int rowsUpdated = database.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted;

        SQLiteDatabase database = mDbhelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOKS_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType( Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
