package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by ddq on 2017/3/15.
 * 收发货的联系人，跟{@link CargoContact}不同
 */

public class BusinessContact implements Parcelable{
    private String contact_name;
    private String contact_tel;
    private String contact_uuid;
    private String member_code;
    private String taxpayer_code;

    public String getContact_name() {
        return contact_name;
    }

    public String getTaxpayer_code() {
        return taxpayer_code;
    }

    public String getTaxpayerCode() {
        if (TextUtils.isEmpty(taxpayer_code))
            return "无";
        return taxpayer_code;
    }

    public String getContact_uuid() {
        return contact_uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contact_name);
        dest.writeString(this.contact_tel);
        dest.writeString(this.contact_uuid);
        dest.writeString(this.member_code);
        dest.writeString(this.taxpayer_code);
    }

    public BusinessContact() {
    }

    protected BusinessContact(Parcel in) {
        this.contact_name = in.readString();
        this.contact_tel = in.readString();
        this.contact_uuid = in.readString();
        this.member_code = in.readString();
        this.taxpayer_code = in.readString();
    }

    public static final Creator<BusinessContact> CREATOR = new Creator<BusinessContact>() {
        @Override
        public BusinessContact createFromParcel(Parcel source) {
            return new BusinessContact(source);
        }

        @Override
        public BusinessContact[] newArray(int size) {
            return new BusinessContact[size];
        }
    };
}
