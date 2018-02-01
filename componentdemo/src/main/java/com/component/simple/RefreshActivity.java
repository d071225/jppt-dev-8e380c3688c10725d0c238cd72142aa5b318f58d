package com.component.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hletong.mob.base.BaseActivity;

public  class RefreshActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_refresh;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
//        BaseFragment mBaseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag("BaseFragment");
//        if (mBaseFragment == null) {
//            mBaseFragment = (BaseFragment) Fragment.instantiate(this, RefreshFragment.class.getName(), null);
//            getSupportFragmentManager().beginTransaction().add(R.id.id_fragment_container, mBaseFragment, "BaseFragment").commit();
//        }
    }
}
