package com.lky.whome.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.lky.whome.R;
import com.lky.whome.activity.BaseActivity;

import com.lky.whome.dialog.ProgressDialog;
import com.lky.whome.network.VolleyMultipart;
import com.lky.whome.network.VolleyNetwork;
import com.lky.whome.toast.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserJoinFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private final String TAG = UserJoinFragment.class.getSimpleName();

    private String mFilePath;
    private ViewGroup mRootView;
    private BaseActivity mActivity;
    private ImageView mUserProfileImg;
    private TextView mUserId;
    private TextView mUserName;
    private TextView mUserPw;
    private TextView mUserPwCheck;
    private TextView mUserAddr1;
    private TextView mUserAddr2;
    private TextView mUserEmail;
    private TextView mUserPhone;
    private TextView mUserIntro;
    private Bitmap mUserImg;

    public static UserJoinFragment getInstance(){
        return new UserJoinFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fragment 1.3.0-alpha04 기능
        //Fragment 간 결과 전달
        getParentFragmentManager().setFragmentResultListener("addrResult", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String address = bundle.getString("addr");
                mUserAddr1.setText(address);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_join_layout, container, false);

        init();

        return mRootView;
    }

    private void init(){

        Toolbar toolbar = mRootView.findViewById(R.id.join_toolbar);
        mActivity = (BaseActivity)getActivity();
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle("회원가입");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        //하위 뷰 클릭 방지
        LinearLayout joinLayout = mRootView.findViewById(R.id.join_layout);
        joinLayout.setOnTouchListener((v, event) -> {
            return true;
        });

        mUserName = mRootView.findViewById(R.id.user_name);
        mUserId = mRootView.findViewById(R.id.user_id);
        mUserPw = mRootView.findViewById(R.id.user_pw);
        mUserPwCheck = mRootView.findViewById(R.id.user_pw_check);
        mUserAddr1 = mRootView.findViewById(R.id.user_addr1);
        mUserAddr2 = mRootView.findViewById(R.id.user_addr2);
        mUserEmail = mRootView.findViewById(R.id.user_email);
        mUserPhone = mRootView.findViewById(R.id.user_phone);
        mUserIntro = mRootView.findViewById(R.id.user_intro);

        setSearchAddr();
        setImgClickBtn();
        setUserJoin();
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
     * 이미지 갤러리 파일 선택
     */
    private void setImgClickBtn(){

        mUserProfileImg = mRootView.findViewById(R.id.user_profile_img);
        mUserProfileImg.setOnClickListener(new View.OnClickListener() {
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
                                REQUEST_PERMISSIONS);
                    }
                }
                else {
                    showFileChooser();
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "프로필 사진 선택"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            mFilePath = getPath(picUri);

            if (mFilePath != null) {
                try {
                    Log.d("filePath", String.valueOf(mFilePath));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    mUserProfileImg.setImageBitmap(bitmap);
                    mUserImg = bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                CustomToast.showToast(getActivity(), getString(R.string.NOT_IMG_SELECTED));
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //회원가입
    private void setUserJoin(){

        mRootView.findViewById(R.id.join_btn).setOnClickListener(v ->{

            String message = "";
            String userName = mUserName.getText().toString();
            String userId = mUserId.getText().toString();
            String userPw = mUserPw.getText().toString();
            String userPwCheck = mUserPwCheck.getText().toString();
            String userAddr1 = mUserAddr1.getText().toString();
            String userAddr2 = mUserAddr2.getText().toString();
            String userEmail = mUserEmail.getText().toString();
            String userPhone = mUserPhone.getText().toString();
            String userIntro = mUserIntro.getText().toString();

            String idPattern = "^[a-zA-Z0-9]{4,10}$";
            String pwPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}$";
            String phonePattern = "^(010)([0-9]{4})([0-9]{4})$";

            if(userName.equals("")){
                message = getString(R.string.NULL_NAME);
            }
            else if(userId.equals("")){
                message = getString(R.string.NULL_ID);
            }
            else if(!Pattern.matches(idPattern, userId)){
                message = getString(R.string.INVALID_ID);
            }
            else if(userPw.equals("")){
                    message = getString(R.string.NULL_PW);
            }
            else if(!Pattern.matches(pwPattern, userPw)){
                message = getString(R.string.INVALID_PW);
            }
            else if(userPwCheck.equals("")){
                message = getString(R.string.NULL_PW_CHECK);
            }
            else if(!userPw.equals(userPwCheck)){
                message = getString(R.string.NOT_PW_MATCH);
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
                userData.put("userName",userName);
                userData.put("userId",userId);
                userData.put("userPw",userPw);
                userData.put("userPwCheck",userPwCheck);
                userData.put("userAddr1",userAddr1);
                userData.put("userAddr2",userAddr2);
                userData.put("userEmail",userEmail);
                userData.put("userPhone",userPhone);
                userData.put("userIntro",userIntro);

                StringBuilder url = new StringBuilder(getResources().getString(R.string.REAL_WHOME_REST_URL))
                        .append(getResources().getString(R.string.USER_JOIN));

                VolleyMultipart volleyMultipartRequest = new VolleyMultipart(Request.Method.POST, url.toString(),
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {

                                String resData = "";
                                for(byte b : response.data){
                                    resData += (char)b;
                                }
                                ProgressDialog.dismissProgressDialog();
                                Log.d(TAG, "onResponse: -----------------"+resData);

                                try {
                                    JSONObject jsonObject = new JSONObject(resData);
                                    String resCode = jsonObject.getString("resCode");

                                    if(resCode.equals("200")){
                                        mActivity.onPopupClick(getString(R.string.JOIN_COMPLETE), true, null);
                                        backStack();
                                    }
                                    else if(resCode.equals("E005")){
                                        CustomToast.showToast(getActivity(), "중복된 아이디가 존재합니다.");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("GotError",""+error.getMessage());
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
                        if(mUserImg != null) {
                            long imgName = System.currentTimeMillis();
                            params.put("userImg", new DataPart(imgName + ".png", getFileDataFromDrawable(mUserImg)));
                        }
                        return params;
                    }
                };
                Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
            }
        });
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
                .remove(UserJoinFragment.this)
                .commit();
        fragmentManager.popBackStack();
    }
}
