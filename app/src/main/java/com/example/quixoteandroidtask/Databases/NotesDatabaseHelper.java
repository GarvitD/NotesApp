package com.example.quixoteandroidtask.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NotesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mDatabase";
    private static final String TABLE_NAME = "notesTable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOTES_LIST = "notes";

    private static final int DB_VERSION = 1001;
    public NotesDatabaseHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTES_LIST + " VARCHAR)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }

    public boolean addNotetoDb(String noteList) {
        SQLiteDatabase notesDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTES_LIST,noteList);

        return notesDatabase.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public String getNotesList(int id) {
        SQLiteDatabase notesDatabase = this.getReadableDatabase();

        String notesQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = notesDatabase.rawQuery(notesQuery,new String[]{String.valueOf(id)});

        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_LIST));
    }
}
