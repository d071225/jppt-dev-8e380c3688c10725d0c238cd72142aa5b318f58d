package com.hletong.hyc.ui.activity.ship;

import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.validate.Transport;
import com.hletong.hyc.model.validate.ship.ShipForecastInfos;
import com.hletong.hyc.ui.activity.BaseForecastActivity;
import com.hletong.hyc.ui.dialog.ShipSelectorDialogFragment;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.util.SimpleDate;

/**
 * Created by ddq on 2017/1/24.
 * 船舶运力预报
 */

public class ForecastShipActivity extends BaseForecastActivity<Ship> {

    @Override
    protected Transport getTransportInfo(Address startLoc, Address endLoc, SimpleDate startDT, SimpleDate endDT, String capacity, String backup) {
        return new ShipForecastInfos(startLoc, endLoc, startDT, endDT, capacity, backup, getSelectedItem());
    }

    @Override
    protected TransporterSelectorDialog<Ship> getTransporter() {
        return ShipSelectorDialogFragment.getInstance("运力预报", Constant.SHIPS, null, this);
    }
}
