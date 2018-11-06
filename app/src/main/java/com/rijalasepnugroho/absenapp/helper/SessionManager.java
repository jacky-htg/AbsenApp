package com.rijalasepnugroho.absenapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rijalasepnugroho.absenapp.helper.Constant;

public class SessionManager {

    public static boolean hasLogin = false;

    private static SharedPreferences getPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void putString(Context context, String key, String value) {
        getPref(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        return getPref(context).getString(key, null);
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        getPref(context).edit().putBoolean(key, value).apply();
    }

    public static Boolean getBoolean(Context context, String key) {
        return getPref(context).getBoolean(key, false);
    }
    public static void clearToken(Context context){
        getPref(context).edit().remove(Constant.TOKEN).apply();
    }

}
