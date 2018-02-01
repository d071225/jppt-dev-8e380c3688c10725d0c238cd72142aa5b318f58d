package com.hletong.hyc.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hletong.hyc.R;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowCargoActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_follow_cargo;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle("货源跟踪");
        ButterKnife.bind(this);
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        mWebView.postUrl("http://10.20.10.127:8080/page/hletong/api/map/trailLngLatList", null);
    }
}
