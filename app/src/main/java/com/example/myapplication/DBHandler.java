package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.SurfaceControl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static DBHandler dbHandler;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TriCoderDB";


    //Notes
    private static final String TABLE_NOTES = "notes";
    private static final String KEY_NOTE_ID = "id";
    private static final String KEY_NOTE_TITLE = "title";
    private static final String KEY_NOTE_CONT = "cont";
    private static final String KEY_NOTE_IS_FAVOURITE = "isFavourite";
    private static final String KEY_NOTE_LAST_OPENED = "lastOpened";
    private static final String KEY_NOTE_DELETED = "deleted";
    private static final DateFormat dateFormat = new SimpleDateFormat("DD-mm-yyyy HH:mm:ss");

    //Tasks
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_CONT = "cont";
    private static final String KEY_TASK_DATE = "date";

    //Finance
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

    public static DBHandler instanceOfDatabase(Context context) {
        if(dbHandler == null)
            dbHandler = new DBHandler(context);

        return dbHandler;
    }

    // Creating Tables

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE CONTACTS
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + TRANSACTION_ID + " INTEGER PRIMARY KEY," + TRANSACTION_CATEGORY + " TEXT,"
                + TRANSACTION_PRICE + " REAL," + TRANSACTION_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);


        // User table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_AGE + " INTEGER," + COLUMN_HEIGHT + " REAL," + COLUMN_WEIGHT + " REAL" + ")";
        db.execSQL(CREATE_USER_TABLE);
        //CREATE NOTES TABLE
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOTE_TITLE + " TEXT,"
                + KEY_NOTE_CONT + " TEXT,"
                + KEY_NOTE_IS_FAVOURITE + " INTEGER,"
                + KEY_NOTE_LAST_OPENED + " TEXT,"
                + KEY_NOTE_DELETED + " TEXT" + ")";
        db.execSQL(CREATE_NOTES_TABLE);

        //CREATE CALENDAR TASK TABLE
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TASK_CONT + " TEXT,"
                + KEY_TASK_DATE + " TEXT"+ ")";
        db.execSQL(CREATE_TASKS_TABLE);

    }



    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
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

    //NOTES: add note to db
    public void addNoteToDatabase(NotesNote note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NOTE_TITLE, note.getTitle());
        contentValues.put(KEY_NOTE_CONT, note.getCont());
        contentValues.put(KEY_NOTE_IS_FAVOURITE, note.getIsFavourite());
        contentValues.put(KEY_NOTE_LAST_OPENED, note.getLastOpened());
        contentValues.put(KEY_NOTE_DELETED, getStringFromDate(note.getDeleted()));

        long insertedId = sqLiteDatabase.insert(TABLE_NOTES, null, contentValues);

        Log.d("DBHandler", "Inserted note with ID: " + insertedId);

        sqLiteDatabase.close();
    }



//NOTES: Populate notes
    public void populateNoteListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NOTES, null)) {
            if (result.getCount() != 0) {
                int idIndex = result.getColumnIndex(KEY_NOTE_ID);
                int titleIndex = result.getColumnIndex(KEY_NOTE_TITLE);
                int contIndex = result.getColumnIndex(KEY_NOTE_CONT);
                int isFavouriteIndex = result.getColumnIndex(KEY_NOTE_IS_FAVOURITE);
                int lastOpenedIndex = result.getColumnIndex(KEY_NOTE_LAST_OPENED);
                int deletedIndex = result.getColumnIndex(KEY_NOTE_DELETED);

                while (result.moveToNext()) {
                    int id = result.getInt(idIndex);
                    String title = (titleIndex != -1) ? result.getString(titleIndex) : "";
                    String cont = (contIndex != -1) ? result.getString(contIndex) : "";
                    boolean isFavourite = (isFavouriteIndex != -1) && (result.getInt(isFavouriteIndex) == 1);
                    String lastOpened = (lastOpenedIndex != -1) ? result.getString(lastOpenedIndex) : "";
                    String stringDeleted = (deletedIndex != -1) ? result.getString(deletedIndex) : "";
                    Date deleted = getDateFromString(stringDeleted);

                    NotesNote note = new NotesNote(id, title, cont, isFavourite, lastOpened, deleted);
                    NotesNote.noteArrayList.add(note);
                }
            }
        }
    }


