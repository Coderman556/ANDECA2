package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
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

//    Caused by: android.database.sqlite.SQLiteException: no such column: snb_type (code 1 SQLITE_ERROR): , while compiling: CREATE TABLE
//    savings_and_budgets(snb_typeTEXT,snb_periodTEXT,snb_amountREAL,PRIMARY KEY (snb_type, snb_period));
    private static final String TABLE_SAVINGSANDBUDGETS = "savings_and_budgets";
    private static final String SNB_GOALTYPE = "snb_type";
    private static final String SNB_GOALPERIOD = "snb_period";
    private static final String SNB_GOALAMOUNT = "snb_amount";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + TRANSACTION_ID + " INTEGER PRIMARY KEY, " + TRANSACTION_CATEGORY + " TEXT,"
                + TRANSACTION_PRICE + " REAL," + TRANSACTION_DESCRIPTION + " TEXT" + ");"
        );
        db.execSQL(
                "CREATE TABLE " + TABLE_SAVINGSANDBUDGETS + "("
                + SNB_GOALTYPE + " TEXT," + SNB_GOALPERIOD + " TEXT,"
                + SNB_GOALAMOUNT + " REAL," + "PRIMARY KEY ("
                + SNB_GOALTYPE + ", " + SNB_GOALPERIOD + ")" + ");"
        );
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVINGSANDBUDGETS);

        // Create tables again
        onCreate(db);
    }

    public long addSnB(FinanceSnB snb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SNB_GOALTYPE, snb.getGoalType());
        values.put(SNB_GOALPERIOD, snb.getGoalPeriod());
        values.put(SNB_GOALAMOUNT, snb.getGoalAmount());

        long newRowId = db.insert(TABLE_SAVINGSANDBUDGETS, null, values);
        db.close();

        return newRowId;
    }

    public List<FinanceSnB> getDisplayableSnB(){
        List<FinanceSnB> SnBList = new ArrayList<>();

        String selectQuery = "SELECT " + SNB_GOALTYPE + ", " + SNB_GOALPERIOD + ", " + SNB_GOALAMOUNT + " "
                + "FROM " + TABLE_SAVINGSANDBUDGETS + " "
                + "WHERE " + SNB_GOALAMOUNT + " <> 0 "
                + "ORDER BY CASE " + SNB_GOALPERIOD + " "
                + "WHEN 'daily' THEN 1 "
                + "WHEN 'weekly' THEN 2 "
                + "WHEN 'monthly' THEN 3 "
                + "ELSE 4 END, "
                + SNB_GOALTYPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    FinanceSnB SnB = new FinanceSnB();
                    SnB.setGoalType(cursor.getString(0));
                    SnB.setGoalPeriod(cursor.getString(1));
                    SnB.setGoalAmount(cursor.getDouble(2));
                    SnBList.add(SnB);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return SnBList;
    }

    public List<FinanceSnB> getAllSnB(){
        List<FinanceSnB> SnBList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SAVINGSANDBUDGETS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    FinanceSnB SnB = new FinanceSnB();

                    SnB.setGoalType(cursor.getString(0));
                    SnB.setGoalPeriod(cursor.getString(1));
                    SnB.setGoalAmount(cursor.getDouble(2));

                    SnBList.add(SnB);
                } while (cursor.moveToNext());
            }
        if (!cursor.isClosed())
            cursor.close();

        db.close();
        return SnBList;
    }



    public int updateSnB(FinanceSnB snb) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SNB_GOALTYPE, snb.getGoalType());
        values.put(SNB_GOALPERIOD, snb.getGoalPeriod());
        values.put(SNB_GOALAMOUNT, snb.getGoalAmount());

        // Assuming you have a unique ID (like a primary key) to identify the row to update
        // If your table uses the goalType and goalPeriod as a composite primary key, then your where clause should match both
        String selection = SNB_GOALTYPE + " = ? AND " + SNB_GOALPERIOD + " = ?";
        String[] selectionArgs = { snb.getGoalType(), snb.getGoalPeriod() };

        int count = db.update(
                TABLE_SAVINGSANDBUDGETS,
                values,
                selection,
                selectionArgs);

        if (count == 1) {
            Log.d("DBHandler", "SnB updated successfully.");
        } else {
            Log.e("DBHandler", "SnB update failed.");
        }

        db.close();
        return count;
    }

    public int deleteSnB(String goalType, String goalPeriod) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define 'where' part of query.
        String selection = SNB_GOALTYPE + " = ? AND " + SNB_GOALPERIOD + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { goalType, goalPeriod };

        // Issue SQL statement.
        int deletedRows = db.delete(TABLE_SAVINGSANDBUDGETS, selection, selectionArgs);

        db.close();
        return deletedRows;
    }

    public long addFinanceTransaction(FinanceTransaction transaction) {
        // Try-with-resources statement automatically closes the database object
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(TRANSACTION_CATEGORY, transaction.getCategory());
            values.put(TRANSACTION_DESCRIPTION, transaction.getDescription());
            values.put(TRANSACTION_PRICE, transaction.getPrice());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TABLE_TRANSACTIONS, null, values);

            return newRowId;
        }
        // No need for db.close() as it's automatically managed by try-with-resources
    }


    public FinanceTransaction getFinanceTransaction(int transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        FinanceTransaction transaction = null;

        Cursor cursor = db.query(
                TABLE_TRANSACTIONS,
                new String[] {
                        TRANSACTION_ID,
                        TRANSACTION_CATEGORY,
                        TRANSACTION_PRICE,
                        TRANSACTION_DESCRIPTION
                },
                TRANSACTION_ID + "=?",
                new String[] {
                        String.valueOf(transactionId)
                },
                null,
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();
//        public FinanceTransaction(String cat, String desc, double price, int id) {
        FinanceTransaction txn = new FinanceTransaction(cursor.getString(1), cursor.getString(3), Double.parseDouble(cursor.getString(2)), Integer.parseInt(cursor.getString(0)));

        return txn;
    }

    public List<FinanceTransaction> searchFinanceTransactions(String searchText) {
        List<FinanceTransaction> transactionsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTIONS +
                " WHERE " + TRANSACTION_CATEGORY + " LIKE ? OR " +
                TRANSACTION_DESCRIPTION + " LIKE ?";
        String[] args = new String[]{"%" + searchText + "%", "%" + searchText + "%"};

        Cursor cursor = db.rawQuery(query, args);

        if (cursor.moveToFirst()) {
            do {
                FinanceTransaction transaction = new FinanceTransaction();
                transaction.setId(cursor.getInt(0)); // TRANSACTION_ID
                transaction.setCategory(cursor.getString(1)); // TRANSACTION_CATEGORY
                transaction.setPrice(cursor.getDouble(2)); // TRANSACTION_PRICE
                transaction.setDescription(cursor.getString(3)); // TRANSACTION_DESCRIPTION

                transactionsList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return transactionsList;
    }

    public ArrayList<FinanceTransaction> getAllFinanceTransactions() {
        ArrayList<FinanceTransaction> transactionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS, null);
            if (cursor.moveToFirst()) {
                do {
                    FinanceTransaction transaction = new FinanceTransaction();
                    transaction.setId(cursor.getInt(0)); // TRANSACTION_ID
                    transaction.setCategory(cursor.getString(1)); // TRANSACTION_CATEGORY
                    transaction.setPrice(cursor.getDouble(2)); // TRANSACTION_PRICE
                    transaction.setDescription(cursor.getString(3)); // TRANSACTION_DESCRIPTION
                    transactionsList.add(transaction);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return transactionsList;
    }

    public int updateFinanceTransaction(FinanceTransaction transaction) {
        // Try-with-resources statement automatically closes the database object
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(TRANSACTION_CATEGORY, transaction.getCategory());
            values.put(TRANSACTION_DESCRIPTION, transaction.getDescription());
            values.put(TRANSACTION_PRICE, transaction.getPrice());

            // Update the database and get the number of rows affected
            int count = db.update(
                    TABLE_TRANSACTIONS,
                    values,
                    TRANSACTION_ID + " = ?",
                    new String[] { String.valueOf(transaction.getId()) }
            );

            return count;
        }
        // No need for db.close() as it's automatically managed by try-with-resources
    }



    public int updateFinanceTransaction(FinanceTransaction transaction) {
        // Try-with-resources statement automatically closes the database object
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(TRANSACTION_CATEGORY, transaction.getCategory());
            values.put(TRANSACTION_DESCRIPTION, transaction.getDescription());
            values.put(TRANSACTION_PRICE, transaction.getPrice());

            // Update the database and get the number of rows affected
            int count = db.update(
                    TABLE_TRANSACTIONS,
                    values,
                    TRANSACTION_ID + " = ?",
                    new String[] { String.valueOf(transaction.getId()) }
            );

            return count;
        }
        // No need for db.close() as it's automatically managed by try-with-resources
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
