package com.lky.whome.activity;


import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;


import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lky.whome.R;
import com.lky.whome.adapter.MainRecyclerViewAdapter;
import com.lky.whome.dialog.ProgressDialog;
import com.lky.whome.network.VolleyNetwork;
import com.lky.whome.toast.CustomToast;
import com.lky.whome.utils.NetworkUtil;
import com.lky.whome.vo.WhomeVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private int mStartNo = 1;
    private int mLimit = 10;
    private boolean mIsFinalPage = false;
    private long mBackKeyPressedTime;

    private RecyclerView mRecyclerView;
    private MainRecyclerViewAdapter mMainRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkUtil.networkStateAlert(this);
        setToolbar();
        initMainRecyclerView();
        getWhomeList();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isLogin();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(ProgressDialog.mProgressDialog != null)
            ProgressDialog.dismissProgressDialog();
    }

    @Override
    protected void setToolbarSetting() {
        setToolbarNav();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    void initMainRecyclerView(){

        mRecyclerView = findViewById(R.id.main_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMainRecyclerViewAdapter = new MainRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mMainRecyclerViewAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            //페이징
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();
                if(lastPosition == totalCount - 1 && !ProgressDialog.onGoing && !mIsFinalPage){
                    getWhomeList();
                }
            }
        });
    }

    void  getWhomeList(){
        int startNum = mStartNo;
        int endNum = mStartNo + mLimit - 1;

        StringBuilder url = new StringBuilder(getResources().getString(R.string.REAL_WHOME_REST_URL))
                .append(getResources().getString(R.string.GET_HOME_LIST));

        ContentValues params = new ContentValues();
        params.put("startNo",startNum);
        params.put("endNo",endNum);

        new VolleyNetwork(this).requestGet(url.toString(), params, (response)->{
            try {
                List<WhomeVO> whomeList = new ArrayList<>();
                JSONObject object = new JSONObject(response);
                JSONArray homeList = object.getJSONArray("homeList");

                //가져온 데이터수가 limit수보다 작으면 마지막페이지
                if(homeList.length() < mLimit){
                    mIsFinalPage = true;
                }
                for(int i = 0; i < homeList.length(); i++){
                    WhomeVO whomeVO = new WhomeVO();
                    JSONObject homeInfo = homeList.getJSONObject(i);
                    whomeVO.setHomeId(homeInfo.getString("homeId"));
                    whomeVO.setHomeType(homeInfo.getString("homeRange"));
                    whomeVO.setHomeTitle(homeInfo.getString("homeTitle"));
                    whomeVO.setHomeBedCount(homeInfo.getString("bedCnt"));
                    whomeVO.setHomeRate(homeInfo.getString("rate"));
                    whomeVO.setHomePay(homeInfo.getString("pay"));
                    whomeVO.setHomeImg(homeInfo.getString("homeImg"));
                    whomeVO.setHomeMaxGuest(homeInfo.getString("maxGuest"));

                    whomeList.add(whomeVO);
                }
                mMainRecyclerViewAdapter.addItems(whomeList);
                mMainRecyclerViewAdapter.notifyDataSetChanged();
            }catch (JSONException e) {
                e.printStackTrace();
            }finally {
                mStartNo = endNum + 1;
            }
        });
    }

    @Override
    protected Context getContext() {
        return getContext();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(mOnBackPressedListener != null){
            mOnBackPressedListener.onBackPressed();
            mOnBackPressedListener = null;
        }
        else if(System.currentTimeMillis() >= mBackKeyPressedTime + 2000){
            mBackKeyPressedTime = System.currentTimeMillis();
            CustomToast.showToast(this,getString(R.string.APP_TOUCH_BACK_BTN));
        }
        else{
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}