package com.dera.memoapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dera.memoapp.data.AiivonContract.MemosEntry;
import com.dera.memoapp.data.AiivonContract.ProfileEntry;
import com.dera.memoapp.data.AiivonContract.ReportEntry;

/**
 * Created by Yamen on 28/04/2017.
 */

public class AiivonDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =1;
    public  static final String DATABASE_NAME = "aiivon.db";

    public AiivonDbHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MEMOS_TABLE =
                "CREATE TABLE " + MemosEntry.MEMO_TABLE + " (" +
                        MemosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MemosEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MemosEntry.COLUMN_DETAILS + " TEXT NOT NULL, " +
                        MemosEntry.COLUMN_RECIPIENTS + "TEXT NOT NULL, " +
                        MemosEntry.COLUMN_SENDER + " TEXT NOT NULL," +
                        MemosEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL );";


        final String SQL_CREATE_REPORTS_TABLE =
                "CREATE TABLE " + ReportEntry.REPORT_TABLE + " (" +
                        ReportEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        ReportEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        ReportEntry.COLUMN_DETAILS + " TEXT NOT NULL, " +
                        ReportEntry.COLUMN_RECIPIENTS + "TEXT NOT NULL, " +
                        ReportEntry.COLUMN_SENDER + " TEXT NOT NULL," +
                        ReportEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL );";

        final String SQL_CREATE_PROFILE_TABLE =
                "CREATE TABLE " + ProfileEntry.PROFILE_TABLE + " (" +
                        ProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ProfileEntry.COLUMN_NAME  + " TEXT NOT NULL, " +
                        ProfileEntry.COLUMN_DEPARTMENT + " TEXT NOT NULL, " +
                        ProfileEntry.COLUMN_POSITION + " TEXT NOT NULL ); ";


        db.execSQL(SQL_CREATE_MEMOS_TABLE);
        db.execSQL(SQL_CREATE_REPORTS_TABLE);
        db.execSQL(SQL_CREATE_PROFILE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MemosEntry.MEMO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ReportEntry.REPORT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ProfileEntry.PROFILE_TABLE);
        onCreate(db);
    }


}
