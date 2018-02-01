package com.hletong.hyc.model.validate.truck;

import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by ddq on 2017/3/28.
 * 车辆提交竞、挂、议价信息
 */

public class TruckSubmitInfo extends SourceInfo {
    private Truck mTruck;

    public TruckSubmitInfo(String cargo, String bidPrice, Source.DeductRt deductTaxRt, int type, Truck truck) {
        super(cargo, bidPrice, deductTaxRt, type, 1);
        this.mTruck = truck;
    }

    @Override
    protected boolean validateInternal(IBaseView baseView) {
        return Validator.isNotNull(mTruck, baseView, "请选择车辆") && super.validateInternal(baseView);
    }

    @Override
    public String getCarrierMemberSubCode() {
        return mTruck.getPersonal_member_code();
    }

    @Override
    protected String getCarrierNo() {
        return mTruck.getPlate();
    }

    @Override
    protected double getCarrierLoad() {//业务部门需求，车辆最大摘牌量100吨
        return 100;
    }
}
