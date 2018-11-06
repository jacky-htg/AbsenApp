package com.rijalasepnugroho.absenapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.rijalasepnugroho.absenapp.helper.Constant;
import com.rijalasepnugroho.absenapp.helper.SessionManager;

public class SplashScreen  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(SessionManager.getString(SplashScreen.this, Constant.TOKEN) == null){
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }else {
                    // tetap di dashboard
                    startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                    finish();
                }
            }
        },2000);
    }
}
