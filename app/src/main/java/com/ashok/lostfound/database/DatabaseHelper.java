package com.ashok.lostfound.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ashok.lostfound.model.LostFound;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ashok.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "lost_found_tb";
    private static final String COLUMN_POST_TYPE = "post_type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_POST_TYPE + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_LOCATION + " TEXT)";

        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //only run if upgrade in table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public long insertData(LostFound lostFound) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_TYPE, lostFound.getPostType());
        values.put(COLUMN_NAME, lostFound.getName());
        values.put(COLUMN_PHONE, lostFound.getPhone());
        values.put(COLUMN_DESCRIPTION, lostFound.getDesc());
        values.put(COLUMN_DATE, lostFound.getDate());
        values.put(COLUMN_LOCATION, lostFound.getLocation());

        return db.insert(TABLE_NAME, null, values);
    }

    public List<LostFound> getAllData() {
        List<LostFound> dataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int postTypeIndex = cursor.getColumnIndex(COLUMN_POST_TYPE);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            int locationIndex = cursor.getColumnIndex(COLUMN_LOCATION);

            do {
                // Check if the column exists in the cursor
                if (postTypeIndex >= 0 && nameIndex >= 0 && phoneIndex >= 0 && descriptionIndex >= 0
                        && dateIndex >= 0 && locationIndex >= 0) {
                    // Extract column values from the cursor
                    String postType = cursor.getString(postTypeIndex);
                    String name = cursor.getString(nameIndex);
                    String phone = cursor.getString(phoneIndex);
                    String description = cursor.getString(descriptionIndex);
                    String date = cursor.getString(dateIndex);
                    String location = cursor.getString(locationIndex);

                    // Create a new instance of YourModelClass and set the values
                    LostFound data = new LostFound(postType,name,phone,description,date,location);


                    // Add the data to the list
                    dataList.add(data);
                }
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return dataList;
    }

    public boolean deleteItemFromDatabase(String itemName, String date) {
        boolean deleted = false;
        SQLiteDatabase db = this.getWritableDatabase();

        // Establish database connection
        // Construct the delete query
        String deleteQuery = COLUMN_NAME + " = ? AND " +
                COLUMN_DATE + " = ?";
        String[] whereArgs = {itemName, date};

        // Create a prepared statement with the delete query
        int rowsAffected = db.delete(TABLE_NAME, deleteQuery, whereArgs);
        if (rowsAffected > 0) {
            deleted = true;
        }

        db.close();
        return deleted;

    }
}
