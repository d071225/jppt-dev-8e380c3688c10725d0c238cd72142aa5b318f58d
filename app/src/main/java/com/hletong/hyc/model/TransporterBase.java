package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.dialog.selector.IItemShow;

/**
 * Created by ddq on 2017/1/24.
 */

public class TransporterBase implements Parcelable, IItemShow {
    private String member_type;
    private String member_uuid;
    private String member_code;//主账号的
    private String member_name;
    private String personal_member_code;//子账号自己的

    private String unit_uuid;
    private String create_dt;

    private String member_status;
    private String member_address_province;
    private String member_address_city;
    private String member_address_area;
    private String member_address;

    private int member_grade;
    private int trade_account;
    private String remark;

    private String longitude;
    private String latitude;

    private String end_dt;

    private String member_tel;
    private String phone;
    private String dentity_no;

    private String register_from;
    private String register_dt;

    private String version;

    @Override
    public String getValue() {
        return "";
    }

    public String getMember_tel() {
        if (phone != null)
            return phone;
        return member_tel;
    }

    public double getLoan() {
        return 0;
    }

    public String getPersonal_member_code() {
        return personal_member_code;
    }

    public String getTradeCount() {
        return "交易笔数：" + trade_account;
    }

    public String getMemberGrade() {
        return "好评率：" + member_grade + "%";
    }

    public float getMember_grade() {
        return member_grade / 100.0f;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.member_type);
        dest.writeString(this.member_code);
        dest.writeString(this.latitude);
        dest.writeString(this.unit_uuid);
        dest.writeString(this.member_address_area);
        dest.writeString(this.member_status);
        dest.writeString(this.create_dt);
        dest.writeString(this.member_address_province);
        dest.writeString(this.longitude);
        dest.writeString(this.end_dt);
        dest.writeString(this.member_tel);
        dest.writeString(this.member_address);
        dest.writeString(this.register_from);
        dest.writeString(this.member_address_city);
        dest.writeString(this.member_name);
        dest.writeString(this.version);
        dest.writeString(this.register_dt);
        dest.writeString(this.phone);
        dest.writeString(this.dentity_no);
        dest.writeString(this.personal_member_code);
        dest.writeString(this.member_uuid);
    }

    public TransporterBase() {
    }

    protected TransporterBase(Parcel in) {
        this.member_type = in.readString();
        this.member_code = in.readString();
        this.latitude = in.readString();
        this.unit_uuid = in.readString();
        this.member_address_area = in.readString();
        this.member_status = in.readString();
        this.create_dt = in.readString();
        this.member_address_province = in.readString();
        this.longitude = in.readString();
        this.end_dt = in.readString();
        this.member_tel = in.readString();
        this.member_address = in.readString();
        this.register_from = in.readString();
        this.member_address_city = in.readString();
        this.member_name = in.readString();
        this.version = in.readString();
        this.register_dt = in.readString();
        this.phone = in.readString();
        this.dentity_no = in.readString();
        this.personal_member_code = in.readString();
        this.member_uuid = in.readString();
    }

    public static final Creator<TransporterBase> CREATOR = new Creator<TransporterBase>() {
        @Override
        public TransporterBase createFromParcel(Parcel source) {
            return new TransporterBase(source);
        }

        @Override
        public TransporterBase[] newArray(int size) {
            return new TransporterBase[size];
        }
    };
}
