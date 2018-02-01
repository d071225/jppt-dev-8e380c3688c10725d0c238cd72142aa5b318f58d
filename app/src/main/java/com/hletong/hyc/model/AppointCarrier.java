package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2017/3/20.
 * 货源预报-指定车船
 */

public class AppointCarrier implements Parcelable {

    private String personal_member_name;
    private String personal_member_code;
    private String unit_uuid;
    private String create_dt;

    private String member_address_province;
    private String member_address_city;
    private String member_address_area;
    private String member_address;

    private String member_name;
    private String member_code;
    private String member_type;
    private String member_status;
    private String phone;
    private String contact_code;

    private String plate;
    private String max_width;
    private String identify;
    private String member_uuid;
    private String truck_name;

    private String version;

    private String truck_type;
    private String max_length;
    private String truck_uuid;
    private String max_height;
    private String max_heavy;

    private String recommend;
    private String ship_uuid;
    private String deep;
    private String ton;
    private String ship_length;
    private String net_ton;
    private String ship_type;
    private String nationality;
    private String ship;
    private String ship_status;
    private String hatch_no;
    private String above_height;
    private String full_draft;
    private String ship_width;
    private String ship_name;
    private String load_ton;
    private String nationality_cert;

    private int trade_account;
    private float member_grade;

    public String getCarrier(int type) {
        if (type == 1) {//车辆
            return plate;
        }
        return ship_name;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getAddress() {
        return new Address(member_address_province, member_address_city, member_address_area, member_address).buildAddress();
    }

    public String getCarrierUuid(){
        if (truck_uuid != null)
            return truck_uuid;
        return ship_uuid;
    }

    public String getMemberGrade() {
        return "好评率：" + member_grade + "%";
    }

    public float getMember_grade() {
        return member_grade / 100.0f;
    }

    public String getTradeCount(){
        return "交易笔数：" + (trade_account == 0 ? "无" : trade_account);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof AppointCarrier) {
            AppointCarrier ac = (AppointCarrier) obj;
            return  (ship_uuid != null && ship_uuid.equals(ac.ship_uuid)) ||
                    (truck_uuid != null && truck_uuid.equals(ac.truck_uuid));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (ship_uuid + truck_uuid).hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phone);
        dest.writeString(this.personal_member_name);
        dest.writeString(this.unit_uuid);
        dest.writeString(this.truck_type);
        dest.writeString(this.member_name);
        dest.writeString(this.max_length);
        dest.writeString(this.create_dt);
        dest.writeString(this.member_address_province);
        dest.writeString(this.member_address_city);
        dest.writeString(this.member_address_area);
        dest.writeString(this.member_address);
        dest.writeString(this.member_code);
        dest.writeString(this.plate);
        dest.writeString(this.member_type);
        dest.writeString(this.max_width);
        dest.writeString(this.identify);
        dest.writeString(this.member_uuid);
        dest.writeString(this.truck_name);
        dest.writeString(this.personal_member_code);
        dest.writeString(this.version);
        dest.writeString(this.member_status);
        dest.writeString(this.contact_code);
        dest.writeString(this.truck_uuid);
        dest.writeString(this.max_height);
        dest.writeString(this.max_heavy);
        dest.writeString(this.recommend);
        dest.writeString(this.ship_uuid);
        dest.writeString(this.deep);
        dest.writeString(this.ton);
        dest.writeString(this.ship_length);
        dest.writeString(this.net_ton);
        dest.writeString(this.ship_type);
        dest.writeString(this.nationality);
        dest.writeString(this.ship);
        dest.writeString(this.ship_status);
        dest.writeString(this.hatch_no);
        dest.writeString(this.above_height);
        dest.writeString(this.full_draft);
        dest.writeString(this.ship_width);
        dest.writeString(this.ship_name);
        dest.writeString(this.load_ton);
        dest.writeString(this.nationality_cert);
    }

    public AppointCarrier() {
    }

    protected AppointCarrier(Parcel in) {
        this.phone = in.readString();
        this.personal_member_name = in.readString();
        this.unit_uuid = in.readString();
        this.truck_type = in.readString();
        this.member_name = in.readString();
        this.max_length = in.readString();
        this.create_dt = in.readString();
        this.member_address_province = in.readString();
        this.member_address_city = in.readString();
        this.member_address_area = in.readString();
        this.member_address = in.readString();
        this.member_code = in.readString();
        this.plate = in.readString();
        this.member_type = in.readString();
        this.max_width = in.readString();
        this.identify = in.readString();
        this.member_uuid = in.readString();
        this.truck_name = in.readString();
        this.personal_member_code = in.readString();
        this.version = in.readString();
        this.member_status = in.readString();
        this.contact_code = in.readString();
        this.truck_uuid = in.readString();
        this.max_height = in.readString();
        this.max_heavy = in.readString();
        this.recommend = in.readString();
        this.ship_uuid = in.readString();
        this.deep = in.readString();
        this.ton = in.readString();
        this.ship_length = in.readString();
        this.net_ton = in.readString();
        this.ship_type = in.readString();
        this.nationality = in.readString();
        this.ship = in.readString();
        this.ship_status = in.readString();
        this.hatch_no = in.readString();
        this.above_height = in.readString();
        this.full_draft = in.readString();
        this.ship_width = in.readString();
        this.ship_name = in.readString();
        this.load_ton = in.readString();
        this.nationality_cert = in.readString();
    }

    public static final Creator<AppointCarrier> CREATOR = new Creator<AppointCarrier>() {
        @Override
        public AppointCarrier createFromParcel(Parcel source) {
            return new AppointCarrier(source);
        }

        @Override
        public AppointCarrier[] newArray(int size) {
            return new AppointCarrier[size];
        }
    };
}
