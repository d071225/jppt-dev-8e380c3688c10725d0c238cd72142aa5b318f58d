package com.hletong.hyc.model.validate.ship;

import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by ddq on 2017/3/28.
 * 船舶提交竞、挂、议价信息
 */

public class ShipSubmitInfo extends SourceInfo {
    private Ship mShip;

    public ShipSubmitInfo(String cargo, String bidPrice, Source.DeductRt deductTaxRt, int type, Ship ship) {
        super(cargo, bidPrice, deductTaxRt, type, 2);
        this.mShip = ship;
    }

    @Override
    protected boolean validateInternal(IBaseView baseView) {
        return Validator.isNotNull(mShip, baseView, "请选择船舶")
                && super.validateInternal(baseView);
    }

    @Override
    public String getCarrierMemberSubCode() {
        if (mShip != null)
            return mShip.getPersonal_member_code();
        return null;
    }

    @Override
    protected String getCarrierNo() {
        return mShip.getShip();
    }

    @Override
    protected double getCarrierLoad() {//业务部门需求，船舶最大载重为自身载重的120%
        return mShip.getLoan() * 1.2;
    }
}
