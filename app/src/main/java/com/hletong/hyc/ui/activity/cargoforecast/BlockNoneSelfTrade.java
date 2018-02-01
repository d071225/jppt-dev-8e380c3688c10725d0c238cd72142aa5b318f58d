package com.hletong.hyc.ui.activity.cargoforecast;

import android.view.ViewStub;

import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by ddq on 2017/3/22.
 *
 */

abstract class BlockNoneSelfTrade extends BaseBlock{

    BlockNoneSelfTrade(ViewStub viewStub, String titleString, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView switchDelegate) {
        super(viewStub, titleString, dictItemDialog, switchDelegate);
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        if (billingType == 3) {//自主交易
            hideBlock();
        } else {//非自主交易
            showBlock(false);
        }
    }
}
