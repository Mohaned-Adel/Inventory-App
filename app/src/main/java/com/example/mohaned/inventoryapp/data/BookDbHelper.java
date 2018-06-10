package com.example.mohaned.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohaned on 2018-06-10.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    // Name of the dataBase file
    private static final String DATABASE_NAME = "books.db";

    /**
     * Database version. If you change the database schema, you must increment the version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BookDbHelper}
     *
     * @param context of the app
     */
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("
                + BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookEntry.COLUMN_BOOK_NAME + " TEXT, "
                + BookContract.BookEntry.COLUMN_BOOK_PRICE + " INTEGER, "
                + BookContract.BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER, "
                + BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " TEXT, "
                + BookContract.BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + " TEXT);";

        // Execute the sql statement
        sqLiteDatabase.execSQL(SQL_CREATE_BOOK_TABLE);
    }

    /**
     * this is called when the database needs to be upgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
