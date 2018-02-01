package com.hletong.hyc.ui.activity.cargoforecast2;

import android.view.ViewStub;

import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.cargo.BlockSelfCargoPresenter;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockSelfCargo extends BlockBase<CargoForecastContract.BlockSelfCargoPresenter> implements CargoForecastContract.BlockSelfCargoView {

    public BlockSelfCargo(ViewStub viewStub) {
        super(viewStub);
    }

    @Override
    CargoForecastContract.BlockSelfCargoPresenter createPresenter() {
        return new BlockSelfCargoPresenter(this);
    }

    @Override
    protected String getTitle() {
        return "货物信息";
    }
}
