package com.dpk.hammoq.utilsNetwork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class CheckNetworkAccess {

    private static String PREFERENCES = "MyProjectPreference";
    private static ProgressDialog mProgressDialog;

    public static boolean isNetAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                }
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void setBooleanPreferences(Context context, String key, boolean isCheck) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean(key, isCheck);
        editor.commit();

    }

    public static boolean getBooleanPreferences(Context context, String key) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        return setting.getBoolean(key, false);

    }

    public static void setStringPreferences(Context context, String key, String value) {
        try {
            SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getStringPreferences(Context context, String key) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        return setting.getString(key, null);

    }

    public static void removeStringPreferences(Context context, String key) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.remove(key);
        editor.commit();

    }

    public static void setIntegerPreferences(Context context, String key, int value) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public static void clearAllSharedPreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static int getIntegerPreferences(Context context, String key) {
        SharedPreferences setting = context.getSharedPreferences(PREFERENCES, 0);
        return setting.getInt(key, 0);

    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftInput(EditText edit, Context context) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit, 0);
    }
}
