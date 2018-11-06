package com.rijalasepnugroho.absenapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public static boolean hasLogin = false;

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SessionManager(Context context) {
        sp = context.getSharedPreferences("ABSEN_APP", Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSessionBoolean(String keySession, boolean value){
        spEditor.putBoolean(keySession, value);
        spEditor.commit();
    }

    public boolean getHasLogin(){
        return sp.getBoolean("hasLogin", false);
    }
}
