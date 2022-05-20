package com.example.quixoteandroidtask.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class UsersDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mDatabase";
    
    private static final String TABLE_NAME_USERS = "usersTable";
    private static final String COLUMN_ID_USERS = "id";
    private static final String COLUMN_EMAIL_USERS = "email";
    private static final String COLUMN_PASSWORD_USERS = "password";
    private static final String COLUMN_NAME_USERS = "name";
    private static final String COLUMN_PHONE_USERS = "phone";

    private static final String TABLE_NAME_NOTES = "notesTable";
    private static final String COLUMN_ID_NOTES = "id";
    private static final String COLUMN_EMAIL_NOTES = "email";
    private static final String COLUMN_LIST_NOTES = "notes";

    private static final int DB_VERSION = 2;

    private static final String PASS = "password";
    public UsersDatabaseHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableUsers = "CREATE TABLE " + TABLE_NAME_USERS + " (" + COLUMN_ID_USERS + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EMAIL_USERS + " VARCHAR, " + COLUMN_PASSWORD_USERS + " VARCHAR, "+ COLUMN_NAME_USERS + " VARCHAR, " + COLUMN_PHONE_USERS + " VARCHAR)";
        String createTableNotes = "CREATE TABLE " + TABLE_NAME_NOTES + " (" + COLUMN_ID_NOTES + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EMAIL_NOTES + " VARCHAR, "+ COLUMN_LIST_NOTES + " VARCHAR)";
        sqLiteDatabase.execSQL(createTableUsers);
        sqLiteDatabase.execSQL(createTableNotes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTableUsers = "DROP TABLE IF EXISTS " + TABLE_NAME_USERS;
        String dropTableNotes = "DROP TABLE IF EXISTS " + TABLE_NAME_NOTES;
        sqLiteDatabase.execSQL(dropTableUsers);
        sqLiteDatabase.execSQL(dropTableNotes);
        onCreate(sqLiteDatabase);
    }

    public boolean addUser (String email,String password, String name, String phone) {
        SQLiteDatabase usersDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL_USERS,email);
        contentValues.put(COLUMN_PASSWORD_USERS,password);
        contentValues.put(COLUMN_NAME_USERS,name);
        contentValues.put(COLUMN_PHONE_USERS,phone);

        if(usersDatabase.insert(TABLE_NAME_USERS, null, contentValues) != -1) {
            Log.i("rowNum", String.valueOf(usersDatabase.insert(TABLE_NAME_USERS, null, contentValues)));
            return true;
        }
        return false;
    }

    public boolean isNewUser (String data) {
        SQLiteDatabase usersDatabase = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_EMAIL_USERS + " = ?";
        String query2 = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_PHONE_USERS + " = ?";
        Cursor cursor = usersDatabase.rawQuery(query,new String[]{data});
        Cursor cursor2 = usersDatabase.rawQuery(query2,new String[]{data});
        if (cursor.getCount() <=0 && cursor2.getCount() <=0 ) return true;
        return false;
    }

    public boolean isPassCorrect(String data, String password) {
        SQLiteDatabase usersDatabase = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_EMAIL_USERS + " = ?";
        String query2 = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_PHONE_USERS + " = ?";
        Cursor cursor = usersDatabase.rawQuery(query,new String[]{data});
        Cursor cursor2 = usersDatabase.rawQuery(query2,new String[]{data});

        if(cursor.getCount() >0) {
            if (cursor.moveToFirst()) {
                if (cursor.getColumnIndex(COLUMN_PASSWORD_USERS) >= 0) {
                    try {
                        return password.equals(AESCrypt.decrypt(PASS,cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD_USERS))));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if(cursor2.moveToFirst()) {
                if (cursor2.getColumnIndex(COLUMN_PASSWORD_USERS) >= 0) {
                    try {
                        return password.equals(AESCrypt.decrypt(PASS,cursor2.getString(cursor2.getColumnIndex(COLUMN_PASSWORD_USERS))));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public String getEmail(String phone) {
        SQLiteDatabase usersDatabase = this.getReadableDatabase();

        String notesQuery = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COLUMN_PHONE_USERS + " = ?";
        Cursor cursor = usersDatabase.rawQuery(notesQuery,new String[]{phone});

        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_USERS));
    }

    public boolean addNotetoDb(String email,String noteList) {
        SQLiteDatabase notesDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL_NOTES,email);
        contentValues.put(COLUMN_LIST_NOTES,noteList);

        return notesDatabase.insert(TABLE_NAME_NOTES, null, contentValues) != -1;
    }

    public String getNotesList(String email) {
        SQLiteDatabase notesDatabase = this.getReadableDatabase();

        String notesQuery = "SELECT * FROM " + TABLE_NAME_NOTES + " WHERE " + COLUMN_EMAIL_NOTES + " = ?";
        Cursor cursor = notesDatabase.rawQuery(notesQuery,new String[]{email});

        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_LIST_NOTES));
    }

    public boolean updateNote(String email,String list) {
        SQLiteDatabase notesDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL_NOTES,email);
        contentValues.put(COLUMN_LIST_NOTES,list);

        return notesDatabase.update(TABLE_NAME_NOTES,contentValues,COLUMN_EMAIL_NOTES+" = ?",new String[]{email}) != -1;
    }
}
