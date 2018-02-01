package com.hletong.hyc.model;

import com.hletong.mob.util.NumberUtil;

public class BidInfo {
    public double transport_unit_price;//交易费
    public double other_unit_amt;//其他运费

    public String getSum() {
        return NumberUtil.format2f(transport_unit_price + other_unit_amt);
    }

    public String getCargoFee(double cargo){
        return NumberUtil.format2f((transport_unit_price + other_unit_amt) * cargo);
    }
}