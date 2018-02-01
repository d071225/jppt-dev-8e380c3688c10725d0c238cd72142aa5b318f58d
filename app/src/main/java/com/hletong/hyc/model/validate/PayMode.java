package com.hletong.hyc.model.validate;

/**
 * Created by ddq on 2017/3/28.
 * 挂价-自主交易，设置支付方式
 */
public interface PayMode extends Validate2 {
    //0 => 本人用E商贸通支付，1 => 会员管理单位垫付，2 => 微信支付
    void setPayMode(int payMode);

    int getPayMode();
}
