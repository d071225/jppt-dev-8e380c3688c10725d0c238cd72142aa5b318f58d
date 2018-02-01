package com.hletong.hyc.model.validate.truck;

import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/28.
 * 车辆报价，非摘牌
 */

public class TruckQuoteInfo extends TruckSubmitInfo implements Validate2 {

    public TruckQuoteInfo(String cargo, String price, Truck truck) {
        super(cargo, price, null, SourceInfo.QUOTE, truck);
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (validateInternal(baseView))
            return getQuoteRequest(baseView);
        return null;
    }
}
