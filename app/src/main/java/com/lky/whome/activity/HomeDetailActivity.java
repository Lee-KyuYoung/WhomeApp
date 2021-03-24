package com.lky.whome.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.lky.whome.R;
import com.lky.whome.adapter.HomeDetailImgAdapter;
import com.lky.whome.adapter.HomeReviewAdapter;
import com.lky.whome.fragment.HomeLocationMapFragment;
import com.lky.whome.kakaomap.KakaoMap;
import com.lky.whome.toast.CustomToast;
import com.lky.whome.view.CreateImageView;
import com.lky.whome.view.CreateTextView;
import com.lky.whome.vo.HomeReviewVo;
import com.lky.whome.vo.WhomeVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeDetailActivity extends BaseActivity {

    private final String TAG = HomeDetailActivity.class.getSimpleName();
    private HomeLocationMapFragment mHomeLocationMapFragment = new HomeLocationMapFragment();
    private GridLayout home_facility_layout;
    private GridLayout home_precaution_layout;
    private GridLayout home_guestrule_layout;
    private ImageView detail_main_img;
    private ImageView detail_user_img;
    private ImageView detail_host_img;
    private TextView detail_home_range;
    private TextView detail_max_guest;
    private TextView detail_bed_cnt;
    private TextView detail_bath_cnt;
    private TextView detail_user_id;
    private TextView detail_title;
    private TextView detail_home_location;
    private TextView detail_home_explain;
    private TextView detail_star_rate;
    private TextView detail_host_join_date;
    private TextView detail_host_id;
    private TextView detail_host_intro;
    private RecyclerView review_recyclerview;
    private HomeReviewAdapter mHomeReviewAdapter;

    private int mStartNo = 1;
    private int mLimit = 10;
    private boolean mIsFinalPage = false;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);

        setToolbar();

        Intent intent = getIntent();
        WhomeVO whomeVO = (WhomeVO) intent.getSerializableExtra("whomeVO");

        initKaKaoMap();
        initDetail(whomeVO);
        initViewPager(whomeVO.getHomeImgList());
        initReview();
        getReviewList(whomeVO.getHomeId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        isLogin();
    }

    @Override
    protected void setToolbarSetting() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setToolbarNav();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home_detail;
    }

    public void initKaKaoMap(){
        Intent intent = getIntent();
        WhomeVO whomeVO = (WhomeVO) intent.getSerializableExtra("whomeVO");

        String latitude = whomeVO.getLatitude();
        String longitude = whomeVO.getLongitude();
        ViewGroup mapContainer = findViewById(R.id.detail_map_view);

        KakaoMap.initMapView(latitude, longitude, this, mapContainer);
    }

    //디테일 레이아웃 초기화
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void initDetail(WhomeVO whomeVO){

        home_facility_layout = findViewById(R.id.home_facility_layout);
        home_precaution_layout = findViewById(R.id.home_precaution_layout);
        home_guestrule_layout = findViewById(R.id.home_guestrule_layout);
        detail_main_img = findViewById(R.id.detail_main_img);
        detail_user_img = findViewById(R.id.detail_user_img);
        detail_user_id = findViewById(R.id.detail_user_id);
        detail_title = findViewById(R.id.detail_title);
        detail_home_range = findViewById(R.id.detail_home_range);
        detail_max_guest = findViewById(R.id.detail_max_guest);
        detail_bed_cnt = findViewById(R.id.detail_bed_cnt);
        detail_bath_cnt = findViewById(R.id.detail_bath_cnt);
        detail_home_location = findViewById(R.id.detail_home_location);
        detail_home_explain = findViewById(R.id.detail_home_explain);
        detail_host_img = findViewById(R.id.detail_host_img);
        detail_star_rate = findViewById(R.id.detail_star_rate);
        detail_host_join_date = findViewById(R.id.detail_host_join_date);
        detail_host_id = findViewById(R.id.detail_host_id);
        detail_host_intro = findViewById(R.id.detail_host_intro);

        Glide.with(this).load(whomeVO.getHomeImg()).into(detail_main_img);
        Glide.with(this).load(whomeVO.getUserImg()).into(detail_user_img);
        Glide.with(this).load(whomeVO.getUserImg()).into(detail_host_img);

        detail_home_range.setText(whomeVO.getHomeRange());
        detail_max_guest.setText("인원 "+whomeVO.getHomeMaxGuest()+"명");
        detail_bed_cnt.setText("침대 "+whomeVO.getHomeBedCount()+"개");
        detail_bath_cnt.setText("욕실 "+whomeVO.getHomeBathCount()+"개");
        detail_home_location.setText(whomeVO.getHomeAddr1());
        detail_home_explain.setText(whomeVO.getHomeComm());
        detail_user_id.setText(whomeVO.getUserId());
        detail_title.setText(whomeVO.getHomeTitle());
        detail_host_id.setText("호스트 : "+whomeVO.getUserId());
        detail_star_rate.setText(whomeVO.getHomeRate()+"( "+whomeVO.getHomeRateCount()+"개 )");
        detail_host_join_date.setText("회원가입 : "+whomeVO.getUserJoinDate());
        detail_host_intro.setText(whomeVO.getUserIntro());

        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(0,0,60,20);

        LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(30,30);
        imgLayoutParams.setMargins(0,0,15,20);

        //편의시설 제공
        for(String facility : whomeVO.getHomeFacilityList()){
            new CreateImageView(this, home_facility_layout)
                    .setImgDrawable(getDrawable(R.drawable.arrow))
                    .setLayoutParam(imgLayoutParams)
                    .addImageView();
            new CreateTextView(this, home_facility_layout)
                    .setText(facility)
                    .setLayoutParam(textLayoutParams)
                    .addTextView();
        }
        //주의사항
        for(String precaution : whomeVO.getHomePrecautionList()){
            new CreateImageView(this, home_precaution_layout)
                    .setImgDrawable(getDrawable(R.drawable.arrow))
                    .setLayoutParam(imgLayoutParams)
                    .addImageView();
            new CreateTextView(this, home_precaution_layout)
                    .setText(precaution)
                    .setLayoutParam(textLayoutParams)
                    .setTextGravity(Gravity.CENTER)
                    .addTextView();
        }
        //게스트가 알아야 하는 사항
        for(String guestRule : whomeVO.getHomeGuestRuleList()){
            new CreateImageView(this, home_guestrule_layout)
                    .setImgDrawable(getDrawable(R.drawable.arrow))
                    .setLayoutParam(imgLayoutParams)
                    .addImageView();
            new CreateTextView(this, home_guestrule_layout)
                    .setText(guestRule)
                    .setLayoutParam(textLayoutParams)
                    .addTextView();
        }

        //지도 상세보기 리스너
        findViewById(R.id.home_detail_map).setOnClickListener(v -> {

            KakaoMap.close();
            replaceFragment(mHomeLocationMapFragment, null);

            String lat = whomeVO.getLatitude();
            String lng = whomeVO.getLongitude();
            String addr = whomeVO.getHomeAddr1();

            Bundle bundle = new Bundle();
            bundle.putString("lat",lat);
            bundle.putString("lng",lng);
            bundle.putString("addr",addr);

            mHomeLocationMapFragment.setArguments(bundle);
        });
    }

    //이미지 뷰페이져 초기화
    private void initViewPager(List<String> list){

        HomeDetailImgAdapter Adapter = new HomeDetailImgAdapter(this);

        ViewPager2 home_detail_viewpager = findViewById(R.id.home_detail_viewpager);
        home_detail_viewpager.setOffscreenPageLimit(3);
        home_detail_viewpager.setAdapter(Adapter);

        //viewpager offset
        home_detail_viewpager.setPageTransformer( (page, position) -> {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            float pageWidth = getResources().getDimension(R.dimen.home_detail_img_width);
            float pageMargin = getResources().getDimension(R.dimen.home_detail_img_margin);
            page.setTranslationX( position * -(screenWidth - pageMargin - pageWidth));
        });

        Adapter.addItems(list);
        Adapter.notifyDataSetChanged();
    }

    //댓글 리사이클러뷰 초기화
    private void initReview(){

        review_recyclerview = findViewById(R.id.review_recyclerview);
        review_recyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        review_recyclerview.setLayoutManager(linearLayoutManager);

        mHomeReviewAdapter = new HomeReviewAdapter(this);
        review_recyclerview.setAdapter(mHomeReviewAdapter);

        review_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            //페이징
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home :
                onBackPressed();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(mOnBackPressedListener != null){
            mOnBackPressedListener.onBackPressed();
            mOnBackPressedListener = null;
        }
        else{
            super.onBackPressed();
            KakaoMap.close();
        }
    }
    @Override
    protected Context getContext() {
        return getContext();
    }
    //댓글 가져오기
    private void getReviewList(String homeId){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        int startNum = mStartNo;
        int endNum = mStartNo + mLimit - 1;

        StringBuffer url = new StringBuffer(getResources().getString(R.string.REAL_WHOME_REST_URL))
                .append(getResources().getString(R.string.GET_REVIEW))
                .append("/"+homeId)
                .append("?startNo="+startNum+"&endNo="+endNum);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                response -> {
                    Log.d(TAG, "onResponse: "+response);

                    try {
                        List<HomeReviewVo> reviewList = new ArrayList<>();
                        JSONObject object = new JSONObject(response);
                        JSONArray reviewJsonArray = object.getJSONArray("reviewList");

                        //가져온 데이터수가 limit수보다 작으면 마지막페이지
                        if(reviewJsonArray.length() < mLimit){
                            mIsFinalPage = true;
                        }
                        for(int i = 0; i < reviewJsonArray.length(); i++){
                            HomeReviewVo homeReviewVo = new HomeReviewVo();
                            JSONObject reviewJsonObject = reviewJsonArray.getJSONObject(i);

                            homeReviewVo.setUserId(reviewJsonObject.getString("userId"));
                            homeReviewVo.setComm(reviewJsonObject.getString("comm"));
                            homeReviewVo.setUserImg(reviewJsonObject.getString("userImg"));
                            homeReviewVo.setInsertDate(reviewJsonObject.getString("insertDate"));

                            reviewList.add(homeReviewVo);
                        }
                        mHomeReviewAdapter.addItems(reviewList);
                        mHomeReviewAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        mStartNo = endNum + 1;
                    }
                }, error -> {
            Log.d(TAG, "onErrorResponse: "+error);
            CustomToast.showToast(this, getString(R.string.NO_CONNECTED_SERVER));
        });
        requestQueue.add(stringRequest);
    }
}
