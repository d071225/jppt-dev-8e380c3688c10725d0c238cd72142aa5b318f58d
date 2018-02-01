package com.hletong.mob.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by dongdaqing on 2017/7/12.
 * mvp activity 基类
 */

public abstract class MvpActivity<P extends IBasePresenter> extends BaseActivity {
    private P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.stop();
        super.onDestroy();
    }
}
