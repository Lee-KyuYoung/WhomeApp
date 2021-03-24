package com.lky.whome.toast;

import android.app.Activity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lky.whome.R;

public class CustomToast {

    public static void showToast(Activity activity, String msg){
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View ToastLayout = layoutInflater.inflate(R.layout.toast_layout, (ViewGroup)activity.findViewById(R.id.toast_layout));

        TextView textView = ToastLayout.findViewById(R.id.toast_msg);
        textView.setText(msg);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(ToastLayout);
        toast.show();
    }
}
