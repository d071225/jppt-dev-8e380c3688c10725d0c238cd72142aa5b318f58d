package com.hletong.hyc.model.validate;

import com.amap.api.location.AMapLocation;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.security.EncryptUtils;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by ddq on 2017/1/5.
 */

public class InvoiceInfo implements Validate2 {
    private String weight;
    private String count;
    private String d_psw;
    private String l_psw;

    //以下字段不需要校验
    private String invoiceUuid;
    private String tradeUuid;
    private String loadingLongitude;
    private String loadingLatitude;

    public InvoiceInfo(AMapLocation location, String weight, String count, String d_psw, String l_psw) {
        this.weight = weight;
        this.count = count;
        this.d_psw = d_psw;
        this.l_psw = l_psw;
        if (location != null) {
            loadingLongitude = String.valueOf(location.getLongitude());
            loadingLatitude = String.valueOf(location.getLatitude());
        }
    }

    public void setExtras(String invoiceUuid, String tradeUuid) {
        this.invoiceUuid = invoiceUuid;
        this.tradeUuid = tradeUuid;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isNotNull(count, baseView, "请填写货源数量")
                && Validator.isNotNull(weight, baseView, "请填写货源重量")
                && Validator.isLength(d_psw, 6, "密码必须为6位数字", baseView)
                && Validator.isLength(l_psw, 6, "密码必须为6位数字", baseView)) {
            try {
                return EasyOkHttp
                        .get(Constant.UPLOAD_INVOICE)
                        .param("unitCt", count)
                        .param("weight", weight)
                        .param("deliverPassword", EncryptUtils.md5Encrypt(d_psw))
                        .param("loadingPassword", EncryptUtils.md5Encrypt(l_psw))
                        .param("invoiceUuid", invoiceUuid)
                        .param("tradeUuid", tradeUuid)
                        .param("loadingLongitude", loadingLongitude == null ? "" : loadingLongitude)
                        .param("loadingLatitude", loadingLatitude == null ? "" : loadingLatitude)
                        .build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
