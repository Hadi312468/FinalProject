package com.example.myapplication123;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "todos.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_TODO = "todos";
    public static final String COL_ID = "_id";
    public static final String COL_TEXT = "text";
    public static final String COL_URGENT = "urgent";

    private static final String CREATE_SQL =
            "CREATE TABLE " + TABLE_TODO + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TEXT + " TEXT NOT NULL, " +
                    COL_URGENT + " INTEGER NOT NULL DEFAULT 0" +
                    ");";

    public TodoDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }
}
