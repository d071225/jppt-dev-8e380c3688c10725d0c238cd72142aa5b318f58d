package com.hletong.hyc.model.validate.ship;

import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/28.
 * 船舶报价，非摘牌
 */

public class ShipQuoteInfo extends ShipSubmitInfo implements Validate2 {

    public ShipQuoteInfo(String cargo, String price, Ship ship) {
        super(cargo, price, null, SourceInfo.QUOTE, ship);
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (validateInternal(baseView))
            return getQuoteRequest(baseView);
        return null;
    }
}
