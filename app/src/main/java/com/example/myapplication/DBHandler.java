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
                + TRANSACTION_ID + " INTEGER PRIMARY KEY," + TRANSACTION_CATEGORY + " TEXT,"
                + TRANSACTION_PRICE + " REAL" + TRANSACTION_DESCRIPTION + "TEXT" + ")"
        );
//        CREATE TABLE something (
//                column1,
//                column2,
//                column3,
//                PRIMARY KEY (column1, column2)
//        );
        db.execSQL(
                "CREATE TABLE " + TABLE_SAVINGSANDBUDGETS + "("
                + SNB_GOALTYPE + "TEXT," + SNB_GOALPERIOD + "TEXT,"
                + SNB_GOALAMOUNT + "REAL," + "PRIMARY KEY ("
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

        String selectQuery = "SELECT " + SNB_GOALTYPE + ", MIN(periodOrder) as MinPeriodOrder "
                + "FROM ( "
                + "SELECT "
                + TABLE_SAVINGSANDBUDGETS + "." + SNB_GOALTYPE + ", "
                + "CASE " + SNB_GOALPERIOD + " "
                + "WHEN 'daily' THEN 1 "
                + "WHEN 'weekly' THEN 2 "
                + "WHEN 'monthly' THEN 3 "
                + "ELSE 4 "
                + "END as periodOrder "
                + "FROM " + TABLE_SAVINGSANDBUDGETS + " "
                + "WHERE " + SNB_GOALAMOUNT + " <> 0 "
                + ") AS orderedSavings "
                + "GROUP BY " + SNB_GOALTYPE + " "
                + "ORDER BY MinPeriodOrder;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    FinanceSnB SnB = new FinanceSnB();

                    SnB.setGoalType(cursor.getString(0));
                    SnB.setGoalPeriod(cursor.getString(1));
                    SnB.setGoalAmount(Double.parseDouble(cursor.getString(2)));

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
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    FinanceSnB SnB = new FinanceSnB();

                    SnB.setGoalType(cursor.getString(0));
                    SnB.setGoalPeriod(cursor.getString(1));
                    SnB.setGoalAmount(Double.parseDouble(cursor.getString(2)));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return SnBList;
    }

    public long addFinanceTransaction(FinanceTransaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRANSACTION_CATEGORY, transaction.getCategory());
        values.put(TRANSACTION_DESCRIPTION, transaction.getDescription());
        values.put(TRANSACTION_PRICE, transaction.getPrice());

        long newRowId = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();

        return newRowId;
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

    public List<FinanceTransaction> getAllFinanceTransactions() {
        List<FinanceTransaction> transactionsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    FinanceTransaction transaction = new FinanceTransaction();

                    transaction.setId(Integer.parseInt(cursor.getString(0)));
                    transaction.setCategory(cursor.getString(1));
                    transaction.setPrice(Double.parseDouble(cursor.getString(2)));
                    transaction.setDescription(cursor.getString(3));


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
