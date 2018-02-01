package com.hletong.hyc.ui.activity.truck;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.hyc.model.validate.ship.ShipQuoteInfo;
import com.hletong.hyc.model.validate.truck.TruckQuoteInfo;
import com.hletong.hyc.ui.activity.QuoteBookActivity;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.ui.dialog.TrucksSelectorDialogFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;

/**
 * Created by ddq on 2017/3/27.
 * 车辆-议价
 */

public class TruckQuoteActivity extends QuoteBookActivity<Truck> {

    @Override
    protected TransporterSelectorDialog<Truck> getTransporter() {
        return TrucksSelectorDialogFragment.getInstance("承运车辆", Constant.TRANSPORTER_ZP, getCargoUuid(), this);
    }

    @Override
    public Validate2 getSubmitInfo() {
        CommonInputView input = (CommonInputView) findViewById(R.id.cargo);
        CommonInputView price = (CommonInputView) findViewById(R.id.price);
        return new TruckQuoteInfo(input.getInputValue(), price.getInputValue(), getSelectedItem());
    }
}
