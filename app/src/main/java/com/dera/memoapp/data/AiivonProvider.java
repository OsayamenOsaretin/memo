package com.dera.memoapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dera.memoapp.data.AiivonContract.MemosEntry;
import com.dera.memoapp.data.AiivonContract.ProfileEntry;
import com.dera.memoapp.data.AiivonContract.ReportEntry;

import static com.dera.memoapp.data.AiivonContract.MemosEntry.MEMO_TABLE;
import static com.dera.memoapp.data.AiivonContract.ProfileEntry.PROFILE_TABLE;
import static com.dera.memoapp.data.AiivonContract.ReportEntry.REPORT_TABLE;


/**
 * Created by Yamen on 28/04/2017.
 */

public class AiivonProvider extends ContentProvider {

    private static final int MEMOS = 100;
    private static final int MEMOS_WITH_SENDER = 101;
    private static final int MEMOS_WITH_RECIPIENTS = 102;
    private static final int MEMOS_WITH_DATES = 103;
    private static final int MEMOS_WITH_ID = 104;

    private static final int REPORTS = 200;
    private static final int REPORTS_WITH_SENDER = 201;
    private static final int REPORTS_WITH_RECIPIENTS = 202;
    private static final int REPORTS_WITH_DATE = 203;
    private static final int REPORTS_WITH_ID = 204;

