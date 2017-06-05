package com.dera.memoapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jedidiah on 07/02/2017.
 */

public class SessionManager {

    private static String IS_LOGIN = "isLogin";

    public static boolean isLoggedIn(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(IS_LOGIN, false);
    }

    public static void createUserLogin(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.apply();
    }

    public static void clearSharedPreference(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
