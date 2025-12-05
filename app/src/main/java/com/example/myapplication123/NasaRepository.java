package com.example.myapplication123;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NasaRepository {

    private final NasaDbHelper helper;

    public NasaRepository(Context context) {
        helper = new NasaDbHelper(context);
    }

    public long insert(NasaImage img) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NasaDbHelper.COL_DATE, img.getDate());
        cv.put(NasaDbHelper.COL_TITLE, img.getTitle());
        cv.put(NasaDbHelper.COL_URL, img.getUrl());
        cv.put(NasaDbHelper.COL_HDURL, img.getHdUrl());
        long id = db.insert(NasaDbHelper.TABLE, null, cv);
        img.setId(id);
        return id;
    }

    public void delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(NasaDbHelper.TABLE, NasaDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public List<NasaImage> getAll() {
        List<NasaImage> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(NasaDbHelper.TABLE, null,
                null, null, null, null,
                NasaDbHelper.COL_ID + " DESC");
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(NasaDbHelper.COL_ID));
            String date = c.getString(c.getColumnIndexOrThrow(NasaDbHelper.COL_DATE));
            String title = c.getString(c.getColumnIndexOrThrow(NasaDbHelper.COL_TITLE));
            String url = c.getString(c.getColumnIndexOrThrow(NasaDbHelper.COL_URL));
            String hd = c.getString(c.getColumnIndexOrThrow(NasaDbHelper.COL_HDURL));
            list.add(new NasaImage(id, date, title, url, hd));
        }
        c.close();
        return list;
    }
}
