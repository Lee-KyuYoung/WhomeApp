package com.lky.whome.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.lky.whome.R;

public class ProgressDialog extends Dialog {

    public static ProgressDialog mProgressDialog;
    public static boolean onGoing = false;

    public ProgressDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_dialog);
    }

    public static void showProgressDialog(Context context){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        onGoing = true;
    }
    public static void dismissProgressDialog(){
        mProgressDialog.dismiss();
        onGoing = false;
    }
 }
