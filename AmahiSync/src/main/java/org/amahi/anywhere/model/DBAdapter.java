/*
 * This source module contains confidential and proprietary
 * information of Amahi Inc. It is not to be disclosed or used
 * except in accordance with applicable agreements. This
 * copyright notice does not evidence any actual or intended
 * publication of such source code.
 *
 * Copyright (c) 2014 Amahi. All rights reserved.
 */

package org.amahi.anywhere.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

/**
 * Created by setdosa on 9/11/13.
 */
public class DBAdapter {
    /**
     * Listing 8-1: Skeleton code for contract class constants
     */
    //The index (key) column name for use in where clauses.
    public static final String KEY_ID = "_id";

    //The name and column index of each column in your database.
    //These should be descriptive.
    public static final String KEY_FOLDER_NAME_COLUMN =
            "KEY_FOLDER_NAME_COLUMN";
    public static final String KEY_TIMESTAMP_COLUMN =
            "KEY_TIMESTAMP_COLUMN";
    public static final String KEY_FILE_LIST_COLUMN =
            "KEY_FILE_LIST_COLUMN";
    //TODO: Create public field for each column in your table.
    /***/


    // Database open/upgrade helper
    private DbOpenHelper mDbOpenHelper;

    public DBAdapter(Context context) {
        mDbOpenHelper = new DbOpenHelper(context, DbOpenHelper.DATABASE_NAME, null,
                DbOpenHelper.DATABASE_VERSION);
    }

    // Called when you no longer need access to the database.
    public void closeDatabase() {
        mDbOpenHelper.close();
    }

    public String getFolderList(String folderName) {
        /**
         * Listing 8-3: Querying a database
         */
        // Specify the result column projection. Return the minimum set
        // of columns required to satisfy your requirements.
        String[] result_columns = new String[]{
                KEY_ID, KEY_TIMESTAMP_COLUMN, KEY_FILE_LIST_COLUMN};

        // Specify the where clause that will limit our results.
        String where = KEY_FOLDER_NAME_COLUMN + "=?";//TODO: Add timestamp clause, KEY_TIMESTAMP_COLUMN + "=" + 1;


        // Replace these with valid SQL statements as necessary.
        String whereArgs[] = new String[]{folderName};
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(DbOpenHelper.DATABASE_TABLE,
                result_columns, where,
                whereArgs, groupBy, having, order);

        int FILE_LIST_COLUMN_INDEX =
                cursor.getColumnIndexOrThrow(KEY_FILE_LIST_COLUMN);

        String jsonFolderList = null;
        while (cursor.moveToNext()) {
            jsonFolderList = cursor.getString(FILE_LIST_COLUMN_INDEX);
        }
        // Close the Cursor when you've finished with it.
        cursor.close();
        return jsonFolderList;
    }

    public void addOrUpdateFolder(String folderName, String jsonFolderList, Date timeStamp) {

        if (getFolderList(folderName) == null) {
            /**
             * Listing 8-5: Inserting new rows into a database
             */
            // Create a new row of values to insert.
            ContentValues newValues = new ContentValues();

            // Assign values for each row.
            newValues.put(KEY_FOLDER_NAME_COLUMN, folderName);
            newValues.put(KEY_FILE_LIST_COLUMN, jsonFolderList);
            newValues.put(KEY_TIMESTAMP_COLUMN, timeStamp.getTime());
            // [ ... Repeat for each column / value pair ... ]

            // Insert the row into your table
            SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
            db.insert(DbOpenHelper.DATABASE_TABLE, null, newValues);
        } else {
            updateFolder(folderName, jsonFolderList, timeStamp);
        }
    }

    public void addFolder(String folderName, String jsonFolderList, Date timeStamp) {
        /**
         * Listing 8-5: Inserting new rows into a database
         */
        // Create a new row of values to insert.
        ContentValues newValues = new ContentValues();

        // Assign values for each row.
        newValues.put(KEY_FOLDER_NAME_COLUMN, folderName);
        newValues.put(KEY_FILE_LIST_COLUMN, jsonFolderList);
        newValues.put(KEY_TIMESTAMP_COLUMN, timeStamp.getTime());
        // [ ... Repeat for each column / value pair ... ]

        // Insert the row into your table
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.insert(DbOpenHelper.DATABASE_TABLE, null, newValues);
    }

    public void updateFolder(String folderName, String jsonFolderList, Date timeStamp) {
        /**
         * Listing 8-6: Updating a database row
         */
        // Create the updated row Content Values.
        ContentValues updatedValues = new ContentValues();

        // Assign values for each row.
        updatedValues.put(KEY_FILE_LIST_COLUMN, jsonFolderList);
        updatedValues.put(KEY_TIMESTAMP_COLUMN, timeStamp.getTime());

        // Specify a where clause the defines which rows should be
        // updated. Specify where arguments as necessary.
        String where = KEY_FOLDER_NAME_COLUMN + "=" + folderName;
        String whereArgs[] = null;

        // Update the row with the specified index with the new values.
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.update(DbOpenHelper.DATABASE_TABLE, updatedValues,
                where, whereArgs);
    }

//    public void deleteFolders() {
//        /**
//         * Listing 8-7: Deleting a database row
//         */
//        // Specify a where clause that determines which row(s) to delete.
//        // Specify where arguments as necessary.
//        String where = KEY_FILE_LIST_COLUMN + "=" + 0;
//        String whereArgs[] = null;
//
//        // Delete the rows that match the where clause.
//        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
//        db.delete(DbOpenHelper.DATABASE_TABLE, where, whereArgs);
//    }

    /**
     * Listing 8-2: Implementing an SQLite Open Helper
     */
    private static class DbOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "AmahiSyncDatabase.db";
        private static final String DATABASE_TABLE = "AmahiSyncFilesCache";
        private static final int DATABASE_VERSION = 1;

        // SQL Statement to create a new database.
        private static final String DATABASE_CREATE = "create table " +
                DATABASE_TABLE + " (" + KEY_ID +
                " integer primary key autoincrement, " +
                KEY_FOLDER_NAME_COLUMN + " text not null, " +
                KEY_FILE_LIST_COLUMN + " float, " +
                KEY_TIMESTAMP_COLUMN + " long);";

        public DbOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        // Called when there is a database version mismatch meaning that
        // the version of the database on disk needs to be upgraded to
        // the current version.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            // Log the version upgrade.
            Log.w("TaskDBAdapter", "Upgrading from version " +
                    oldVersion + " to " +
                    newVersion + ", which will destroy all old data");

            // Upgrade the existing database to conform to the new
            // version. Multiple previous versions can be handled by
            // comparing oldVersion and newVersion values.

            // The simplest case is to drop the old table and create a new one.
            db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
            // Create a new one.
            onCreate(db);
        }
    }
}
