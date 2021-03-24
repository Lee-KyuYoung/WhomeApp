package com.lky.whome.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.lky.whome.R;
import com.lky.whome.activity.BaseActivity;
import com.lky.whome.activity.HomeDetailActivity;
import com.lky.whome.adapter.GallerylImgAdapter;


import java.util.List;
import java.util.Objects;


public class ImgGalleryFragment extends Fragment{

    private ViewGroup mRootView;
    private GallerylImgAdapter mAdapter;
    private BaseActivity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_gallery_layout, container, false);
        init();
        return mRootView;
    }

    private void init(){
        Toolbar toolbar = mRootView.findViewById(R.id.gallery_toolbar);
        mActivity = (BaseActivity)getActivity();
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle("Gallery");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        assert bundle != null;
        List<String> imgList = bundle.getStringArrayList("imgList");

        initViewPager(imgList);

        int imgPosition = bundle.getInt("position");
        int totalPosition = mAdapter.getItemCount();

        ((TextView)mRootView.findViewById(R.id.gallery_position)).setText((imgPosition+1)+" / "+totalPosition);

        Glide.with(mRootView.getContext())
                .load(imgList.get(imgPosition))
                .into((ImageView)mRootView.findViewById(R.id.gallery_img_viewer));
    }

    void initViewPager(List<String> list){

        mAdapter = new GallerylImgAdapter(mRootView.getContext());

        ViewPager2 gallery_viewpager = mRootView.findViewById(R.id.gallery_viewpager);
        gallery_viewpager.setOffscreenPageLimit(3);
        gallery_viewpager.setAdapter(mAdapter);

        //viewpager offset
        gallery_viewpager.setPageTransformer( (page, position) -> {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            float pageWidth = getResources().getDimension(R.dimen.home_detail_img_width);
            float pageMargin = getResources().getDimension(R.dimen.home_detail_img_margin);
            page.setTranslationX( position * -(screenWidth - pageMargin - pageWidth));
        });

        mAdapter.addItems(list);
        mAdapter.notifyDataSetChanged();
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
                .remove(ImgGalleryFragment.this)
                .commit();
        fragmentManager.popBackStack();
    }
}
