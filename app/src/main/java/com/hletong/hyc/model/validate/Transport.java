package com.hletong.hyc.model.validate;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.SimpleDate;
import com.hletong.mob.util.StringUtil;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ddq on 2017/1/4.
 * 提交运力预报的参数
 */

public abstract class Transport implements Validate2 {
    private Address origin;
    private Address destination;
    private SimpleDate st;
    private SimpleDate et;
    private String capacity;
    private String contactTel;

    public Transport(Address mOrigin, Address mDestination, SimpleDate mSt, SimpleDate mEt, String mCapacity, String mContactTel) {
        origin = mOrigin;
        destination = mDestination;
        st = mSt;
        et = mEt;
        capacity = mCapacity;
        contactTel = mContactTel;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isAboveZero(capacity, baseView, "请填写可配载量", "可配载量必须大于0")
                && Validator.isNotNull(origin, baseView, "请选择起始地")
                && Validator.isNotNull(destination, baseView, "请选择目的地")
                && Validator.isNotNull(st, baseView, "请选择开始时间")
                && Validator.isNotNull(et, baseView, "请选择结束时间")
                && Validator.isSmallerThan(st, et, baseView, "结束时间不能早于开始时间")) {
            //备用电话可以不填，但是填了,就必须遵守格式
            if (contactTel.length() > 0
                    && !StringUtil.isMobileNumber(contactTel)
                    && !StringUtil.isTelePhoneOrFax(contactTel)) {
                baseView.handleError(ErrorFactory.getParamError("请输入正确的电话号码"));
                return null;
            }

            Map<String, String> map = new HashMap<>();
            getExtraParams(map);
            map.put("carrierType", String.valueOf(BuildConfig.carrier_type));//此参数用来区分车辆和船舶，不同build type值不一样，详见build.gradle
            map.put("avlCarryingCapacity", capacity);
            map.put("originProvince", origin.getProvinceForQuery());
            map.put("originCity", origin.getCityForQuery());
            map.put("originCountry", origin.getCountyForQuery());
            map.put("destinationProvince", destination.getProvinceForQuery());
            map.put("destinationCity", destination.getCityForQuery());
            map.put("destinationCountry", destination.getCountyForQuery());
            map.put("avlCarrierDt", st.dateString(true, ""));
            map.put("leaveDt", et.dateString(true, ""));
            map.put("contactTel", contactTel);

            return EasyOkHttp.get(Constant.SUBMIT_TRANSPORT_FORECAST)
                    .tag(baseView.hashCode())
                    .params(map)
                    .build();
        }
        return null;
    }

    protected abstract void getExtraParams(Map<String, String> map);
}
