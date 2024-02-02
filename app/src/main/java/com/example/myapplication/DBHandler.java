package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.SurfaceControl;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TriCoderDB";
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TRANSACTION_ID = "trans_id";
    private static final String TRANSACTION_CATEGORY = "trans_category";
    private static final String TRANSACTION_PRICE = "trans_price";
    private static final String TRANSACTION_DESCRIPTION = "trans_description";

    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + TRANSACTION_ID + " INTEGER PRIMARY KEY," + TRANSACTION_CATEGORY + " TEXT,"
                + TRANSACTION_PRICE + " REAL" + TRANSACTION_DESCRIPTION + "TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);


        // User table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_AGE + " INTEGER," + COLUMN_HEIGHT + " REAL," + COLUMN_WEIGHT + " REAL" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

        // Create tables again
        onCreate(db);
    }

    // Add new user
    public void addUser(String name, int age, float height, float weight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_WEIGHT, weight);

        db.insert(TABLE_USER, null, values);
        db.close();
    }


    User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AGE, COLUMN_HEIGHT, COLUMN_WEIGHT},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User();
        if (cursor != null && cursor.getCount() > 0) {
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
            user.setAge(Integer.parseInt(cursor.getString(2)));
            user.setHeight(Float.parseFloat(cursor.getString(3)));
            user.setWeight(Float.parseFloat(cursor.getString(4)));
        }

        if (cursor != null) {
            cursor.close();
        }

        return user;
    }

    // Get all users
    public Cursor getAllUsers() {
        String query = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

}

