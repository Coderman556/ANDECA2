package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDB";
    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_AGE + " INTEGER," + COLUMN_HEIGHT + " REAL," + COLUMN_WEIGHT + " REAL" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
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

    // Get user by ID
//    public Cursor getUserById(int userId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_ID + "=?";
//        return db.rawQuery(query, new String[]{String.valueOf(userId)});
//    }

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

