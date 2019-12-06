package com.example.mu338.a11_22mp3recycledb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "groupDB";
    private static final int VERSION = 1;

    // 데이터베이스 생성
    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        String str = "CREATE TABLE groupTBL (" + " celeMusicName CHAR(20) PRIMARY KEY, " + "celeName CHAR(20));";
        db.execSQL(str);
        */
        String str = "CREATE TABLE groupTBL (" + " celeMusicName CHAR(20) PRIMARY KEY, " + "celeName CHAR(20), " +
                            " albumName CHAR(20), " + " data CHAR(20), " + " genre CHAR(20), " + " albumImage BLOB);";


        db.execSQL(str);

    }

    // 테이블을 삭제하고 다시 생성함.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(db);

    }



}
