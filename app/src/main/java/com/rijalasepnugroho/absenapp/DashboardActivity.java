package com.rijalasepnugroho.absenapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    public static boolean hasLogin = false;
    Button btnAbsen;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sessionManager = new SessionManager(this);

        //if(!sessionManager.getHasLogin()){
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        //}

        btnAbsen = findViewById(R.id.btnAbsen);
        btnAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absenProses();
            }
        });


    }

    private void absenProses(){
        Toast.makeText(this, "@string/message_proses_absen", Toast.LENGTH_LONG).show();
    }
}
