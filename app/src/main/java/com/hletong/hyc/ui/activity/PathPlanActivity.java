package com.hletong.hyc.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseActivity;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ddq on 2017/2/8.
 */

public class PathPlanActivity extends BaseActivity {
    public static final String START_ADDRESS = "startAddress";
    public static final String END_ADDRESS = "endAddress";
    @BindView(R.id.tv_startCity)
    TextView tvStartCity;
    @BindView(R.id.tv_startCountry)
    TextView tvStartCountry;
    @BindView(R.id.tv_endCity)
    TextView tvEndCity;
    @BindView(R.id.tv_endCountry)
    TextView tvEndCountry;
    @BindView(R.id.id_webView)
    WebView mWebView;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    private Address mStartAddress;
    private Address mEndAddress;

    private String mUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pathplan;
    }

    @Override
    public void initData() {
        super.initData();
        mStartAddress = getBundle().getParcelable(START_ADDRESS);
        mEndAddress = getBundle().getParcelable(END_ADDRESS);
        assert mStartAddress != null;
        assert mEndAddress != null;
        String encode = "%%7C";//"|转义"
        mUrl = Constant.getUrl(Constant.PATH_PLAN) + String.format("?jsonParam=%s" + encode + "%s" + encode + "%s" + encode + "%s" + encode + "%s" + encode + "%s" + encode + "%s" + encode + "%s", mStartAddress.getRealProvince(), mStartAddress.getRealCity(), mStartAddress.getRealCounty(), mStartAddress.getDetails(), mEndAddress.getRealProvince(), mEndAddress.getRealCity(), mEndAddress.getRealCounty(), mEndAddress.getDetails());
        Logger.d(mUrl);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        tvStartCity.setText(mStartAddress.getRealCity());
        tvStartCountry.setText(mStartAddress.getRealCounty());
        tvEndCity.setText(mEndAddress.getRealCity());
        tvEndCountry.setText(mEndAddress.getRealCounty());

        mWebView = (WebView) findViewById(R.id.id_webView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        mWebView.loadUrl(mUrl);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
    }
}
