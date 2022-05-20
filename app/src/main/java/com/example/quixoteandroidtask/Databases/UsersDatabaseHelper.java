package com.example.quixoteandroidtask.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UsersDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mDatabase";
    private static final String TABLE_NAME = "usersTable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";

    private static final int DB_VERSION = 1001;

    public UsersDatabaseHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EMAIL + " VARCHAR, " + COLUMN_PASSWORD + " VARCHAR, "+ COLUMN_NAME + " VARCHAR, " + COLUMN_PHONE + " VARCHAR)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }

    public boolean addUser (String email,String password, String name, String phone) {
        SQLiteDatabase usersDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL,email);
        contentValues.put(COLUMN_PASSWORD,password);
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_PHONE,phone);

        if(usersDatabase.insert(TABLE_NAME, null, contentValues) != -1) {
            Log.i("rowNum", String.valueOf(usersDatabase.insert(TABLE_NAME, null, contentValues)));
            return true;
        }
        return false;
    }

    public boolean isNewUser (String data) {
        SQLiteDatabase usersDatabase = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        String query2 = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = usersDatabase.rawQuery(query,new String[]{data});
        Cursor cursor2 = usersDatabase.rawQuery(query2,new String[]{data});
        if (cursor.getCount() <=0 && cursor2.getCount() <=0 ) return true;
        return false;
    }

    public boolean isPassCorrect(String data, String password) {
        SQLiteDatabase usersDatabase = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        String query2 = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = usersDatabase.rawQuery(query,new String[]{data});
        Cursor cursor2 = usersDatabase.rawQuery(query2,new String[]{data});

        if(cursor.getCount() >0) {
            if (cursor.moveToFirst()) {
                if (cursor.getColumnIndex(COLUMN_PASSWORD) >= 0) {
                    return password.equals(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                }
            }
        } else {
            if(cursor2.moveToFirst()) {
                if (cursor2.getColumnIndex(COLUMN_PASSWORD) >= 0) {
                    return password.equals(cursor2.getString(cursor2.getColumnIndex(COLUMN_PASSWORD)));
                }
            }
        }
        return false;
    }

    public int getRowNum(String data) {
        SQLiteDatabase usersDatabase = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        String query2 = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = usersDatabase.rawQuery(query,new String[]{data});
        Cursor cursor2 = usersDatabase.rawQuery(query2,new String[]{data});

        if(cursor.getCount() >0) {
            if (cursor.moveToFirst()) {
                if (cursor.getColumnIndex(COLUMN_PASSWORD) >= 0) {
                    return cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                }
            }
        } else {
            if(cursor2.moveToFirst()) {
                if (cursor2.getColumnIndex(COLUMN_PASSWORD) >= 0) {
                    return cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                }
            }
        }
        return -1;
    }
}
