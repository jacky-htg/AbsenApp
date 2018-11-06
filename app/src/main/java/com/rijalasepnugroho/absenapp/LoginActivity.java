package com.rijalasepnugroho.absenapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rijalasepnugroho.absenapp.helper.Constant;
import com.rijalasepnugroho.absenapp.helper.Server;
import com.rijalasepnugroho.absenapp.helper.SessionManager;
import com.rijalasepnugroho.absenapp.helper.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText txtEmail;
    EditText txtPassword;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        btnLogin = findViewById(R.id.btnLogin);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPwd);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProses();
            }
        });
    }

    private void loginProses(){
        final String email = txtEmail.getText().toString();
        final String pwd = txtPassword.getText().toString();

        final JSONObject job = new JSONObject();
        try {
            job.put("email", email);
            job.put("password", pwd);
        }catch (JSONException e){
            e.printStackTrace();
        }

        RequestQueue queue = Server.getInstance(this).getRequestQueue();
        StringRequest sr = new StringRequest(Request.Method.POST,URL.GET_TOKEN,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGIN", response);
                String token = response.replaceAll("[\\W]", "");
                SessionManager.putString(LoginActivity.this, Constant.TOKEN, token);
                Toast.makeText(LoginActivity.this,"Sukses", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                Log.e("absen", error.networkResponse.data.toString());
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    Log.e("absen", body);
                    Toast.makeText(LoginActivity.this,body, Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/json");
                params.put("Accept","application/json");
                params.put("X-Api-Key","sh0N3tAp1D3v");
                return params;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return job.toString().getBytes();
            }
        };
        queue.add(sr);
    }
}
