package com.hletong.hyc.ui.activity.cargoforecast2;

import android.view.ViewStub;

import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.cargo.BlockTransportPresenter;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockTransport extends BlockBase<CargoForecastContract.BlockTransportPresenter> implements CargoForecastContract.BlockTransportView {
    public BlockTransport(ViewStub viewStub) {
        super(viewStub);
    }

    @Override
    CargoForecastContract.BlockTransportPresenter createPresenter() {
        return new BlockTransportPresenter(this);
    }

    @Override
    protected String getTitle() {
        return "运输信息";
    }
}
