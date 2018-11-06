package com.rijalasepnugroho.absenapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rijalasepnugroho.absenapp.helper.Constant;
import com.rijalasepnugroho.absenapp.helper.SessionManager;

public class DashboardActivity extends AppCompatActivity {

    public static boolean hasLogin = false;
    Button btnAbsen;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        sessionManager = new SessionManagerager(this);

        btnAbsen = findViewById(R.id.btnAbsen);
        btnAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absenProses();
            }
        });
    }

    private void absenProses(){
        if(SessionManager.getString(this, Constant.TOKEN) == null){
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }else {
            // tetap di dashboard
            Toast.makeText(this, "Anda sudah login", Toast.LENGTH_LONG).show();
        }

    }
}
