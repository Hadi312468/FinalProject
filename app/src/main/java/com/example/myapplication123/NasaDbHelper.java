package com.example.myapplication123;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NasaDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "nasa.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE = "favourites";
    public static final String COL_ID = "_id";
    public static final String COL_DATE = "date";
    public static final String COL_TITLE = "title";
    public static final String COL_URL = "url";
    public static final String COL_HDURL = "hdurl";

    public NasaDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " TEXT, " +
                COL_TITLE + " TEXT, " +
                COL_URL + " TEXT, " +
                COL_HDURL + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
