package com.dera.memoapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Yamen on 28/04/2017.
 */

public class AiivonContract {

    public static final String CONTENT_AUTHORITY = "com.example.yamen.aiivonmr";

    // Use CONTENT AUTHORITY to create the base of URI'S which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI'S)
    public static final String PATH_MEMOS = "memos";
    public static final String PATH_REPORTS = "reports";
    public static final String PATH_PROFILE = "profile";


    /* Inner classes that determine the table contents of the memos table
     */

    public final static class MemosEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMOS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MEMOS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/"+ PATH_MEMOS;

        //Table name
        public static final String MEMO_TABLE = "memos";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DETAILS = "details";

        public static final String COLUMN_SENDER = "sender";

        public static final String COLUMN_RECIPIENTS = "recipients";

        public static final String COLUMN_TIMESTAMP = "timestamp";



        public static Uri buildMemoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMemowithDate(String date){
            return CONTENT_URI.buildUpon().appendPath("date").appendPath(date).build();
        }

        public static Uri buildMemowithSender(String sender){
            return CONTENT_URI.buildUpon().appendPath("sender").appendPath(sender).build();
        }

        public static Uri buildMemowithRecipients(String recipients){
            return CONTENT_URI.buildUpon().appendPath(recipients).build();
        }

        public static String getDateFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static String getSenderFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static String getRecipientFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }


    public final static class ReportEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMOS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REPORTS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/"+ PATH_REPORTS;

        // Table name
        public static final String REPORT_TABLE = "reports";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DETAILS  = "details";

        public static final String COLUMN_SENDER = "sender";

        public static final String COLUMN_RECIPIENTS = "recipients";

        public static final String COLUMN_TIMESTAMP = "timestamp";


        public static Uri buildReportsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildReportswithDate(String date){
            return CONTENT_URI.buildUpon().appendPath("date").appendPath(date).build();
        }

        public static Uri buildReportswithSender(String sender){
            return CONTENT_URI.buildUpon().appendPath("sender").appendPath(sender).build();
        }

        public static Uri buildReportwithRecipients(String recipients){
            return CONTENT_URI.buildUpon().appendPath(recipients).build();
        }

        public static String getDateFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static String getSenderFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static String getRecipientFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }



    public final static class ProfileEntry implements BaseColumns{


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROFILE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_PROFILE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/"+ PATH_PROFILE;

        // Table name
        public static final String PROFILE_TABLE = "profile";


        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_POSITION = "position";

        public static final String COLUMN_DEPARTMENT = "department";


        public static Uri buildProfileId(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }


    }


}
