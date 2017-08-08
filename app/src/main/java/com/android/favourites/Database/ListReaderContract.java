package com.android.favourites.Database;

import android.provider.BaseColumns;

public class ListReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ListReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "list";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESC = "desc";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_COUNT = "view_count";
        public static final String COLUMN_NAME_IMGURL = "imageUrl";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                        FeedEntry.COLUMN_NAME_DESC + " TEXT," +
                        FeedEntry.COLUMN_NAME_TYPE + " TEXT," +
                        FeedEntry.COLUMN_NAME_COUNT + " TEXT," +
                        FeedEntry.COLUMN_NAME_IMGURL + " TEXT);";


        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }
}
