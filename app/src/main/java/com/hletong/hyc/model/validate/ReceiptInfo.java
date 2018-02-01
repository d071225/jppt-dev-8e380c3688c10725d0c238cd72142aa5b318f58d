package com.hletong.hyc.model.validate;

import com.amap.api.location.AMapLocation;
import com.hletong.hyc.model.Receipt;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.security.EncryptUtils;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by ddq on 2017/1/4.
 */

public class ReceiptInfo implements Validate2 {
    //理论上应该校验用户填写的重量和数量
    //但是测试要求按重量计费时数量可不填，按数量计费时重量可不填
    //然而没有办法判断到底是用重量计费还是数量计费，故先不做校验
    private String weight;
    private String count;
    private String r_psw;
    private String u_psw;
    private String waybillFileId;
    //下面的字段不需要校验
    private boolean dissentFlag;
    private String receiptUuid;
    private String tradeUuid;
    private String loadingLongitude;
    private String loadingLatitude;

    public ReceiptInfo(AMapLocation location, String mWeight, String mCount, String mR_psw, String mU_psw, boolean dissentFlag) {
        if (location != null) {
            loadingLongitude = String.valueOf(location.getLongitude());
            loadingLatitude = String.valueOf(location.getLatitude());
        }
        weight = mWeight;
        count = mCount;
        r_psw = mR_psw;
        u_psw = mU_psw;
        this.dissentFlag = dissentFlag;
    }

    public void setExtras(String mWaybillFileId, String mReceiptUuid, String mTradeUuid) {
        waybillFileId = mWaybillFileId;
        receiptUuid = mReceiptUuid;
        tradeUuid = mTradeUuid;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isNotAllEmpty(new String[]{count, weight}, baseView, "重量和数量请至少填一项")
                && Validator.isLength(r_psw, 6, "密码必须为6位数字", baseView)
                && Validator.isLength(u_psw, 6, "密码必须为6位数字", baseView)) {
            try {
                return EasyOkHttp
                        .get(Constant.UPLOAD_RECEIPT)
                        .param("unitCt", count.length() == 0 ? "0" : count)
                        .param("weight", weight.length() == 0 ? "0" : weight)
                        .param("receivingPassword", EncryptUtils.md5Encrypt(r_psw))
                        .param("unloadPassword", EncryptUtils.md5Encrypt(u_psw))
                        .param("receiptUuid", receiptUuid)
                        .param("tradeUuid", tradeUuid)
                        .param("dissentFlag", dissentFlag ? 1 : 0)
                        .param("unloadLongitude", loadingLongitude)
                        .param("unloadLatitude", loadingLatitude)
                        .param("waybillFileId", waybillFileId)
                        .tag(baseView.hashCode())
                        .build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //当用户填写的数量和重量与发货数量和重量不一致时，要提示用户
    public boolean isSame(Receipt mReceipt) {
        return mReceipt.isSameCount(count) &&
                mReceipt.isSameWeight(weight);
    }
}
