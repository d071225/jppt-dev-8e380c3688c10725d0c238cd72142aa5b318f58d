package com.hletong.hyc.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by dongdaqing on 16/7/24.
 */
public class Address implements Parcelable {
    private static final String[] ss = {"北京市", "天津市", "上海市", "重庆市"};
    private String province;
    private String city;
    private String county;
    private String details;

    public Address() {
    }

    public Address(String province, String city, String county) {
        set(province, city, county, null);
    }

    public Address(String province, String city, String county, String details) {
        set(province, city, county, details);
    }

    protected Address(Parcel in) {
        province = in.readString();
        city = in.readString();
        county = in.readString();
        details = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(county);
        dest.writeString(details);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public void set(String province, String city, String county, String details) {
        this.province = province;
        this.city = city;
        this.county = county;
        this.details = details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    //仅用于界面展示，不要用做接口参数
    public String getCityForDisplay() {
        if (city == null || "-2".equals(city) || (isSS(province) && city.startsWith(province))) {
            if ("-1".equals(province))
                return "全国";
            return province;
        }

        return city;
    }

    public String getDetails() {
        return details;
    }

    public String getRealProvince() {
        return province;
    }

    public String getRealCity() {
        return city;
    }

    public String getRealCounty() {
        return county;
    }

    //不要通过这个方法获取字段然后拼接地址展示，这个方法的返回值用于当做查询参数。
    // 下面有对应的方法生成地址
    public String getProvinceForQuery() {
        if (province == null || "-1".equals(province))
            return "";

        return province;
    }

    //同上
    public String getCityForQuery() {
        if ("-1".equals(province) || city == null || "-2".equals(city))
            return "";

        return city;
    }

    public String getCountyForQuery() {
        if ("-1".equals(province)//全国
                || "-2".equals(city) //全省
                || county == null
                || "-3".equals(county)) {
            return "";
        }

        return county;
    }

    @Override
    public String toString() {
        return province +
                " " +
                city +
                " " +
                county +
                " " +
                details;
    }

    public boolean isSameCity(Address address) {
        return isSameCity(this, address);
    }

    public boolean isSameCounty(Address address) {
        return isSameCounty(this, address);
    }

    private static boolean isSameProvince(Address a1, Address a2) {
        if (a1 == null || a2 == null || a1.getRealProvince() == null || a2.getRealProvince() == null)
            return false;

        return a1.getRealProvince().equals(a2.getRealProvince());
    }

    private static boolean isSameCity(Address a1, Address a2) {
        if (!isSameProvince(a1, a2))
            return false;

        if (a1.getRealCity() == null)
            return false;

        return a1.getRealCity().equals(a2.getRealCity());
    }

    private static boolean isSameCounty(Address a1, Address a2) {
        if (!isSameCity(a1, a2))
            return false;

        if (a1.getRealCounty() == null)
            return false;

        return a1.getRealCounty().equals(a2.getRealCounty());
    }

    private static boolean isSS(String input) {
        if (input == null)
            return false;

        for (String s : ss) {
            if (s.equals(input))
                return true;
        }
        return false;
    }

    /**
     * 直辖市的city字段是 xx市辖区，可以将之替换为区
     */
    public static String chooseCity(String province, String city) {
        /**
         * 这里的情况：
         *  1.直辖市 -> 返回情况两种，1.北京市 北京市辖区 东城区 xxx ,2.北京市 东城区  xxx，从栗子看：市无论怎么变都用province字段
         *  2.非直辖市 -> 直接返回市
         */
        if (isSS(province) || TextUtils.isEmpty(city)) {
            return province;
        }
//        if (TextUtils.isEmpty(city)) {
//            return "全省";
//        }
        return city;
    }

    public static String chooseCountry(String province, String city, String country) {
        if (!isSS(province))//非直辖市
        {
            if (TextUtils.isEmpty(city)) {
                return "全省";
            } else if (TextUtils.isEmpty(country))
                return "全市";
            else
                return country;
        }

        if (!TextUtils.isEmpty(country)){
            return country;
        }else {
            if (city == null || city.startsWith(province))
                return "全市";
            return city;
        }
    }

    public String buildAddress(char separator, boolean withDetails) {
        String result;
        if ("-1".equals(province)) {
            result = "全国";
        } else {
            result = province == null ? "" : province;
            //这要排除 直辖市中的重复内容，比如北京市 北京市辖区 东城区，其中北京市辖区就是要排除的
            if (!isSS(province) || !city.startsWith(province)) {
                if (city != null && !"-2".equals(city))
                    result += (separator + city);
            }

            if (county != null && !"-3".equals(county)) {
                result += (separator + county);
            }

            if (withDetails && details != null) {
                result += (separator + details);
            }
        }

        return result;
    }

    public String buildAddress() {
        return buildAddress(' ', false);
    }

    public Intent setAsData() {
        Intent intent = new Intent();
        setAddress(intent, this);
        return intent;
    }

    public static Address getAddress(Intent intent) {
        Address address = new Address();
        address.province = intent.getStringExtra("province");
        address.city = intent.getStringExtra("city");
        address.county = intent.getStringExtra("county");
        address.details = intent.getStringExtra("details");
        return address;
    }

    public static Address getAddress(Bundle intent) {
        Address address = new Address();
        address.province = intent.getString("province");
        address.city = intent.getString("city");
        address.county = intent.getString("county");
        address.details = intent.getString("details");
        return address;
    }

    public static void setAddress(Intent intent, Address address) {
        Bundle bundle = new Bundle();
        setAddress(bundle, address);
        intent.putExtras(bundle);
    }

    public static void setAddress(Bundle bundle, Address address) {
        bundle.putString("province", address.province);
        bundle.putString("city", address.city);
        bundle.putString("county", address.county);
        bundle.putString("details", address.details);
    }

    public static void setAddress(Bundle intent, Address[] addresses) {
        intent.putInt("address_count", addresses.length + 1);
        for (int i = 0; i < addresses.length; i++) {
            intent.putString(getIndexedKey("province", i), addresses[i].province);
            intent.putString(getIndexedKey("city", i), addresses[i].city);
            intent.putString(getIndexedKey("county", i), addresses[i].county);
            intent.putString(getIndexedKey("details", i), addresses[i].details);
        }
    }

    public static Address getAddressByIndex(Bundle intent, int index) {
        if (index >= intent.getInt("address_count", 0))
            return null;
        Address address = new Address();
        address.province = intent.getString(getIndexedKey("province", index));
        address.city = intent.getString(getIndexedKey("city", index));
        address.county = intent.getString(getIndexedKey("county", index));
        address.details = intent.getString(getIndexedKey("details", index));
        return address;
    }

    private static String getIndexedKey(String originalKey, int index) {
        return String.format(Locale.US, "address[%d].%s", index, originalKey);
    }
}
