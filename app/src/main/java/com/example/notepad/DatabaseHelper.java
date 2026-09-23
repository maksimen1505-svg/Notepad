package com.example.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE = "notes";
    private static final String COL_ID = "_id";
    private static final String COL_TEXT = "text";
    private static final String COL_FIRESTORE_ID = "firestore_id";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TEXT + " TEXT, " +
                COL_FIRESTORE_ID + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public long insertNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TEXT, note.getText());
        values.put(COL_FIRESTORE_ID, note.getId());
        long id = db.insert(TABLE, null, values);
        db.close();
        return id;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TEXT, note.getText());
        db.update(TABLE, values, COL_ID + "=?",
                new String[]{String.valueOf(note.getLocalId())});
        db.close();
    }

    public void deleteNote(long localId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, COL_ID + "=?", new String[]{String.valueOf(localId)});
        db.close();
    }

    public void clearAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE);
        db.close();
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, COL_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setLocalId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
                note.setText(cursor.getString(cursor.getColumnIndexOrThrow(COL_TEXT)));
                note.setId(cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRESTORE_ID)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }
}
