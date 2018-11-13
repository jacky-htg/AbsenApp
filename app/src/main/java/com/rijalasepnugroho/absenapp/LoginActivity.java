package com.rijalasepnugroho.absenapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.rijalasepnugroho.absenapp.helper.MyDialog;
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
    private Dialog dialogLoading;
    private MyDialog dlg = new MyDialog();
    private String email, pwd;

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

    private void loginProses() {
        Log.e("cek", "LOGININ");
        email = txtEmail.getText().toString();
        pwd = txtPassword.getText().toString();
        if (email.isEmpty()) {
            txtEmail.setError("Email harus di isi");
            txtEmail.requestFocus();
        } else if (pwd.isEmpty()) {
            txtPassword.setError("Password harus di isi");
            txtPassword.requestFocus();
        } else {
            postData();
        }
    }

    private void postData(){
        dialogLoading = MyDialog.showDialog(this);
        final JSONObject job = new JSONObject();
        try {
            job.put("email", email);
            job.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Server.getInstance(this).getRequestQueue();
        StringRequest sr = new StringRequest(Request.Method.POST, URL.GET_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("LOGIN", response.toString());
                    dialogLoading.dismiss();
                    JSONObject jobj = new JSONObject(response);
                    String message = jobj.getString("message");
                    if (message.matches("Login Success!")) {
                        String token = jobj.getString("Token");
                        SessionManager.putString(LoginActivity.this, Constant.TOKEN, token);
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    } else {
                        dlg.showDialogString(LoginActivity.this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialogLoading.dismiss();
                    Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                Log.e("absen", error.networkResponse.data.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", Constant.BEARER + " 65B6778032156");
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
