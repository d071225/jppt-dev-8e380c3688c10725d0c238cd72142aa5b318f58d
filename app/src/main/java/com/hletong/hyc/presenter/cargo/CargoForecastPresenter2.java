package com.hletong.hyc.presenter.cargo;

import android.content.Intent;

import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.FilePresenter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dongdaqing on 2017/8/25.
 * 发布货源-总控制台，负责消息路由，信息收集
 */

public class CargoForecastPresenter2 extends FilePresenter<CargoForecastContract.View2> implements CargoForecastContract.Presenter2 {
    private Set<CargoForecastContract.BlockPresenter> subPresenter;

    public CargoForecastPresenter2(CargoForecastContract.View2 view) {
        super(view);
        subPresenter = new HashSet<>();
    }

    /**
     * 每个Block变为可见状态时(showBlock())被调用
     *
     * @param presenter
     */
    public void attach(CargoForecastContract.BlockPresenter presenter) {
        subPresenter.add(presenter);
    }

    /**
     * 每个Block变为不可见状态时(hideBlock())调用
     * @param presenter
     */
    public void detach(CargoForecastContract.BlockPresenter presenter) {
        subPresenter.remove(presenter);
    }

    /**
     * 某个Presenter里面的的数据发生了变化
     * @param presenter 源
     * @param event     事件描述
     * @param data      数据
     */
    public void notifyDataChanged(CargoForecastContract.BlockPresenter presenter, String event, Object data) {
        for (CargoForecastContract.BlockPresenter bp : subPresenter) {
            if (bp != presenter) {
                bp.dataChanged(event, data);
            }
        }
    }

    /**
     * 分发activityResult
     * @param requestCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, Intent data) {
        for (CargoForecastContract.BlockPresenter presenter : subPresenter)
            presenter.onActivityResult(requestCode, data);
    }

    @Override
    public void stop() {
        ForecastPresenterManager.clear();
    }
}
