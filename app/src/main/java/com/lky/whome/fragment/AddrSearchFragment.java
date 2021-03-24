package com.lky.whome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lky.whome.R;
import com.lky.whome.activity.BaseActivity;
import com.lky.whome.toast.CustomToast;

public class AddrSearchFragment extends Fragment {

    public static AddrSearchFragment getInstance(){
        return new AddrSearchFragment();
    }

    private ViewGroup mRootView;
    private WebView mWebView;
    private BaseActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_webview, container, false);
        initWebView();
        return mRootView;
    }

    private void initWebView(){
        mActivity = (BaseActivity) getActivity();
        mWebView = mRootView.findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.addJavascriptInterface(new WebViewInterFace(), "whome_app");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        String url = getString(R.string.REAL_WHOME_REST_URL) + getString(R.string.WEB_VIEW);

        mWebView.loadUrl(url);
    }

    class WebViewInterFace{
        @JavascriptInterface
        public void getAddr(String addr){
            Bundle bundle = new Bundle();
            bundle.putString("addr",addr);
            getParentFragmentManager().setFragmentResult("addrResult", bundle);
            FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(AddrSearchFragment.this).commit();
            fragmentManager.popBackStack();
        }
    }
}

