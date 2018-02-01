package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.SimpleDate;

/**
 * Created by ddq on 2017/4/5.
 * 匹配车船
 */

public class MatchedCarrier implements Parcelable {
    private String memberCode;

    private String carrierNo;
    private String matchCode;
    private String avlCarrierDt;

    private String latitude;
    private String longitude;

    private String nowAddress;

    private String contact;
    private String contactTel;

    private String leaveDt;
    private String avlCarryingCapacity;


    private String matchUuid;

    private String forecastUuid;

    private String originProvince;
    private String originCity;
    private String originCountry;

    private String destinationProvince;
    private String destinationCity;
    private String destinationCountry;

    private int memberGrade;
    private int tradeAccount;

    public String getForecastUuid() {
        return forecastUuid;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public String getDestinationProvince() {
        return destinationProvince;
    }

    public String getNowAddress() {
        return nowAddress;
    }

    public String getContactTel() {
        return contactTel;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public String getContact() {
        return contact;
    }

    public String getOriginCity() {
        return originCity;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public String getOriginProvince() {
        return originProvince;
    }

    public Address getOriginAddress() {
        return new Address(originProvince, originCity, originCountry);
    }

    public Address getDestAddress() {
        return new Address(destinationProvince, destinationCity, destinationCountry);
    }

    public String getCapacityWithUnit(){
        return avlCarryingCapacity + " 吨";
    }

    public String getMemberGrade() {
        return "好评率：" + memberGrade + "%";
    }

    public float getMemberGradeAsFloat() {
        return memberGrade / 100f;
    }

    public String getTradeAccount() {
        if (tradeAccount == 0)
            return "交易笔数：无";
        return "交易笔数：" + tradeAccount;
    }

    public String getTimeDes(String separator) {
        StringBuilder builder = new StringBuilder();
        SimpleDate start = SimpleDate.parse(avlCarrierDt);
        builder.append(start.getMonthOfYear());
        builder.append("月");
        builder.append(start.getDayOfMonth());
        builder.append("日");
        builder.append(separator);

        SimpleDate end = SimpleDate.parse(leaveDt);
        builder.append(end.getMonthOfYear());
        builder.append("月");
        builder.append(end.getDayOfMonth());
        builder.append("日");
        return builder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberCode);
        dest.writeString(this.destinationProvince);
        dest.writeString(this.matchCode);
        dest.writeString(this.avlCarrierDt);
        dest.writeString(this.latitude);
        dest.writeString(this.forecastUuid);
        dest.writeString(this.nowAddress);
        dest.writeString(this.contactTel);
        dest.writeString(this.destinationCountry);
        dest.writeString(this.leaveDt);
        dest.writeString(this.destinationCity);
        dest.writeString(this.carrierNo);
        dest.writeString(this.contact);
        dest.writeString(this.avlCarryingCapacity);
        dest.writeString(this.originCity);
        dest.writeString(this.originCountry);
        dest.writeString(this.matchUuid);
        dest.writeString(this.originProvince);
        dest.writeString(this.longitude);
    }

    public MatchedCarrier() {
    }

    protected MatchedCarrier(Parcel in) {
        this.memberCode = in.readString();
        this.destinationProvince = in.readString();
        this.matchCode = in.readString();
        this.avlCarrierDt = in.readString();
        this.latitude = in.readString();
        this.forecastUuid = in.readString();
        this.nowAddress = in.readString();
        this.contactTel = in.readString();
        this.destinationCountry = in.readString();
        this.leaveDt = in.readString();
        this.destinationCity = in.readString();
        this.carrierNo = in.readString();
        this.contact = in.readString();
        this.avlCarryingCapacity = in.readString();
        this.originCity = in.readString();
        this.originCountry = in.readString();
        this.matchUuid = in.readString();
        this.originProvince = in.readString();
        this.longitude = in.readString();
    }

    public static final Creator<MatchedCarrier> CREATOR = new Creator<MatchedCarrier>() {
        @Override
        public MatchedCarrier createFromParcel(Parcel source) {
            return new MatchedCarrier(source);
        }

        @Override
        public MatchedCarrier[] newArray(int size) {
            return new MatchedCarrier[size];
        }
    };
}
