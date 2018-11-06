package com.rijalasepnugroho.absenapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.rijalasepnugroho.absenapp.helper.Constant;
import com.rijalasepnugroho.absenapp.helper.SessionManager;

public class DashboardActivity extends AppCompatActivity {

    public static boolean hasLogin = false;
    Button btnAbsen;
    SessionManager sessionManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setTitle("Dashboard");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                SessionManager.clearToken(this);
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
