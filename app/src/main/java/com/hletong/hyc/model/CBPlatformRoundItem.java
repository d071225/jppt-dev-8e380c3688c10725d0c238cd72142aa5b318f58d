package com.hletong.hyc.model;

import com.hletong.mob.util.NumberUtil;

/**
 * Created by dongdaqing on 2017/7/19.
 */

public class CBPlatformRoundItem extends CBRoundItem{
    private double transportUnitPrice;
    private double otherUnitAmt;

    public CBPlatformRoundItem(boolean mEmpty) {
        super(mEmpty);
    }

    public String getTransportUnitPrice() {
        if (isEmpty())
            return "-";
        return NumberUtil.format2f(transportUnitPrice);
    }

    public String getOtherUnitAmt() {
        if (isEmpty())
            return "-";
        return NumberUtil.format2f(otherUnitAmt);
    }
}
