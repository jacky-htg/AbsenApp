package com.rijalasepnugroho.absenapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText txtEmail;
    EditText txtPassword;
    SessionManager sessionManager;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPwd);
        sessionManager = new SessionManager(this);
        requestQueue = Volley.newRequestQueue(this);


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

        /*JSONObject json = new JSONObject();

        try {
            json.put("email",email);
            json.put("password",pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://apidev.theshonet.com/get-token", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("LOGIN", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.getMessage());
            }
        });
        jsonObjectRequest.setTag("VACTIVITY");

        requestQueue.add(jsonObjectRequest);
        */
        StringRequest sr = new StringRequest(Request.Method.POST,"http://apidev.theshonet.com/get-token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LOGIN", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",pwd);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Accept","application/json");
                params.put("X-Api-Key","sh0N3tAp1D3v");
                return params;
            }
        };
        requestQueue.add(sr);
/*
        if (email.matches("rijal.asep.nugroho@gmail.com") && pwd.matches("1234")) {
            sessionManager.saveSessionBoolean("hasLogin", true);
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
        else {
            Toast.makeText(this, "@string/message_error_login", Toast.LENGTH_LONG).show();
        }*/
    }
}
