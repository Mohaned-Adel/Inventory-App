package com.example.mohaned.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Mohaned on 2018-06-10.
 */

public class BookContract {

    private BookContract(){}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.mohaned.inventoryapp";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_BOOKS = "books";

    public static abstract class BookEntry implements BaseColumns {

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        public static final String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOK_NAME = "productName";
        public static final String COLUMN_BOOK_PRICE = "price";
        public static final String COLUMN_BOOK_QUANTITY = "quantity";
        public static final String COLUMN_BOOK_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_BOOK_SUPPLIER_PHONE = "supplierNumber";

    }
}
