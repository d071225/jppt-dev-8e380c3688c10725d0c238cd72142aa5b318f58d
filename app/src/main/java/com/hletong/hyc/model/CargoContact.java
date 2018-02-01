package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by ddq on 2017/3/14.
 * 装卸货的联系人，跟{@link BusinessContact}不同
 */

public class CargoContact implements Parcelable {
//    private String address;
//    private String contact_name;
//    private String contact_tel;
//    private String contact_uuid;
//    private String goods_address_area;
//    private String goods_address_city;
//    private String goods_address_province;
//    private String point_uuid;
//    private String water_depth;
//
//    public CargoContact(String address, String contact_name, String contact_tel,
//                         String contact_uuid, String goods_address_area, String goods_address_city,
//                         String goods_address_province, String point_uuid, String water_depth) {
//        this.address = address;
//        this.contact_name = contact_name;
//        this.contact_tel = contact_tel;
//        this.contact_uuid = contact_uuid;
//        this.goods_address_area = goods_address_area;
//        this.goods_address_city = goods_address_city;
//        this.goods_address_province = goods_address_province;
//        this.point_uuid = point_uuid;
//        this.water_depth = water_depth;
//    }
//
//    public CargoContact(Address address) {
//        this.goods_address_province = address.getRealProvince();
//        this.goods_address_city = address.getRealCity();
//        this.goods_address_area = address.getRealCounty();
//        this.address = address.getDetails();
//    }
//
//    public boolean isAddressValid(){
//        return !TextUtils.isEmpty(goods_address_province)
//                && !TextUtils.isEmpty(goods_address_city);
//    }
//
//    public void setContact_name(String contact_name) {
//        this.contact_name = contact_name;
//    }
//
//    public void setContact_tel(String contact_tel) {
//        this.contact_tel = contact_tel;
//    }
//
//    public String getWater_depth() {
//        return water_depth;
//    }
//
//    public String getGoods_address_area() {
//        return goods_address_area;
//    }
//
//    public String getGoods_address_city() {
//        return goods_address_city;
//    }
//
//    public String getGoods_address_province() {
//        return goods_address_province;
//    }
//
//    public String getPoint_uuid() {
//        return point_uuid;
//    }
//
//    public String getContact_uuid() {
//        return contact_uuid;
//    }
//
//    public String getFullAddress() {
//        return goods_address_province + goods_address_city + goods_address_area + address;
//    }
//
//    public Address getCustomAddress() {
//        return new Address(goods_address_province, goods_address_city, goods_address_area, address);
//    }
//
//    public String getContactInfo() {
//        return String.format(Locale.CHINESE, "%s(%s)", contact_name, contact_tel);
//    }
//
//    public String getContact_tel() {
//        return contact_tel;
//    }
//
//    public String getContact_name() {
//        return contact_name;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.address);
//        dest.writeString(this.contact_name);
//        dest.writeString(this.contact_tel);
//        dest.writeString(this.contact_uuid);
//        dest.writeString(this.goods_address_area);
//        dest.writeString(this.goods_address_city);
//        dest.writeString(this.goods_address_province);
//        dest.writeString(this.point_uuid);
//        dest.writeString(this.water_depth);
//    }
//
//    public CargoContact() {
//    }
//
//    protected CargoContact(Parcel in) {
//        this.address = in.readString();
//        this.contact_name = in.readString();
//        this.contact_tel = in.readString();
//        this.contact_uuid = in.readString();
//        this.goods_address_area = in.readString();
//        this.goods_address_city = in.readString();
//        this.goods_address_province = in.readString();
//        this.point_uuid = in.readString();
//        this.water_depth = in.readString();
//    }
//
//    public static final Creator<CargoContact> CREATOR = new Creator<CargoContact>() {
//        @Override
//        public CargoContact createFromParcel(Parcel source) {
//            return new CargoContact(source);
//        }
//
//        @Override
//        public CargoContact[] newArray(int size) {
//            return new CargoContact[size];
//        }
//    };

    @SerializedName(value = "member_code", alternate = "memberCode")
    private String memberCode;//会员编号
    @SerializedName(value = "contact_name", alternate = "contactName")
    private String contactName;//联系人
    @SerializedName(value = "contact_tel", alternate = "contactTel")
    private String contactTel;//联系电话
    private String province;//省
    private String city;//市
    private String country;//县
    private String address;//详细地址
    private String waterDepth;//水深
    @SerializedName(value = "contact_uuid", alternate = "contactUuid")
    private String contactUuid;//联系人流水号
    private String id;//流水号
    private String creater;//创建人
    private String createTime;//创建时间
    private String editor;//编辑人
    private String editTime;//编辑时间
    private String remark;//备注
    private String version;//版本号

    public CargoContact(String contactName, String contactTel, String province, String city, String country, String address, String waterDepth) {
        this.contactName = contactName;
        this.contactTel = contactTel;
        this.province = province;
        this.city = city;
        this.country = country;
        this.address = address;
        this.waterDepth = waterDepth;
    }

    public CargoContact(Address addr) {
        province = addr.getRealProvince();
        city = addr.getRealCity();
        country = addr.getRealCounty();
        address = addr.getDetails();
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getContactTel() {
        return contactTel;
    }

    public String getContactName() {
        return contactName;
    }

    public String getWaterDepth() {
        return waterDepth;
    }

    public String getId() {
        return id;
    }

    public String getContactUuid() {
        return contactUuid;
    }

    public void set(String contactTel, String contactName) {
        this.contactTel = contactTel;
        this.contactName = contactName;
    }

    public String getContactInfo() {
        return String.format(Locale.CHINESE, "%s(%s)", contactName, contactTel);
    }

    public boolean isAddressValid() {
        return !TextUtils.isEmpty(province)
                && !TextUtils.isEmpty(city);
    }

    public Address getCustomAddress() {
        return new Address(province, city, country, address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberCode);
        dest.writeString(this.contactName);
        dest.writeString(this.contactTel);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeString(this.address);
        dest.writeString(this.waterDepth);
        dest.writeString(this.contactUuid);
        dest.writeString(this.id);
        dest.writeString(this.creater);
        dest.writeString(this.createTime);
        dest.writeString(this.editor);
        dest.writeString(this.editTime);
        dest.writeString(this.remark);
        dest.writeString(this.version);
    }

    public CargoContact() {
    }

    protected CargoContact(Parcel in) {
        this.memberCode = in.readString();
        this.contactName = in.readString();
        this.contactTel = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.country = in.readString();
        this.address = in.readString();
        this.waterDepth = in.readString();
        this.contactUuid = in.readString();
        this.id = in.readString();
        this.creater = in.readString();
        this.createTime = in.readString();
        this.editor = in.readString();
        this.editTime = in.readString();
        this.remark = in.readString();
        this.version = in.readString();
    }

    public static final Creator<CargoContact> CREATOR = new Creator<CargoContact>() {
        @Override
        public CargoContact createFromParcel(Parcel source) {
            return new CargoContact(source);
        }

        @Override
        public CargoContact[] newArray(int size) {
            return new CargoContact[size];
        }
    };
}
