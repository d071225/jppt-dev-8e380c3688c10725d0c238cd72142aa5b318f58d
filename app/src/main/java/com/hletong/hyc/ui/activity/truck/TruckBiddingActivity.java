package com.hletong.hyc.ui.activity.truck;

import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.hyc.model.validate.truck.TruckBiddingBookInfo;
import com.hletong.hyc.ui.activity.BiddingBookActivity;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.ui.dialog.TrucksSelectorDialogFragment;
import com.hletong.hyc.util.Constant;

/**
 * Created by ddq on 2017/1/24.
 * 车辆竞价
 */

public class TruckBiddingActivity extends BiddingBookActivity<Truck> {
    @Override
    protected Validate2 getSubmitParams(String cargo, String price, Source.DeductRt deductTaxRt) {
        return new TruckBiddingBookInfo(cargo, price, deductTaxRt, getSelectedItem());
    }

    @Override
    protected TransporterSelectorDialog<Truck> getTransporter() {
        return TrucksSelectorDialogFragment.getInstance("竞价投标", Constant.TRANSPORTER_ZP, getCargoUuid(), this);
    }
}
