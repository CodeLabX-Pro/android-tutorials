package com.codingtutorials.loginsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Stores users in a local SQLite database.
 * Passwords are never saved as typed: each one is mixed with a random "salt" and hashed,
 * so even someone who copies the database file cannot read them.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Login.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE = "Users";
    private static final String COL_USERNAME = "USERNAME";
    private static final String COL_SALT = "SALT";
    private static final String COL_HASH = "PASSWORD_HASH";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // UNIQUE makes insert fail for a username that already exists.
        db.execSQL("CREATE TABLE " + TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT UNIQUE NOT NULL, "
                + COL_SALT + " TEXT NOT NULL, "
                + COL_HASH + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    /** Returns false when the username is already taken. */
    public boolean insertData(String username, String password) {
        String salt = newSalt();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_SALT, salt);
        values.put(COL_HASH, hash(salt, password));
        long result = getWritableDatabase().insert(TABLE, null, values);
        return result != -1;
    }

    /** Returns the username when the password matches, otherwise null. */
    public String checkLogin(String username, String password) {
        String[] columns = {COL_USERNAME, COL_SALT, COL_HASH};
        try (Cursor cursor = getReadableDatabase().query(TABLE, columns, COL_USERNAME + "=?",
                new String[]{username}, null, null, null)) {
            if (!cursor.moveToFirst()) return null;
            String salt = cursor.getString(cursor.getColumnIndexOrThrow(COL_SALT));
            String stored = cursor.getString(cursor.getColumnIndexOrThrow(COL_HASH));
            boolean matches = MessageDigest.isEqual(
                    stored.getBytes(StandardCharsets.UTF_8),
                    hash(salt, password).getBytes(StandardCharsets.UTF_8));
            return matches ? cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)) : null;
        }
    }

    private static String newSalt() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return toHex(bytes);
    }

    private static String hash(String salt, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return toHex(digest.digest((salt + password).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is always available on Android", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder text = new StringBuilder();
        for (byte b : bytes) text.append(String.format("%02x", b));
        return text.toString();
    }
}
