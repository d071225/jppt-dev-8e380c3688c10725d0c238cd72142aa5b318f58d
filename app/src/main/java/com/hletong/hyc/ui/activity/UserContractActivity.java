package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.mob.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserContractActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_hlet;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_hlyt, R.id.tv_hzdw})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_hlyt:
                toActivity(CompanyHistoryActivity.class, null, null);
                break;
            case R.id.tv_hzdw:
                toActivity(CooperationActivity.class, null, null);
                break;
        }
    }
}
