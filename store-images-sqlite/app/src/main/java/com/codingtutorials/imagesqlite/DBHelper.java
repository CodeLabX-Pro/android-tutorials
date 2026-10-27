package com.codingtutorials.imagesqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** Stores images as BLOBs (binary data) in an SQLite table. */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Images.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE images(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS images");
        onCreate(db);
    }

    public boolean insertImage(String name, byte[] image) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        return getWritableDatabase().insert("images", null, values) != -1;
    }

    /** Newest first. */
    public List<SavedImage> getImages() {
        List<SavedImage> images = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT id, name, image FROM images ORDER BY id DESC", null)) {
            while (cursor.moveToNext()) {
                images.add(new SavedImage(cursor.getLong(0), cursor.getString(1), cursor.getBlob(2)));
            }
        }
        return images;
    }

    public void deleteImage(long id) {
        getWritableDatabase().delete("images", "id=?", new String[]{String.valueOf(id)});
    }
}
