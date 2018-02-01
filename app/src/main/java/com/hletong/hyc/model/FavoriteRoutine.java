package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by ddq on 2017/2/10.
 */

public class FavoriteRoutine implements Parcelable {
    private String routeUuid;
    private String routeCode;
    private String memberCode;
    private String vehicles;
    private String vehiclesType;
    private String loadingProvince;
    private String loadingCity;
    private String loadingCountry;
    private String unloadProvince;
    private String unloadCity;
    private String unloadCountry;
    private String status;

    private Address mStartAddress;
    private Address mEndAddress;

    public String getRouteUuid() {
        return routeUuid;
    }

    public String getFormatAddress() {
        return getStartAddress().buildAddress() + " - " + getEndAddress().buildAddress();
    }

    public String getActivityTitle(){
        String from = getStartAddress().getCityForDisplay();
        if (TextUtils.isEmpty(from)){
            from = getStartAddress().getRealProvince();
        }

        String to = getEndAddress().getCityForDisplay();
        if (TextUtils.isEmpty(to)){
            to = getEndAddress().getRealProvince();
        }

        return from + " - " + to;
    }

    public Address getStartAddress() {
        if (mStartAddress == null)
            mStartAddress = new Address(loadingProvince, loadingCity, loadingCountry);
        return mStartAddress;
    }

    public Address getEndAddress() {
        if (mEndAddress == null){
            mEndAddress = new Address(unloadProvince, unloadCity, unloadCountry);
        }
        return mEndAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.routeUuid);
        dest.writeString(this.routeCode);
        dest.writeString(this.memberCode);
        dest.writeString(this.vehicles);
        dest.writeString(this.vehiclesType);
        dest.writeString(this.loadingProvince);
        dest.writeString(this.loadingCity);
        dest.writeString(this.loadingCountry);
        dest.writeString(this.unloadProvince);
        dest.writeString(this.unloadCity);
        dest.writeString(this.unloadCountry);
        dest.writeString(this.status);
    }

    public FavoriteRoutine() {
    }

    protected FavoriteRoutine(Parcel in) {
        this.routeUuid = in.readString();
        this.routeCode = in.readString();
        this.memberCode = in.readString();
        this.vehicles = in.readString();
        this.vehiclesType = in.readString();
        this.loadingProvince = in.readString();
        this.loadingCity = in.readString();
        this.loadingCountry = in.readString();
        this.unloadProvince = in.readString();
        this.unloadCity = in.readString();
        this.unloadCountry = in.readString();
        this.status = in.readString();
    }

    public static final Creator<FavoriteRoutine> CREATOR = new Creator<FavoriteRoutine>() {
        @Override
        public FavoriteRoutine createFromParcel(Parcel source) {
            return new FavoriteRoutine(source);
        }

        @Override
        public FavoriteRoutine[] newArray(int size) {
            return new FavoriteRoutine[size];
        }
    };
}
