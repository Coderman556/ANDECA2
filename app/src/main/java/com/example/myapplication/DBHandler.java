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
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

        // Create tables again
        onCreate(db);
    }

    public long addFinanceTransaction(FinanceTransaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRANSACTION_CATEGORY, transaction.getCategory());
        values.put(TRANSACTION_DESCRIPTION, transaction.getDescription());
        values.put(TRANSACTION_PRICE, transaction.getPrice());

        long newRowId = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();

        return newRowId; // Returns the ID of the newly inserted row, or -1 on error.
    }

    public FinanceTransaction getFinanceTransaction(int transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        FinanceTransaction transaction = null;

        Cursor cursor = db.query(
                TABLE_TRANSACTIONS,
                null,
                TRANSACTION_ID + "=?",
                new String[] { String.valueOf(transactionId) },
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(TRANSACTION_ID));
                String category = cursor.getString(cursor.getColumnIndex(TRANSACTION_CATEGORY));
                String description = cursor.getString(cursor.getColumnIndex(TRANSACTION_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndex(TRANSACTION_PRICE));

                transaction = new FinanceTransaction(category, description, price, id);
            }
            cursor.close();
        }

        db.close();
        return transaction;
    }

    public List<FinanceTransaction> getAllFinanceTransactions() {
        List<FinanceTransaction> transactionsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(TRANSACTION_ID));
                    String category = cursor.getString(cursor.getColumnIndex(TRANSACTION_CATEGORY));
                    String description = cursor.getString(cursor.getColumnIndex(TRANSACTION_DESCRIPTION));
                    double price = cursor.getDouble(cursor.getColumnIndex(TRANSACTION_PRICE));

                    FinanceTransaction transaction = new FinanceTransaction(category, description, price, id);

                    transactionsList.add(transaction);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return transactionsList;
    }



    public int updateFinanceTransaction(FinanceTransaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRANSACTION_CATEGORY, transaction.getCategory());
        values.put(TRANSACTION_DESCRIPTION, transaction.getDescription());
        values.put(TRANSACTION_PRICE, transaction.getPrice());

        return db.update(
                TABLE_TRANSACTIONS,
                values,
                TRANSACTION_ID + " = ?",
                new String[] { String.valueOf(transaction.getId()) }
        );
    }
    public void deleteFinanceTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TABLE_TRANSACTIONS,
                TRANSACTION_ID + " = ?",
                new String[] { String.valueOf(transactionId) }
        );
        db.close();
    }

}
