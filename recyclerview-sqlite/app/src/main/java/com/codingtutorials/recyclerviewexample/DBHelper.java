package com.codingtutorials.recyclerviewexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** Creates the database and holds insert, read and delete for the user list. */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Userdetails(name TEXT primary key, email TEXT, age TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Userdetails");
        onCreate(DB);
    }

    public Boolean insertuserdata(String name, String email, String age) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("age", age);
        long result = getWritableDatabase().insert("Userdetails", null, contentValues);
        return result != -1;
    }

    /** Reads every user into a list, closing the cursor when done. */
    public List<User> getdata() {
        List<User> users = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery("Select name, email, age from Userdetails order by name", null)) {
            while (cursor.moveToNext()) {
                users.add(new User(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
        return users;
    }

    public Boolean deleteuserdata(String name) {
        // delete() returns how many rows it removed, so 0 means no user with that name.
        return getWritableDatabase().delete("Userdetails", "name=?", new String[]{name}) > 0;
    }
}
