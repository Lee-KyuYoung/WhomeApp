package com.lky.whome.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lky.whome.R;
import com.lky.whome.activity.BaseActivity;
import com.lky.whome.network.VolleyNetwork;
import com.lky.whome.preference.PreferenceManager;
import com.lky.whome.toast.CustomToast;
import com.lky.whome.vo.UserInfoVO;

import org.json.JSONException;
import org.json.JSONObject;

public class UserPwConfirmFragment extends Fragment {

    public static UserPwConfirmFragment getInstance(){return new UserPwConfirmFragment();}

    private BaseActivity mActivity;
    private ViewGroup mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_pw_confirm_layout, container, false);
        init();
        return mRootView;
    }

    private void init(){
        mActivity = (BaseActivity)getActivity();
        Toolbar toolbar = mRootView.findViewById(R.id.pw_confirm_toolbar);
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle("비밀번호 확인");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        LinearLayout pw_confirm_layout = mRootView.findViewById(R.id.pw_confirm_layout);
        pw_confirm_layout.setOnTouchListener((v, event) -> {
            return true;
        });

        setOnClickPwConfirm();
    }

    /**
     * 비밀번호 확인 클릭
     */
    private void setOnClickPwConfirm(){
        mRootView.findViewById(R.id.user_pw_confirm).setOnClickListener(v -> {

            EditText userPwET = mRootView.findViewById(R.id.user_pw);
            String userPw = userPwET.getText().toString();
            String userId = PreferenceManager.getAttribute(getActivity(), "userId");

            if(userPw != null && !userPw.equals("")){

                ContentValues params = new ContentValues();
                params.put("user_id",userId);
                params.put("user_pw",userPw);

                StringBuilder url = new StringBuilder(getResources().getString(R.string.REAL_WHOME_REST_URL))
                        .append(getResources().getString(R.string.USER_LOGIN));

                new VolleyNetwork(getActivity()).requestPost(url.toString(), params, (response)->{

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String resCode = jsonObject.getString("resCode");
                        String resMessage = jsonObject.getString("resMessage");

                        if(resCode.equals("200")){

                            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
                            UserInfoVO userInfoVO = new UserInfoVO();
                            userInfoVO.setUserPhone(userInfo.getString("userPhone"));
                            userInfoVO.setUserEmail(userInfo.getString("userEmail"));
                            userInfoVO.setUserImg(userInfo.getString("userImg"));
                            userInfoVO.setUserIntro(userInfo.getString("userIntro"));

                            Bundle userInfoData = new Bundle();
                            userInfoData.putSerializable("userInfo",userInfoVO);

                            mActivity.onBackPressed();
                            mActivity.replaceFragment(MyInfoFragment.getInstance(), userInfoData);
                        }
                        else{
                            CustomToast.showToast(getActivity(), resMessage);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }else{
                CustomToast.showToast(getActivity(), "비밀번호를 입력해주세요.");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setOnBackPressedListener(()->{
            backStack();
        });
    }

    private void backStack(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(this)
                .commit();
        fragmentManager.popBackStack();
    }
}
