package com.hletong.hyc.model.validate.truck;

import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.Transport;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.ParamsHelper;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.okhttp.request.OkRequest;

import org.json.JSONException;

import java.util.Map;

/**
 * Created by ddq on 2017/1/24.
 * 车辆-提交运力预报信息
 */

public class TruckForecastInfos extends Transport {
    private Truck mTruck;

    public TruckForecastInfos(Address mOrigin, Address mDestination, SimpleDate mSt, SimpleDate mEt, String mCapacity, String mContactTel, Truck truck) {
        super(mOrigin, mDestination, mSt, mEt, mCapacity, mContactTel);
        this.mTruck = truck;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isNotNull(mTruck, baseView, "请选择车辆"))
            return super.validate(baseView);
        return null;
    }

    @Override
    protected void getExtraParams(Map<String, String> map) {
        map.put("truckUuid", mTruck.getTruck_uuid());
    }
}
