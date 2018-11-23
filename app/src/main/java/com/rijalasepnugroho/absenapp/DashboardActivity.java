package com.rijalasepnugroho.absenapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    public static boolean hasLogin = false;
    Button btnAbsen;
    SessionManager sessionManager;
    private Toolbar toolbar;
    private MyDialog dlg = new MyDialog();
    private EditText editLocation;
    private String lat, lng;
    private Dialog dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setTitle("Dashboard");
        btnAbsen = findViewById(R.id.btnAbsen);
        editLocation = findViewById(R.id.editLocation);
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new MyLocationListener();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(loc != null){
                    if (isMockLocationOn(loc, DashboardActivity.this)){
                        Toast.makeText(DashboardActivity.this, "You use Fake GPS", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        showAddress(loc);
                    }
                }
            }else {
                Toast.makeText(this, "Please check permission", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void showAddress(Location loc) {
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();
        editLocation.setText(""+latitude+ " , " +longitude);
        lat = latitude +"";
        lng = longitude + "";
        sendGPS();
    }

    private void sendGPS() {
        // method to upload gps here
        dialogLoading = MyDialog.showDialog(this);
        final JSONObject job = new JSONObject();
        try {
            job.put("jwt", SessionManager.getString(this, Constant.TOKEN));
            job.put("lat", lat);
            job.put("long", lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("cek param", job.toString());

        RequestQueue queue = Server.getInstance(this).getRequestQueue();
        StringRequest sr = new StringRequest(Request.Method.POST, URL.ABSEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("PAK RIJAL", response.toString());
                    dialogLoading.dismiss();
                    JSONObject jobj = new JSONObject(response);
                    String stat = jobj.getString("status");
                    String message = jobj.getString("message");
                    if (stat.matches("Success")) {
                        dlg.showDialogString(DashboardActivity.this, message);
                    } else {
                        dlg.showDialogString(DashboardActivity.this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialogLoading.dismiss();
                    Toast.makeText(DashboardActivity.this, "gagal", Toast.LENGTH_LONG).show();
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


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
//            editLocation.setText("");
            if (isMockLocationOn(loc, DashboardActivity.this)){
                Toast.makeText(DashboardActivity.this, "You use Fake GPS", Toast.LENGTH_LONG).show();
                finish();
            }
//            Toast.makeText(
//                    getBaseContext(),
//                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.e("absen", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.e("absen", latitude);

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
            Toast.makeText(getApplicationContext(), "Kota : "+cityName, Toast.LENGTH_LONG).show();
//            editLocation.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    // user use fake gps
    public static boolean isMockLocationOn(Location location, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return location.isFromMockProvider();
        } else {
            String mockLocation = "0";
            try {
                mockLocation = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return !mockLocation.equals("0");
        }
    }



}
