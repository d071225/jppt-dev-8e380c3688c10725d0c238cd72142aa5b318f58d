package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hletong.hyc.R;
import com.hletong.mob.base.BaseActivity;

public class CooperationActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_cooperation;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle(R.string.partner);
    }
}
