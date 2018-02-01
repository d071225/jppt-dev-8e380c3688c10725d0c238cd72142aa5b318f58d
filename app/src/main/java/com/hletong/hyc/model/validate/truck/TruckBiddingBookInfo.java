package com.hletong.hyc.model.validate.truck;

import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/1/24.
 * 车辆提交竞价信息
 */

public class TruckBiddingBookInfo extends TruckSubmitInfo implements Validate2 {

    public TruckBiddingBookInfo(String cargo, String bidPrice, Source.DeductRt deductTaxRt, Truck truck) {
        super(cargo, bidPrice, deductTaxRt, SourceInfo.CB, truck);
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (validateInternal(baseView))
            return getBiddingRequest(baseView);
        return null;
    }
}
