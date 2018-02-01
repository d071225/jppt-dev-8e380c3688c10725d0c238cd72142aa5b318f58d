package com.hletong.hyc.model.validate.truck;

import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.request.OkRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by ddq on 2017/1/24.
 * 车辆提交挂价信息
 */

public class TruckNormalBookInfo extends TruckSubmitInfo implements PayMode {
    private int payMode;//0 => 本人用E商贸通支付，1 => 会员管理单位垫付，2 => 微信支付

    public TruckNormalBookInfo(String cargo, Source.DeductRt deductTaxRt, Truck truck) {
        super(cargo, null, deductTaxRt, SourceInfo.GJ, truck);
        payMode = 0;//默认自己付款
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (validateInternal(baseView)) {
            return getRequest(baseView);
        }
        return null;
    }

    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

    @Override
    public int getPayMode() {
        return payMode;
    }

    @Override
    protected void addExtras(Map<String, String> params) {
        params.put("payMode", String.valueOf(payMode));
    }
}
