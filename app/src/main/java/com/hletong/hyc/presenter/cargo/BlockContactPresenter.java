package com.hletong.hyc.presenter.cargo;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoForecastContract;

import java.util.Map;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockContactPresenter extends BlockBasePresenter<CargoForecastContract.BlockContactView> implements CargoForecastContract.BlockContactPresenter {

    public BlockContactPresenter(@NonNull CargoForecastContract.BlockContactView baseView) {
        super(baseView);
    }

    @Override
    protected void handleBillingType(int billingType) {
        if (isSelfTrade()){
            getView().showBlock();
        }else {
            getView().hideBlock();
        }
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}
