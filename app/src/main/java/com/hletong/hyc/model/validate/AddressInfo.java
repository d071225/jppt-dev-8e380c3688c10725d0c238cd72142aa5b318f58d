package com.hletong.hyc.model.validate;

import android.content.Intent;

import com.hletong.hyc.model.Address;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/14.
 */

public class AddressInfo implements Validate2 {
    private Address mAddress;
    private String details;
    private String contact;
    private String tel;
    private String deep;

    private String id;

    public AddressInfo(String details, String contact, String tel, String deep) {
        this.details = details;
        this.contact = contact;
        this.tel = tel;
        this.deep = deep;
    }

    public void setAddress(Address address) {
        mAddress = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Intent getResult() {
        mAddress.setDetails(details);
        Intent intent = mAddress.setAsData();
        intent.putExtra("contact", contact);
        intent.putExtra("tel", tel);
        return intent;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isNotNull(mAddress, baseView, "请选择地址")
                && Validator.isNotNull(details, baseView, "请填写详细地址")
                && Validator.isNotNull(contact, baseView, "请填写联系人")
                && Validator.isNotNull(tel, baseView, "请填写联系人电话")) {
            if (isUpdate())
                return EasyOkHttp.get(Constant.CARGO_CONTACT_MODIFY)
                        .tag(baseView.hashCode())
                        .param("province", mAddress.getProvinceForQuery())
                        .param("city", mAddress.getCityForQuery())
                        .param("country", mAddress.getCountyForQuery())
                        .param("address", details)
                        .param("contactName", contact)
                        .param("contactTel", tel)
                        .param("waterDepth", deep)
                        .param("id", id)
                        .build();

            return EasyOkHttp.get(Constant.CARGO_CONTACT_ADD)
                    .tag(baseView.hashCode())
                    .param("province", mAddress.getProvinceForQuery())
                    .param("city", mAddress.getCityForQuery())
                    .param("country", mAddress.getCountyForQuery())
                    .param("address", details)
                    .param("contactName", contact)
                    .param("contactTel", tel)
                    .param("waterDepth", deep)
                    .build();
        }
        return null;
    }

    private boolean isUpdate() {
        return id != null;
    }
}
