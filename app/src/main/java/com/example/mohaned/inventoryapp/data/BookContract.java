package com.example.mohaned.inventoryapp.data;

import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Mohaned on 2018-06-10.
 */

public class BookContract {

    private BookContract(){}

    public static abstract class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOK_NAME = "productName";
        public static final String COLUMN_BOOK_PRICE = "price";
        public static final String COLUMN_BOOK_QUANTITY = "quantity";
        public static final String COLUMN_BOOK_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_BOOK_SUPPLIER_PHONE = "supplierNumber";

    }
}
