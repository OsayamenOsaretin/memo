package com.dera.memoapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Jedidiah on 17/07/2016.
 */
public class CommonUtil {

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(CommonUtil.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
