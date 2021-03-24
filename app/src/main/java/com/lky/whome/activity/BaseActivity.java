package com.lky.whome.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.lky.whome.Listener.OnBackPressedListener;
import com.lky.whome.R;
import com.lky.whome.dialog.ProgressDialog;
import com.lky.whome.fragment.LoginFragment;
import com.lky.whome.fragment.MyInfoFragment;
import com.lky.whome.fragment.UserPwConfirmFragment;
import com.lky.whome.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public abstract class BaseActivity extends AppCompatActivity{

    protected OnBackPressedListener mOnBackPressedListener;

    private NavigationView mNavigationView;
    private ImageView mUserImg;
    private TextView mUserText;
    protected ImageView mToolbar_nav;
    protected Toolbar mToolbar;

    public static boolean IS_LOGIN = false;
    public static int POPUP_REQUEST_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
    }

    protected void setToolbar(){
        mToolbar = findViewById(R.id.base_toolbar);
        if(mToolbar != null){
            setSupportActionBar(mToolbar);
            setToolbarSetting();
        }
    }

    /**
     * 툴바 네비게이션 뷰
     */
    protected void setToolbarNav(){

        DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);

        mNavigationView = findViewById(R.id.navigation_view);
        mUserImg = mNavigationView.getHeaderView(0).findViewById(R.id.nav_user_img);
        mUserText = mNavigationView.getHeaderView(0).findViewById(R.id.login_info);

        mToolbar_nav = findViewById(R.id.toolbar_nav);
        mToolbar_nav.setVisibility(View.VISIBLE);

        mToolbar_nav.setOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.RIGHT);
        });

        //네비게이션 아이템 클릭 리스너
        mNavigationView.setNavigationItemSelectedListener(item -> {

            item.setChecked(false).setCheckable(false);
            drawerLayout.closeDrawer(Gravity.RIGHT);

            int id = item.getItemId();
            if(id == R.id.login){
                replaceFragment(LoginFragment.getInstance(), null);
            }
            else if(id == R.id.my_info){
                replaceFragment(UserPwConfirmFragment.getInstance(), null);
            }
            else if(id == R.id.logout){
                onPopupClick(getString(R.string.USER_LOGOUT), false, new ResultReceiver(new Handler()){
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        if (resultCode == RESULT_OK) {
                            ProgressDialog.showProgressDialog(BaseActivity.this);
                            userLogout();
                            new Handler().postDelayed(()->{
                                ProgressDialog.dismissProgressDialog();
                                onPopupClick(getString(R.string.LOGOUT_COMPLETE), true, null);
                            }, 500);
                        }
                    }
                });
            }
            return true;
        });
        mNavigationView.getMenu().findItem(R.id.my_info).setVisible(true);
    }

    protected void invisibleToolbar(){
        mToolbar.setVisibility(View.GONE);
    }

    abstract protected Context getContext();

    abstract protected void setToolbarSetting();

    abstract protected int getLayoutResource();

    public void setOnBackPressedListener(OnBackPressedListener listener){mOnBackPressedListener = listener;}

    public void userLogin(){
        IS_LOGIN = true;
        isLogin();
    }

    public void userLogout(){
        IS_LOGIN = false;
        isLogin();
    }

    /**
     * 로그인 상태 확인
     */
    protected void isLogin(){
        if(IS_LOGIN){
            mNavigationView.getMenu().findItem(R.id.login).setVisible(false);
            mNavigationView.getMenu().findItem(R.id.my_info).setVisible(true);
            mNavigationView.getMenu().findItem(R.id.logout).setVisible(true);

            try {
                String userImg = PreferenceManager.getAttribute(this, "userImg");
                String userId = PreferenceManager.getAttribute(this, "userId");

                if(userImg != null && !userImg.equals("null") && !userImg.equals("")) {
                    Glide.with(this).load(userImg).into(mUserImg);
                }
                mUserText.setText(userId + "님 환영합니다.");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            mNavigationView.getMenu().findItem(R.id.login).setVisible(true);
            mNavigationView.getMenu().findItem(R.id.my_info).setVisible(false);
            mNavigationView.getMenu().findItem(R.id.logout).setVisible(false);

            Glide.with(this).load(getDrawable(R.drawable.ic_baseline_account_circle_24)).into(mUserImg);
            mUserText.setText(getString(R.string.require_login));

            PreferenceManager.removeAllAttribute(this);
        }
    }

    /**
     * 프래그먼트에서 프래그먼트를 호출 못하므로 액티비티를 통해 호출하도록 한다.
     * @param fragment
     */
    public void replaceFragment(Fragment fragment, Bundle data){
        if(data != null){
            fragment.setArguments(data);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_framelayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Activity 팝업창
     * @param msg 팝업창에 띄울 내용
     * @param isOnlyConfirm true  : 확인버튼만 나오게
     *                      false : 확인버튼, 취소버튼 2개 나오게
     * @param resultReceiver callback
     */
    public void onPopupClick(String msg, boolean isOnlyConfirm, ResultReceiver resultReceiver){
        Intent intent = new Intent(this, PopupActivity.class);
        intent.putExtra("msg",msg);
        intent.putExtra("isOnlyConfirm",isOnlyConfirm);
        if(resultReceiver != null)
        intent.putExtra("resultReceiver",resultReceiver);

        startActivityForResult(intent, POPUP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == POPUP_REQUEST_CODE){
            if(resultCode == RESULT_OK){

            }
        }
    }
}

