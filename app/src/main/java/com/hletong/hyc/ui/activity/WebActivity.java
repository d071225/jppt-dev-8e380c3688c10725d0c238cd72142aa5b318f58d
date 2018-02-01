package com.hletong.hyc.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.enums.Config;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.StringUtil;
import com.xcheng.view.util.JumpUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

/**
 * Created by ddq on 2017/2/8.
 */

public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private String firstUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            getToolbar().setVisibility(View.GONE);
        } else {
            setCustomTitle(title);
        }
        mWebView = findViewById(R.id.id_webView);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!StringUtil.isTrimBlank(title)) {
                    setCustomTitle(title);
                }
            }
        });
        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        final String s = getIntent().getStringExtra("url");
        final String url;
        if (!s.startsWith("http")) {
            final String prefix = Config.getConfig(BuildConfig.config).getHost();
            boolean r1 = prefix.endsWith("/");
            boolean r2 = s.startsWith("/");
            if (r1 && r2)
                url = prefix + s.substring(1);
            else if (!r1 && !r2)
                url = prefix + "/" + s;
            else
                url = prefix + s;
        } else
            url = getIntent().getStringExtra("url");
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", AppManager.getCookie());
        firstUrl = url;
        mWebView.loadUrl(url, header);
    }

    @Override
    public void setListener() {
        super.setListener();
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!HttpUrl.parse(firstUrl).encodedPath().equals(HttpUrl.parse(mWebView.getUrl()).encodedPath())) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
            }
        });
    }

    public static void openActivity(Activity context, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        JumpUtil.toActivity(context, WebActivity.class, bundle);
    }
}
