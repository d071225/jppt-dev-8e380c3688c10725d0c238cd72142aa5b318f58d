package com.hletong.mob.base;

import android.os.Bundle;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by dongdaqing on 2017/7/31.
 */

public abstract class MvpBaseRefreshFragment<D, V extends IBaseView, P extends IBasePresenter> extends BaseRefreshFragment<D> {
    private P mPresenter;

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        mPresenter.stop();
        super.onDestroy();
    }
}
