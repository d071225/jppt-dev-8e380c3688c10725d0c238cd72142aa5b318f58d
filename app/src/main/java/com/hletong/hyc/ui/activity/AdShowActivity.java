package com.hletong.hyc.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.R;
import com.hletong.hyc.model.AdInfo;
import com.hletong.hyc.model.ETCCard;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.util.SPUtils;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.view.controller.EasyActivity;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2016/12/12.
 */
public class AdShowActivity extends EasyActivity {
    @BindView(R.id.count_down)
    TextView cd;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mShowTime--;
            if (mShowTime > 0) {
                cd.setText(getString(R.string.skip) + mShowTime + "秒");
                mHandler.sendEmptyMessageDelayed(1, 1000);
            } else {
                finish();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_ad_show;
    }

    private AdInfo mAdInfo;
    private int mShowTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        List<AdInfo> adInfos = SPUtils.getBean(Constant.SP_KEY.KEY_ADINFO_LIST, new TypeToken<List<AdInfo>>() {
        });
        //in case
        if (adInfos == null) {
            finish();
            return;
        }
        mAdInfo = adInfos.get(new Random().nextInt(adInfos.size()));
        mShowTime = mAdInfo.getShowTime();
        List<String> adFiles = mAdInfo.getFileList();
        if (ParamUtil.isEmpty(adFiles)) {
            finish();
            return;
        }
        String file = adFiles.get(new Random().nextInt(adFiles.size()));
        Glide.with(this).load(String.format(Constant.getUrl(Constant.LOAD_PIC_PUBLIC), file))
                .into(ivAd);
        cd.setText(getString(R.string.skip) + mShowTime + "秒");
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @OnClick({R.id.count_down, R.id.iv_ad})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.count_down:
                finish();
                break;
            case R.id.iv_ad:
                String jumpUrl = mAdInfo.getJumpUrl();
                if (!TextUtils.isEmpty(jumpUrl)) {
                    if (jumpUrl.regionMatches(true, 0, "http", 0, 4)) {
                        WebActivity.openActivity(this, mAdInfo.getAdvName(), jumpUrl);
                    } else {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName(this, jumpUrl);
                            if (jumpUrl.equalsIgnoreCase(CardDetailActivity.class.getName())) {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("etc", new ETCCard(true, getString(R.string.hl_etc_card), getString(R.string.card_detail), R.drawable.hl_etc_credit_card_small));
                                bundle.putInt("position", 0);
                                intent.putExtras(bundle);
                            }
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
    }
}
