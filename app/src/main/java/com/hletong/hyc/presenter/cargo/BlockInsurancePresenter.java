package com.hletong.hyc.presenter.cargo;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoForecastContract;

import java.util.Map;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockInsurancePresenter extends BlockBasePresenter<CargoForecastContract.BlockInsuranceView> implements CargoForecastContract.BlockInsurancePresenter {
    public BlockInsurancePresenter(@NonNull CargoForecastContract.BlockInsuranceView baseView) {
        super(baseView);
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}
