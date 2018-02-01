package com.hletong.hyc.model.validate;

import com.amap.api.location.AMapLocation;
import com.hletong.hyc.HyApplication;
import com.hletong.hyc.model.Order;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.security.EncryptUtils;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ddq on 2017/1/4.
 */

public class ContractInfo implements Validate2 {
    //下面的参数是发起请求需要的参数，并不需要校验
    private String bookUUID;
    private List<Order> mOrders;
    //下面的参数要进行完整性校验
    private List<PickerInfo> mPickerInfos;
    private String password;
    private String passwordConfirm;

    public ContractInfo(List<PickerInfo> pickerInfos, String mPassword, String mPasswordConfirm) {
        mPickerInfos = pickerInfos;
        password = mPassword;
        passwordConfirm = mPasswordConfirm;
    }

    public ContractInfo(String mBookUUID, List<Order> orders) {
        bookUUID = mBookUUID;
        this.mOrders = orders;
    }

    public void setExtras(ContractInfo mContractInfo) {
        if (mContractInfo == null)
            return;

        this.bookUUID = mContractInfo.bookUUID;
        this.mOrders = mContractInfo.mOrders;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {

        for (PickerInfo info : mPickerInfos) {
            if (!info.validate(baseView))
                return null;
        }

        if (Validator.isLength(password, 6, "卸货密码为6位数字", baseView)
                && Validator.isSame(passwordConfirm, password, baseView, "两次输入的密码不一致")) {
            try {
                Map<String, String> map = new HashMap<>();
                map.put("bookUuid", bookUUID);
                AMapLocation location = HyApplication.getAMapLocation();
                if (location != null) {
                    map.put("longitude", String.valueOf(location.getLongitude()));
                    map.put("latitude", String.valueOf(location.getLatitude()));
                }
                for (int i = 0; i < mPickerInfos.size(); i++) {
                    map.put("signSubReqDtoList[" + i + "].transporterName", mPickerInfos.get(i).getName());
                    map.put("signSubReqDtoList[" + i + "].transporterIdentityCode", mPickerInfos.get(i).getId());
                    map.put("signSubReqDtoList[" + i + "].planLoadingDttm", mPickerInfos.get(i).getTime().dateString(true, "") + mPickerInfos.get(i).getTime().timeString(true, ""));
                    map.put("signSubReqDtoList[" + i + "].bookDtlUuid", mOrders.get(i).getBookDtlUuid());
                    map.put("signSubReqDtoList[" + i + "].carrierNo", mOrders.get(i).getCarrierNo());
                    map.put("signSubReqDtoList[" + i + "].checkTransPwd", EncryptUtils.md5Encrypt(passwordConfirm));
                    map.put("signSubReqDtoList[" + i + "].transPwd", EncryptUtils.md5Encrypt(password));
                }
                return EasyOkHttp.get(Constant.SIGN_CONTRACT_TRANSPORTER).tag(baseView.hashCode()).params(map).build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static class PickerInfo {
        private String name;
        private String id;
        private SimpleDate time;

        public PickerInfo(String name, String id, SimpleDate time) {
            this.name = name;
            this.id = id;
            this.time = time;
        }

        public boolean validate(IBaseView baseView) {
            return Validator.isNotNull(name, baseView, "请填写提货人姓名")
                    && Validator.isIdCard(id, baseView)
                    && Validator.timeIsLateThanCurrent(time, baseView, "请选择预装货时间", "预装货时间不能早于现在");
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public SimpleDate getTime() {
            return time;
        }
    }
}
