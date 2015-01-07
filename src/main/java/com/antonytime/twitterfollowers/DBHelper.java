package com.antonytime.twitterfollowers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = "myLogs";

    public DBHelper(Context context) {
        super(context, "DataBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- Created database ---");

        db.execSQL("CREATE TABLE if not exists 'followers' ('id' INTEGER PRIMARY KEY NOT NULL, 'name' STRING);");
        db.execSQL("CREATE TABLE if not exists 'unfollowers' ('id' INTEGER PRIMARY KEY NOT NULL, 'name' STRING);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