    private static final int PROFILE= 300;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static AiivonDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AiivonContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, AiivonContract.PATH_MEMOS, MEMOS);
        matcher.addURI(authority, AiivonContract.PATH_MEMOS + "/#", MEMOS_WITH_ID);
        matcher.addURI(authority, AiivonContract.PATH_MEMOS + "/sender/*", MEMOS_WITH_SENDER);
        matcher.addURI(authority, AiivonContract.PATH_MEMOS + "/*", MEMOS_WITH_RECIPIENTS);
        matcher.addURI(authority, AiivonContract.PATH_MEMOS + "/date/*", MEMOS_WITH_DATES);

        matcher.addURI(authority, AiivonContract.PATH_REPORTS, REPORTS);
        matcher.addURI(authority, AiivonContract.PATH_REPORTS + "/#", REPORTS_WITH_ID);
        matcher.addURI(authority, AiivonContract.PATH_REPORTS + "/sender/*", REPORTS_WITH_SENDER);
        matcher.addURI(authority, AiivonContract.PATH_REPORTS + "/*", REPORTS_WITH_RECIPIENTS);
        matcher.addURI(authority, AiivonContract.PATH_REPORTS + "/date/*", REPORTS_WITH_DATE);


        matcher.addURI(authority, AiivonContract.PATH_PROFILE, PROFILE);

        return matcher;
    }



    @Override
    public boolean onCreate() {
        mOpenHelper = new AiivonDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MEMOS:
                return AiivonContract.MemosEntry.CONTENT_TYPE;
            case MEMOS_WITH_ID:
                return AiivonContract.MemosEntry.CONTENT_ITEM_TYPE;
            case MEMOS_WITH_SENDER:
                return AiivonContract.MemosEntry.CONTENT_TYPE;
            case MEMOS_WITH_RECIPIENTS:
                return AiivonContract.MemosEntry.CONTENT_TYPE;
            case MEMOS_WITH_DATES:
                return AiivonContract.MemosEntry.CONTENT_TYPE;

            case REPORTS:
                return AiivonContract.ReportEntry.CONTENT_TYPE;
            case REPORTS_WITH_ID:
                return AiivonContract.ReportEntry.CONTENT_ITEM_TYPE;
            case REPORTS_WITH_SENDER:
                return AiivonContract.ReportEntry.CONTENT_TYPE;
            case REPORTS_WITH_RECIPIENTS:
                return AiivonContract.ReportEntry.CONTENT_TYPE;
            case REPORTS_WITH_DATE:
                return AiivonContract.ReportEntry.CONTENT_TYPE;

            case PROFILE:
                return AiivonContract.ProfileEntry.CONTENT_ITEM_TYPE;
        }

        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsUpdated;

        switch(match){
            case MEMOS:{
                rowsUpdated = db.update(
                        MEMO_TABLE,
                        values,
                        selection,
                        selectionArgs
                );

                break;
            }

            case REPORTS:{
                rowsUpdated = db.update(
                        REPORT_TABLE,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            }

            case PROFILE:{
                rowsUpdated = db.update(
                        PROFILE_TABLE,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0 ){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case MEMOS: {
                long _id = db.insert(
                        MEMO_TABLE,
                        null,
                        values
                );
                if (_id > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return AiivonContract.MemosEntry.buildMemoUri(_id);
                } else {
                    throw new android.database.SQLException("failed to insert row into uri: " + uri);
                }
            }
            case REPORTS: {
                long _id = db.insert(
                        REPORT_TABLE,
                        null,
                        values
                );
                if (_id > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return AiivonContract.ReportEntry.buildReportsUri(_id);
                } else {
                    throw new android.database.SQLException("failed to insert row into uri: " + uri);
                }
            }
            case PROFILE: {
                long _id = db.insert(
                        PROFILE_TABLE,
                        null,
                        values
                );

                if (_id > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return AiivonContract.ProfileEntry.buildProfileId(_id);
                } else {
                    throw new android.database.SQLException("failed to insert row into uri: " + uri);
                }
            }

            default:
                throw new UnsupportedOperationException("Unknown ur: " + uri);
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;

        switch(match){
            case MEMOS:{
                rowsDeleted = db.delete(
                        MEMO_TABLE,
                        selection,
                        selectionArgs
                );

                break;
            }

            case REPORTS:{
                rowsDeleted = db.delete(
                        REPORT_TABLE,
                        selection,
                        selectionArgs
                );

                break;
            }

            case PROFILE:{
                rowsDeleted = db.delete(
                        PROFILE_TABLE,
                        selection,
                        selectionArgs
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }


        if(null == selection || 0 != rowsDeleted){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsDeleted;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case MEMOS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                    MEMO_TABLE,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
                break;

            case MEMOS_WITH_DATES:
                String dates = AiivonContract.MemosEntry.getDateFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MEMO_TABLE,
                        projection,
                        AiivonContract.MemosEntry.COLUMN_TIMESTAMP + " = " + dates +" ",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MEMOS_WITH_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MEMO_TABLE,
                        projection,
                        AiivonContract.MemosEntry._ID + "=" + ContentUris.parseId(uri) + " ",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MEMOS_WITH_RECIPIENTS:
                String recipients = AiivonContract.MemosEntry.getRecipientFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MEMO_TABLE,
                        projection,
                        AiivonContract.MemosEntry.COLUMN_RECIPIENTS + " = " + recipients + " ",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MEMOS_WITH_SENDER:
                String sender = AiivonContract.MemosEntry.getSenderFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MEMO_TABLE,
                        projection,
                        AiivonContract.MemosEntry.COLUMN_SENDER + " = " + sender + " ",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            case REPORTS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        REPORT_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;

            case REPORTS_WITH_DATE:
                dates = AiivonContract.ReportEntry.getDateFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        REPORT_TABLE,
                        projection,
                        AiivonContract.ReportEntry.COLUMN_TIMESTAMP + " = " + dates +" ",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            case REPORTS_WITH_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        REPORT_TABLE,
                        projection,
                        AiivonContract.ReportEntry._ID + "=" + ContentUris.parseId(uri) + " ",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            case REPORTS_WITH_RECIPIENTS:
                recipients = AiivonContract.ReportEntry.getRecipientFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        REPORT_TABLE,
                        projection,
                        AiivonContract.ReportEntry.COLUMN_RECIPIENTS + " = " + recipients + " ",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            case REPORTS_WITH_SENDER:
                sender = AiivonContract.ReportEntry.getSenderFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        REPORT_TABLE,
                        projection,
                        AiivonContract.ReportEntry.COLUMN_SENDER + " = " + sender + " ",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }
}
