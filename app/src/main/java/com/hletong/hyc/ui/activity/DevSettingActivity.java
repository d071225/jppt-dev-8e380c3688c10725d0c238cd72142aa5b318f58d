package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.enums.Config;
import com.hletong.mob.HLApplication;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.SPUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevSettingActivity extends BaseActivity {
    @BindView(R.id.ck_jsonLog)
    CheckBox cbJson;
    @BindView(R.id.tv_host)
    TextView tvHost;
    @BindView(R.id.tv_loginInfo)
    TextView tvLoginInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_dev_set;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        Config config = Config.getConfig(BuildConfig.config);
        cbJson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HLApplication.JSON_LOG_ON = isChecked;
            }
        });
        tvHost.setText(String.format(Locale.CHINA, "服务地址：%s", config.getHost()));
        tvLoginInfo.setText(SPUtils.getString("RequestInfo", "noData")
                + "\n\n" + SPUtils.getString("UnsupportedEncodingException", "noUnsupported")
                + "\n\n" + SPUtils.getString("time", "noTime")
                + "\n\n" + SPUtils.getString("location", "noLocation"));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