//NOTES: Update note
    public void updateNoteInDB(NotesNote note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NOTE_ID, note.getId());
        contentValues.put(KEY_NOTE_TITLE, note.getTitle());
        contentValues.put(KEY_NOTE_CONT, note.getCont());
        contentValues.put(KEY_NOTE_IS_FAVOURITE, note.getIsFavourite());
        contentValues.put(KEY_NOTE_LAST_OPENED, note.getLastOpened());
        contentValues.put(KEY_NOTE_DELETED, getStringFromDate(note.getDeleted()));

        sqLiteDatabase.update(TABLE_NOTES, contentValues, KEY_NOTE_ID + " =? ", new String[]{String.valueOf(note.getId())});
    }

    // NOTES: Delete note
    public void deleteNoteFromDB(int noteId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NOTES, KEY_NOTE_ID + " =? ", new String[]{String.valueOf(noteId)});
        sqLiteDatabase.close();

        // Remove the note from the in-memory list as well
        removeNoteFromList(noteId);
    }

    private void removeNoteFromList(int noteId) {
        for (NotesNote note : NotesNote.noteArrayList) {
            if (note.getId() == noteId) {
                NotesNote.noteArrayList.remove(note);
                break;
            }
        }
    }



    // TASKS: Add task to DB
    public void addTaskToDatabase(NotesTask task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TASK_CONT, task.getCont());
        contentValues.put(KEY_TASK_DATE, getStringFromDate(task.getDate()));

        long insertedId = sqLiteDatabase.insert(TABLE_TASKS, null, contentValues);

        Log.d("DBHandler", "Inserted task with ID: " + insertedId);

        sqLiteDatabase.close();
    }

    //TASKS: Populate tasks
    public List<NotesTask> populateTaskListArray() {
        List<NotesTask> tasks = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TASKS, null)) {
            if (result.getCount() != 0) {
                int idIndex = result.getColumnIndex(KEY_TASK_ID);
                int contIndex = result.getColumnIndex(KEY_TASK_CONT);
                int dateIndex = result.getColumnIndex(KEY_TASK_DATE);

                while (result.moveToNext()) {
                    int id = result.getInt(idIndex);
                    String cont = (contIndex != -1) ? result.getString(contIndex) : "";
                    String stringDate = (dateIndex != -1) ? result.getString(dateIndex) : "";
                    Date date = getDateFromString(stringDate);

                    NotesTask task = new NotesTask(id, cont, date);
                    tasks.add(task);

                    // Log the retrieved task
                    Log.d("DBHandler", "Retrieved task with ID: " + id);
                }
            }
        }

        return tasks;
    }

    //TASKS: Update task
    public void updateTaskInDB(NotesTask task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TASK_ID, task.getId());
        contentValues.put(KEY_TASK_CONT, task.getCont());
        contentValues.put(KEY_TASK_DATE, getStringFromDate(task.getDate()));

        sqLiteDatabase.update(TABLE_TASKS, contentValues, KEY_TASK_ID + " =? ", new String[]{String.valueOf(task.getId())});
    }



    private String getStringFromDate(Date date) {
        if(date == null)
            return null;
        return dateFormat.format(date);
    }

    private Date getDateFromString(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }



    // code to add a new note
//    void addNote(NotesNote note) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NOTE_ID, note.getId());
//        values.put(KEY_NOTE_TITLE, note.getTitle());
//        values.put(KEY_NOTE_CONT, note.getCont());
//        values.put(KEY_NOTE_IS_FAVOURITE, note.getIsFavourite() ? 1 : 0);
//        values.put(KEY_NOTE_LAST_OPENED, note.getLastOpened());
//
//        // Inserting Row
//        db.insert(TABLE_NOTES, null, values);
//        db.close(); // Closing database connection
//    }


}

