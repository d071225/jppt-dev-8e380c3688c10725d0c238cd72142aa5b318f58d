package com.hletong.hyc.model.validate.ship;

import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/1/24.
 * 船舶提交竞价信息
 */

public class ShipBiddingBookInfo extends ShipSubmitInfo implements Validate2{

    public ShipBiddingBookInfo(String cargo, String bidPrice, Source.DeductRt deductTaxRt, Ship ship) {
        super(cargo, bidPrice, deductTaxRt, SourceInfo.CB, ship);
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (validateInternal(baseView))
            return getBiddingRequest(baseView);
        return null;
    }
}
