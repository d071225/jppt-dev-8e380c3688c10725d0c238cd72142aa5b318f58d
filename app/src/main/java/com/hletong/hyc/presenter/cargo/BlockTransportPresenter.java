package com.hletong.hyc.presenter.cargo;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoForecastContract;

import java.util.Map;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockTransportPresenter extends BlockBasePresenter<CargoForecastContract.BlockTransportView> implements CargoForecastContract.BlockTransportPresenter {
    public BlockTransportPresenter(@NonNull CargoForecastContract.BlockTransportView baseView) {
        super(baseView);
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}
