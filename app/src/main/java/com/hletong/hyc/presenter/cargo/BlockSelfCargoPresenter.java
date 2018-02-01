package com.hletong.hyc.presenter.cargo;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoForecastContract;

import java.util.Map;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockSelfCargoPresenter extends BlockBasePresenter<CargoForecastContract.BlockSelfCargoView> implements CargoForecastContract.BlockSelfCargoPresenter {

    public BlockSelfCargoPresenter(@NonNull CargoForecastContract.BlockSelfCargoView baseView) {
        super(baseView);
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}
