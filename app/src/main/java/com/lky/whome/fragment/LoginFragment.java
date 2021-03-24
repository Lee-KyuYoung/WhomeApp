package com.lky.whome.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.lky.whome.R;
import com.lky.whome.activity.BaseActivity;

import com.lky.whome.network.VolleyNetwork;
import com.lky.whome.preference.PreferenceManager;
import com.lky.whome.toast.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

public class LoginFragment extends Fragment{

    private final String TAG = LoginFragment.class.getSimpleName();
    private ViewGroup mRootView;
    private BaseActivity mActivity;

    public static LoginFragment getInstance(){
        return new LoginFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_login_layout, container, false);
        init();
        return mRootView;
    }

    private void init(){
        Toolbar toolbar = mRootView.findViewById(R.id.login_toolbar);
        mActivity = (BaseActivity) getActivity();
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle("Login");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        //하위 뷰 클릭 방지
        LinearLayout loginLayout = mRootView.findViewById(R.id.login_layout);
        loginLayout.setOnTouchListener((v, event) -> {
            return true;
        });

        setLogin();
        setUserJoinFragment();
    }

    /**
     * 로그인 처리
     */
    private void setLogin(){

        Button loginBtn = mRootView.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(v -> {

            EditText userIdET = mRootView.findViewById(R.id.user_id);
            EditText userPwET = mRootView.findViewById(R.id.user_pw);

            String userID = userIdET.getText().toString().trim();
            String userPw = userPwET.getText().toString();

            Boolean paramCheck = true;

            if(userID == null || userID.equals("")){
                CustomToast.showToast(getActivity(), getString(R.string.NULL_ID));
                paramCheck = false;
            }
            else if(userPw == null || userPw.equals("")){
                CustomToast.showToast(getActivity(), getString(R.string.NULL_PW));
                paramCheck = false;
            }
            if(paramCheck){
                ContentValues params = new ContentValues();
                params.put("user_id",userID);
                params.put("user_pw",userPw);

                StringBuilder url = new StringBuilder(getResources().getString(R.string.REAL_WHOME_REST_URL))
                        .append(getResources().getString(R.string.USER_LOGIN));

                new VolleyNetwork(getActivity()).requestPost(url.toString(), params, response->{

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String resCode = jsonObject.getString("resCode");
                        String resMessage = jsonObject.getString("resMessage");

                        if(resCode.equals("200")) {

                            JSONObject jsonUserInfo = jsonObject.getJSONObject("userInfo");
                            String userId = jsonUserInfo.getString("userId");
                            String userName = jsonUserInfo.getString("userName");
                            String userImg = jsonUserInfo.getString("userImg");

                            PreferenceManager.setAttribute(getActivity(), "userId", userId);
                            PreferenceManager.setAttribute(getActivity(), "userName", userName);
                            PreferenceManager.setAttribute(getActivity(), "userImg", userImg);

                            mActivity.userLogin();
                            mActivity.onBackPressed();
                        }
                        else{
                            CustomToast.showToast(getActivity(), resMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * 회원가입 버튼 클릭 시 이동
     */
    private void setUserJoinFragment(){

        Button joinBtn = mRootView.findViewById(R.id.join_btn);
        joinBtn.setOnClickListener(v -> {
            mActivity.replaceFragment(UserJoinFragment.getInstance(), null);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //뒤로가기 설정
        mActivity.setOnBackPressedListener(()->{
            backStack();
        });
    }

    private void backStack(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(LoginFragment.this)
                .commit();
        fragmentManager.popBackStack();
    }
}
