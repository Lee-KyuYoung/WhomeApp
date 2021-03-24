package com.lky.whome.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;


import androidx.appcompat.app.AppCompatActivity;

import com.lky.whome.R;

public class NetworkUtil {

    //네트워크 연결 체크
    public static void networkStateAlert(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected())){
            new AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.NO_CONNECTED_NETWORK))
                    .setCancelable(false)
                    .setPositiveButton("확인", (dialog, which) -> ((AppCompatActivity)context).finishAffinity())
                    .create()
                    .show();
        }
    }
}
