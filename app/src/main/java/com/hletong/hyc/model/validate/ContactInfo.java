package com.hletong.hyc.model.validate;

import android.text.TextUtils;

import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.ParamsHelper;
import com.hletong.mob.util.StringUtil;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/20.
 */

public class ContactInfo implements Validate2 {

    private String contact_name;
    private String taxpayer_code;

    private String contact_uuid;

    public ContactInfo(String contact_name, String taxpayer_code) {
        this.contact_name = contact_name;
        this.taxpayer_code = taxpayer_code;
    }

    public void setExtras(String contactUuid) {
        this.contact_uuid = contactUuid;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (contact_uuid != null)
            return EasyOkHttp.get(Constant.UPDATE_CONSIGNOR)
                    .tag(baseView.hashCode())
                    .param("contactName", contact_name)
                    .param("contactType", 2)
                    .param("taxpayerCode", taxpayer_code)
                    .param("contactUuid", contact_uuid)
                    .build();

        //新增
        if (Validator.isNotNull(contact_name, baseView, "公司名称不能为空")
                && (TextUtils.isEmpty(taxpayer_code) || Validator.isTaxCode(taxpayer_code, baseView)))
            return EasyOkHttp.get(Constant.ADD_CONSIGNOR)
                    .tag(baseView.hashCode())
                    .param("contactName", contact_name)
                    .param("contactType", 2)
                    .param("taxpayerCode", taxpayer_code)
                    .build();

        return null;
    }
}
