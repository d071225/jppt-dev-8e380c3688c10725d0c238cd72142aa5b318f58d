package com.component.simple;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.component.model.LoginInfo;
import com.component.model.Source;
import com.google.gson.Gson;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.security.Base64;
import com.hletong.mob.security.EncryptUtils;
import com.hletong.mob.util.SimpleDate;
import com.hletong.mob.util.ToastLess;
import com.hletong.mob.util.UpgradeUtil;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.BitmapParse;
import com.xcheng.okhttp.callback.OkCall;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.BaseError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HttpActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tv_json)
    TextView jsonView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_http;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_http, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        webView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        jsonView.setVisibility(View.GONE);

        if (item.getItemId() == R.id.string) {
            getString();
        } else if (item.getItemId() == R.id.image) {
            getImageView();
        } else if (item.getItemId() == R.id.jsonItem) {
            try {
                getJsonItem();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (item.getItemId() == R.id.jsonList) {
            getJsonList();
        }
        return true;
    }

    private void getString() {
        webView.setVisibility(View.VISIBLE);
        OkRequest request = EasyOkHttp.get("https://github.com/").tag(hashCode()).build();
        new ExecutorCall<String>(request).enqueue(new UICallback<String>() {
            @Override
            public void onError(OkCall<String> okCall, BaseError error) {
                ToastLess.showToast(error.getMessage());
            }

            @Override
            public void onSuccess(OkCall<String> okCall, String response) {
                webView.loadDataWithBaseURL(null, response, "text/html", "utf-8", null);
            }
        });
    }

    private void getImageView() {
        imageView.setVisibility(View.VISIBLE);
        OkRequest request = EasyOkHttp.get("http://pic1.nipic.com/20090311/2175354_084638022_2.jpg")
                .tag(hashCode())
                .outProgress()
                .parseClass(BitmapParse.class)
                .build();
        new ExecutorCall<Bitmap>(request).enqueue(new UICallback<Bitmap>() {
            @Override
            public void outProgress(OkCall<Bitmap> okCall, float progress, long total, boolean done) {
                super.outProgress(okCall, progress, total, done);
                ToastLess.showToast(progress * 100 + "%");
            }

            @Override
            public void onError(OkCall<Bitmap> okCall, BaseError error) {
                ToastLess.showToast(error.getMessage());
            }

            @Override
            public void onSuccess(OkCall<Bitmap> okCall, Bitmap response) {
                imageView.setImageBitmap(response);
            }
        });
    }

    private void getJsonItem() throws UnsupportedEncodingException {
        jsonView.setVisibility(View.VISIBLE);
        OkRequest okRequest = EasyOkHttp.get("mobile/hletong/api/login")
                .tag(hashCode())
                .param("loginName", "cl08")
                .param("userPwd", EncryptUtils.md5Encrypt("999999"))
                .param("userPwd2", Base64.encode("999999"))
                .param("userType", 2)
                .param("channel", "android")
                .param("updateDate", SimpleDate.formatDate(new Date(), SimpleDate.formatterLoginDate))
                .param("versionCode", UpgradeUtil.getVersionCode())
                .build();
        new ExecutorCall<LoginInfo>(okRequest).enqueue(new UICallback<LoginInfo>() {
            @Override
            public void onError(OkCall<LoginInfo> okCall, BaseError error) {
                ToastLess.showToast(error.getMessage());
            }

            @Override
            public void onSuccess(OkCall<LoginInfo> okCall, LoginInfo response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response));
                    String message = jsonObject.toString(2);
                    jsonView.setText(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getJsonList() {
        jsonView.setVisibility(View.VISIBLE);

        OkRequest okRequest = EasyOkHttp.get("mobile/public/hletong/cdm/cargoforecast/notice/list")
                .tag(hashCode())
                .param("start", 1)
                .param("limit", 10)
                .extra("entry", "list")
                .param("transportType", 1)
                .build();
        new ExecutorCall<List<Source>>(okRequest).enqueue(new UICallback<List<Source>>() {
            @Override
            public void onError(OkCall<List<Source>> okCall, BaseError error) {
                ToastLess.showToast(error.getMessage());
            }

            @Override
            public void onSuccess(OkCall<List<Source>> okCall, List<Source> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response));
                    String message = jsonArray.toString(2);
                    jsonView.setText(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyOkHttp.cancel(hashCode());
    }
}
