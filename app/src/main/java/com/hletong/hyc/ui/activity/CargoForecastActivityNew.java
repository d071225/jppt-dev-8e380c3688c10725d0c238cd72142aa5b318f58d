package com.hletong.hyc.ui.activity;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class CargoForecastActivityNew extends ImageSelectorActivityNew<CargoForecastContract.Presenter2> implements CargoForecastContract.View2 {
    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_forecast_main;
    }

    @Override
    protected CargoForecastContract.Presenter2 createPresenter() {
        return null;
    }
}
