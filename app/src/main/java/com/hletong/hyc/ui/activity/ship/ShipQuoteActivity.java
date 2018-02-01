package com.hletong.hyc.ui.activity.ship;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.model.validate.Validate;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.hyc.model.validate.ship.ShipQuoteInfo;
import com.hletong.hyc.ui.activity.QuoteBookActivity;
import com.hletong.hyc.ui.dialog.ShipSelectorDialogFragment;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;

/**
 * Created by ddq on 2017/3/27.
 * 船舶-议价
 */

public class ShipQuoteActivity extends QuoteBookActivity<Ship> {

    @Override
    protected TransporterSelectorDialog<Ship> getTransporter() {
        return ShipSelectorDialogFragment.getInstance("承运船舶", Constant.TRANSPORTER_ZP, getCargoUuid(), this);
    }

    @Override
    public Validate2 getSubmitInfo() {
        CommonInputView input = (CommonInputView) findViewById(R.id.cargo);
        CommonInputView price = (CommonInputView) findViewById(R.id.price);
        return new ShipQuoteInfo(input.getInputValue(), price.getInputValue(), getSelectedItem());
    }
}
