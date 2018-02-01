package com.hletong.hyc.ui.activity.truck;

import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.model.validate.truck.TruckNormalBookInfo;
import com.hletong.hyc.presenter.NormalBookPresenter;
import com.hletong.hyc.ui.activity.NormalBookActivity;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.ui.dialog.TrucksSelectorDialogFragment;
import com.hletong.hyc.util.Constant;

/**
 * Created by ddq on 2017/1/24.
 * 车辆挂价摘牌
 */

public class TruckBookActivity extends NormalBookActivity<Truck,BookContract.NormalPresenter> {
    @Override
    protected PayMode getSubmitParam(String cargo, Source.DeductRt deductTaxRt) {
        return new TruckNormalBookInfo(cargo, deductTaxRt, getSelectedItem());
    }

    @Override
    protected TransporterSelectorDialog<Truck> getTransporter() {
        return TrucksSelectorDialogFragment.getInstance("挂价摘牌", Constant.TRANSPORTER_ZP, getCargoUuid(), this);
    }

    @Override
    protected BookContract.NormalPresenter createPresenter() {
        return new NormalBookPresenter<>(this);
    }
}
