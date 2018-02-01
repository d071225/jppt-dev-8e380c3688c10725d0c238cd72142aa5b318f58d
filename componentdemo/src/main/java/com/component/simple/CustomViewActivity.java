package com.component.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hletong.mob.base.BaseActivity;

public  class CustomViewActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_customview;
    }
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }
}
