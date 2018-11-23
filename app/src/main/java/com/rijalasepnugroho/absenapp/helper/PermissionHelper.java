package com.rijalasepnugroho.absenapp.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.rijalasepnugroho.absenapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionHelper {


    private Activity mActivity;
    private final int REQUEST_PERMISSION = 99;
    private String TAG = "PermissionHelper";
    private PermissionListener listener;

    public PermissionHelper(Activity activity){
        mActivity = activity;
    }

    public void permissionListener (PermissionListener permissionListener){
        listener = permissionListener;
    }


    public boolean checkAndRequestPermissions(){//1. Call this to check permission. (Call this affected loop for check permission until user Approved it)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionLocation = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionLocation2 = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionLocation2 != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION);
                return false;
            }

        }

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        listener.onPermissionCheckDone();

        return true;
    }

    public void onRequestCallBack(int RequestCode,String[] permissions ,int[] grantResults){//2. call this inside onRequestPermissionsResult
        switch (RequestCode) {
            case REQUEST_PERMISSION: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "permission granted");

                        checkAndRequestPermissions();
                    } else {
                        Log.e(TAG, "Some permissions are not granted ask again ");
                        // permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
                        // show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            showDialogOK(mActivity.getString(R.string.permission_dialog),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
//                                                case DialogInterface.BUTTON_NEGATIVE:
//                                                    // proceed with logic by disabling the related features or quit the app.
//                                                    break;
                                            }
                                        }
                                    });
                        }
                        // permission is denied (and never ask again is  checked)
                        // shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(mActivity, R.string.go_to_permissions_settings, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                            intent.setData(uri);
                            mActivity.startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public interface PermissionListener{
        void onPermissionCheckDone();
    }
}
