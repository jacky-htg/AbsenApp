package com.rijalasepnugroho.absenapp.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.rijalasepnugroho.absenapp.R;
import com.wang.avi.AVLoadingIndicatorView;

public class MyDialog {
    public static Dialog showDialog(Context ctx) {
        // show loading animations
        Dialog dialogLoading = new Dialog(ctx);
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoading.setCancelable(false);
        dialogLoading.setContentView(R.layout.custom_loading);
        dialogLoading.show();
        AVLoadingIndicatorView avi = dialogLoading.findViewById(R.id.avi);
        avi.show();
        return dialogLoading;
    }

    public void showDialogString(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dlg_string);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
