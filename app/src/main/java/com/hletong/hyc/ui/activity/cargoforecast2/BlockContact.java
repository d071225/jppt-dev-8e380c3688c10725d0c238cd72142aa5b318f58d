package com.hletong.hyc.ui.activity.cargoforecast2;

import android.view.ViewStub;

import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.cargo.BlockContactPresenter;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockContact extends BlockBase<CargoForecastContract.BlockContactPresenter> implements CargoForecastContract.BlockContactView {
    public BlockContact(ViewStub viewStub) {
        super(viewStub);
    }

    @Override
    CargoForecastContract.BlockContactPresenter createPresenter() {
        return new BlockContactPresenter(this);
    }

    @Override
    protected String getTitle() {
        return "货方联系人";
    }
}
