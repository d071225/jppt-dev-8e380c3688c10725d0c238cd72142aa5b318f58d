package com.hletong.hyc.presenter.cargo;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.mob.architect.presenter.BasePresenter;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public abstract class BlockBasePresenter<V extends CargoForecastContract.BlockView> extends BasePresenter<V> implements CargoForecastContract.BlockPresenter {
    private int billingType;

    public BlockBasePresenter(@NonNull V baseView) {
        super(baseView);
    }

    @Override
    public void sourceAttached(Source source) {

    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {

    }

    @Override
    public void billingTypeChanged(int billingType) {
        if (billingType != this.billingType) {
            this.billingType = billingType;
            handleBillingType(billingType);
        }
    }

    @Override
    public void dictionarySelected(String key, DictionaryItem item) {

    }

    @Override
    public void dataChanged(String event, Object data) {

    }

    protected void handleBillingType(int billingType) {
        getView().showBlock();
    }

    public boolean isSelfTrade(){
        return billingType == 3;
    }
}
