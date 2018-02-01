package com.hletong.mob.base;

import android.os.Bundle;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by dongdaqing on 2017/7/31.
 */

public abstract class MvpFragment<P extends IBasePresenter> extends BaseFragment {
    private P mPresenter;

    protected abstract P createPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        mPresenter.stop();
        super.onDestroy();
    }
}
