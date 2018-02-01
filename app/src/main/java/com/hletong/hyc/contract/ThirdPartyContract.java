package com.hletong.hyc.contract;

import android.support.annotation.StringRes;
import android.text.Spanned;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by ddq on 2017/1/10.
 */

public interface ThirdPartyContract {
    interface View extends IBaseView {
        void setupContractRequest();

        void showContract(Spanned mSpanned);

        String getString(@StringRes int res);
    }

    interface Presenter extends IBasePresenter {
        void loadContractByContractID(String contractUUID);

        void loadContractByCargoID(String cargoUUID, double price, double count);
    }
}
