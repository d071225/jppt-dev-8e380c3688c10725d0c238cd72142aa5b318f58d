package com.hletong.hyc.ui.activity.ship;

import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.model.validate.ship.ShipNormalBookInfo;
import com.hletong.hyc.presenter.NormalBookPresenter;
import com.hletong.hyc.ui.activity.NormalBookActivity;
import com.hletong.hyc.ui.dialog.ShipSelectorDialogFragment;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.util.Constant;

/**
 * Created by ddq on 2017/1/24.
 * 船舶挂价摘牌
 */

public class ShipBookActivity extends NormalBookActivity<Ship, BookContract.NormalPresenter> {

    @Override
    protected PayMode getSubmitParam(String cargo, Source.DeductRt deductTaxRt) {
        return new ShipNormalBookInfo(cargo, deductTaxRt, getSelectedItem());
    }

    @Override
    protected TransporterSelectorDialog<Ship> getTransporter() {
        return ShipSelectorDialogFragment.getInstance("挂价摘牌", Constant.TRANSPORTER_ZP, getCargoUuid(), this);
    }

    @Override
    protected BookContract.NormalPresenter createPresenter() {
        return new NormalBookPresenter<>(this);
    }
}
