package com.lky.whome.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.lky.whome.R;
import com.lky.whome.activity.BaseActivity;
import com.lky.whome.dialog.ProgressDialog;
import com.lky.whome.network.VolleyMultipart;
import com.lky.whome.network.VolleyNetwork;
import com.lky.whome.preference.PreferenceManager;
import com.lky.whome.toast.CustomToast;
import com.lky.whome.utils.GalleryImgUtil;
import com.lky.whome.vo.UserInfoVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MyInfoFragment extends Fragment {

    private final String TAG = MyInfoFragment.class.getSimpleName();
    private String mFilePath;
    private ViewGroup mRootView;
    private BaseActivity mActivity;
    private Toolbar mToolbar;
    private EditText mOriginalPw;
    private EditText mNewPw;
    private EditText mNewPwCheck;
    private EditText mUserAddr1;
    private EditText mUserAddr2;
    private EditText mUserPhone;
    private EditText mUserEmail;
    private EditText mUserIntro;
    private ImageView mUserImg;
    private Bitmap mBitmapUserImg;

    public static MyInfoFragment getInstance(){
        return new MyInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("addrResult", this, (requestKey, bundle) ->{
            String address = bundle.getString("addr");
            mUserAddr1.setText(address);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_myinfo_layout, container, false);

        init();

        return mRootView;
    }

    private void init(){
        mToolbar = mRootView.findViewById(R.id.my_info_toolbar);
        mActivity = (BaseActivity)getActivity();

        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setTitle("내 정보");
        setHasOptionsMenu(true);

        //하위 뷰 클릭 방지
        LinearLayout myInfoLayout = mRootView.findViewById(R.id.my_info_layout);
        myInfoLayout.setOnTouchListener((v, event) -> {
            return true;
        });

        initForm();
        setImgClickBtn();
        updateUserInfo();
        setSearchAddr();
    }

    private void initForm(){

        mOriginalPw = mRootView.findViewById(R.id.original_pw);
        mNewPw = mRootView.findViewById(R.id.new_pw);
        mNewPwCheck = mRootView.findViewById(R.id.new_pw_check);
        mUserPhone = mRootView.findViewById(R.id.user_phone);
        mUserEmail = mRootView.findViewById(R.id.user_email);
        mUserIntro = mRootView.findViewById(R.id.user_intro);
        mUserImg = mRootView.findViewById(R.id.user_profile_img);
        mUserAddr1 = mRootView.findViewById(R.id.user_addr1);
        mUserAddr2 = mRootView.findViewById(R.id.user_addr2);

        Bundle userData = getArguments();
        if(userData != null){

            UserInfoVO userInfoVO = (UserInfoVO) userData.getSerializable("userInfo");
            mUserPhone.setText(userInfoVO.getUserPhone());
            mUserEmail.setText(userInfoVO.getUserEmail());
            mUserIntro.setText(userInfoVO.getUserIntro());

            if(userInfoVO.getUserImg() != null && !userInfoVO.getUserImg().equals("")){
                Glide.with(getActivity())
                        .load(userInfoVO.getUserImg())
                        .into(mUserImg);
            }
        }
    }

    /**
     * 주소 검색
     */
    private void setSearchAddr(){
        mRootView.findViewById(R.id.addr_search_btn).setOnClickListener(v -> {
            mActivity.replaceFragment(AddrSearchFragment.getInstance(), null);
        });
    }

    /**
     * 유저 정보 업데이트
     */
    private void updateUserInfo(){
        mRootView.findViewById(R.id.update_btn).setOnClickListener(v -> {
            try {
                String message = "";

                String userId = PreferenceManager.getAttribute(getActivity(), "userId");
                String userOriPw = mOriginalPw.getText().toString();
                String userNewPw = mNewPw.getText().toString();
                String userNewPwCheck = mNewPwCheck.getText().toString();
                String userAddr1 = mUserAddr1.getText().toString();
                String userAddr2 = mUserAddr2.getText().toString();
                String userEmail = mUserEmail.getText().toString();
                String userPhone = mUserPhone.getText().toString();
                String userIntro = mUserIntro.getText().toString();

                String pwPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}$";
                String phonePattern = "^(010)([0-9]{4})([0-9]{4})$";

                if(!userOriPw.equals("")){
                    if(userNewPw.equals("") || userNewPwCheck.equals("")){
                        message = "새로운 비밀번호를 입력해주세요.";
                    }
                    else{
                        if(!Pattern.matches(pwPattern, userNewPw)){
                            message = getString(R.string.INVALID_PW);
                        }
                        else if(!userNewPw.equals(userNewPwCheck)){
                            message = "새로운 비밀번호가 일치하지 않습니다.";
                        }
                    }
                }
                else if(userOriPw.equals("") && (!userNewPw.equals("") || !userNewPwCheck.equals(""))){
                    message = getString(R.string.NULL_PW);
                }
                else if(userAddr1.equals("")){
                    message = getString(R.string.NULL_ADDR);
                }
                else if(userEmail.equals("")){
                    message = getString(R.string.NULL_EMAIL);
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    message = getString(R.string.INVALID_EMAIL);
                }
                else if(!userPhone.equals("")){
                    userPhone = userPhone.trim().replaceAll(" ","");
                    if(!Pattern.matches(phonePattern, userPhone)){
                        message = getString(R.string.INVALID_PHONE);
                    }
                }

                if(!message.equals("")){
                    CustomToast.showToast(getActivity(), message);
                }
                else{
                    ProgressDialog.showProgressDialog(getActivity());
                    ContentValues userData = new ContentValues();
                    userData.put("userPw",userNewPwCheck);
                    userData.put("userAddr1",userAddr1);
                    userData.put("userAddr2",userAddr2);
                    userData.put("userEmail",userEmail);
                    userData.put("userPhone",userPhone);
                    userData.put("userIntro",userIntro);

                    StringBuilder url = new StringBuilder(getResources().getString(R.string.REAL_WHOME_REST_URL))
                            .append(getResources().getString(R.string.USER_UPDATE))
                            .append("/")
                            .append(userId);

                    VolleyMultipart volleyMultipartRequest = new VolleyMultipart(Request.Method.POST, url.toString(),
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {

                                    String resData = "";
                                    for (byte b : response.data) {
                                        resData += (char) b;
                                    }
                                    ProgressDialog.dismissProgressDialog();
                                    Log.d(TAG, "onResponse: -----------------" + resData);

                                    try {
                                        JSONObject jsonObject = new JSONObject(resData);
                                        String resCode = jsonObject.getString("resCode");

                                        if (resCode.equals("200")) {
                                            mActivity.onPopupClick(getString(R.string.JOIN_COMPLETE), true, null);
                                            backStack();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("GotError", "" + error.getMessage());
                                }
                            }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = VolleyNetwork.getParamBody(userData);
                            return params;
                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            if (mBitmapUserImg != null) {
                                long imgName = System.currentTimeMillis();
                                params.put("userImg", new DataPart(imgName + ".png", GalleryImgUtil.getFileDataFromDrawable(mBitmapUserImg)));
                            }
                            return params;
                        }
                    };
                    Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    /**
     * 유저 프로필 이미지 클릭
     */
    private void setImgClickBtn(){

        mUserImg = mRootView.findViewById(R.id.user_profile_img);
        mUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {
                    }
                    else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                GalleryImgUtil.REQUEST_PERMISSIONS);
                    }
                }
                else {
                    GalleryImgUtil.showFileChooser(MyInfoFragment.this);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryImgUtil.PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            mFilePath = GalleryImgUtil.getPath(getActivity(), picUri);

            if (mFilePath != null) {
                try {
                    Log.d("filePath", String.valueOf(mFilePath));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    mUserImg.setImageBitmap(bitmap);
                    mBitmapUserImg = bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                CustomToast.showToast(getActivity(), getString(R.string.NOT_IMG_SELECTED));
            }
        }
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
