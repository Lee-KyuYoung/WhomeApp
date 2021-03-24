package com.lky.whome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lky.whome.R;
import com.lky.whome.activity.BaseActivity;
import com.lky.whome.activity.HomeDetailActivity;
import com.lky.whome.kakaomap.KakaoMap;

import java.util.Objects;

public class HomeLocationMapFragment extends Fragment {

    private BaseActivity mActivity;
    private ViewGroup mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.home_location_map, container, false);

        init();

        return mRootView;
    }

    private void init(){
        Bundle bundle = getArguments();
        assert bundle != null;
        String lat = bundle.getString("lat");
        String lng = bundle.getString("lng");
        String addr = bundle.getString("addr");

        Toolbar toolbar = mRootView.findViewById(R.id.home_location_toolbar);
        mActivity = (BaseActivity) getActivity();
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle(addr);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        ViewGroup viewGroup = mRootView.findViewById(R.id.home_location_map);
        KakaoMap.initMapView(lat,lng, mRootView.getContext(),viewGroup);
    }

    @Override
    public void onResume() {
        super.onResume();
        //뒤로가기 설정
        mActivity.setOnBackPressedListener(()->{
            backStack();
        });
    }

    public void backStack(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(this)
                .commit();
        fragmentManager.popBackStack();
        KakaoMap.close();
        ((HomeDetailActivity)getActivity()).initKaKaoMap();
    }
}
