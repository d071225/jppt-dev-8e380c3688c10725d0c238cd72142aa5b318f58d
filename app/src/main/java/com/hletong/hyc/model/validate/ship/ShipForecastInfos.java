package com.hletong.hyc.model.validate.ship;

import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.validate.Transport;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.okhttp.request.OkRequest;

import java.util.Map;

/**
 * Created by ddq on 2017/1/24.
 * 船舶-提交运力预报信息
 */

public class ShipForecastInfos extends Transport {
    private Ship mShip;

    public ShipForecastInfos(Address mOrigin, Address mDestination, SimpleDate mSt, SimpleDate mEt, String mCapacity, String mContactTel, Ship ship) {
        super(mOrigin, mDestination, mSt, mEt, mCapacity, mContactTel);
        this.mShip = ship;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isNotNull(mShip, baseView, "请选择船舶"))
            return super.validate(baseView);
        return null;
    }

    @Override
    protected void getExtraParams(Map<String, String> map) {
        map.put("shipUuid", mShip.getShip_uuid());
    }
}
