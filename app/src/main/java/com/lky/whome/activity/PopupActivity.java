package com.lky.whome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.lky.whome.R;

import java.io.Serializable;

public class PopupActivity extends Activity {

    private TextView mPopupMsg;
    private ResultReceiver mResultReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // setContentView보다 먼저 호출 되어야함
        setContentView(R.layout.activity_popup);

        mPopupMsg = findViewById(R.id.popup_msg);

        Intent intent = getIntent();
        mResultReceiver = intent.getParcelableExtra("resultReceiver");
        String msg = intent.getStringExtra("msg");
        boolean isOnlyConfirm = intent.getBooleanExtra("isOnlyConfirm", true);

        if(isOnlyConfirm){
            findViewById(R.id.cancel_btn).setVisibility(View.GONE);
        }
        mPopupMsg.setText(msg);
    }

    public void mOnConfirm(View v){
        Intent intent = new Intent();
        if(mResultReceiver != null){
            mResultReceiver.send(RESULT_OK, null);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    public void mOnCancel(View v){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
