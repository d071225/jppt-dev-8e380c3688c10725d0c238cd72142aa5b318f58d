package com.hletong.hyc.ui.activity.ship;

import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.hyc.model.validate.ship.ShipBiddingBookInfo;
import com.hletong.hyc.ui.activity.BiddingBookActivity;
import com.hletong.hyc.ui.dialog.ShipSelectorDialogFragment;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.util.Constant;

/**
 * Created by ddq on 2017/1/24.
 * 船舶竞价投标
 */

public class ShipBiddingActivity extends BiddingBookActivity<Ship> {

    @Override
    protected TransporterSelectorDialog<Ship> getTransporter() {
        return ShipSelectorDialogFragment.getInstance("竞价投标", Constant.TRANSPORTER_ZP, getCargoUuid(), this);
    }

    @Override
    protected Validate2 getSubmitParams(String cargo, String price, Source.DeductRt deductTaxRt) {
        return new ShipBiddingBookInfo(cargo, price, deductTaxRt, getSelectedItem());
    }
}
