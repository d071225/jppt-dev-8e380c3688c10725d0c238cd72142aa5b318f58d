package com.hletong.hyc.ui.activity.truck;

import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.Transport;
import com.hletong.hyc.model.validate.truck.TruckForecastInfos;
import com.hletong.hyc.ui.activity.BaseForecastActivity;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.ui.dialog.TrucksSelectorDialogFragment;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.util.SimpleDate;

/**
 * Created by ddq on 2017/1/24.
 * 车辆运力预报
 */

public class ForecastTruckActivity extends BaseForecastActivity<Truck> {

    @Override
    protected Transport getTransportInfo(Address startLoc, Address endLoc, SimpleDate startDT, SimpleDate endDT, String capacity, String backup) {
        return new TruckForecastInfos(startLoc, endLoc, startDT, endDT, capacity, backup, getSelectedItem());
    }

    @Override
    protected TransporterSelectorDialog<Truck> getTransporter() {
        return TrucksSelectorDialogFragment.getInstance("运力预报", Constant.TRUCKS, null, this);
    }
}
