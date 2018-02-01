package com.hletong.hyc.presenter.cargo;

import com.hletong.hyc.contract.CargoForecastContract;

/**
 * Created by dongdaqing on 2017/8/25.
 *
 */

public class ForecastPresenterManager {
    private CargoForecastPresenter2 mForecastPresenter;
    private static ForecastPresenterManager manager;

    private ForecastPresenterManager(CargoForecastPresenter2 presenter) {
        mForecastPresenter = presenter;
    }

    public static void initManager(CargoForecastPresenter2 presenter) {
        if (manager == null) {
            manager = new ForecastPresenterManager(presenter);
        } else {
            manager.mForecastPresenter = presenter;
        }
    }

    public static void attach(CargoForecastContract.BlockPresenter presenter) {
        manager.mForecastPresenter.attach(presenter);
    }

    public static void detach(CargoForecastContract.BlockPresenter presenter) {
        manager.mForecastPresenter.attach(presenter);
    }

    public static void notifyDataChanged(CargoForecastContract.BlockPresenter presenter, String event, Object data) {
        manager.mForecastPresenter.notifyDataChanged(presenter, event, data);
    }

    public static void clear() {
        manager.mForecastPresenter = null;
    }
}
