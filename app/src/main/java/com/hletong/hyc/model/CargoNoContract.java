package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dongdaqing on 2017/5/31.
 * 长期协议价货源
 */

public class CargoNoContract implements Parcelable{
    private String agrtPriceUuid;
    private String cargoUuid;
    private double agrtUnitAmt;
    private String cargoCode;
    private String retCode;
    private double inputUnitAmt;
    private String cargoOwnerCode;
    private String unit;

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCargoUuid() {
        return cargoUuid;
    }

    public String getAgrtPriceUuid() {
        return agrtPriceUuid;
    }

    public double getAgrtUnitAmt() {
        return agrtUnitAmt;
    }

    public String getUnitAmt(){
        return agrtUnitAmt + "元/" + unit;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.agrtPriceUuid);
        dest.writeString(this.cargoUuid);
        dest.writeDouble(this.agrtUnitAmt);
        dest.writeString(this.cargoCode);
        dest.writeString(this.retCode);
        dest.writeDouble(this.inputUnitAmt);
        dest.writeString(this.cargoOwnerCode);
        dest.writeString(this.unit);
    }

    public CargoNoContract() {
    }

    protected CargoNoContract(Parcel in) {
        this.agrtPriceUuid = in.readString();
        this.cargoUuid = in.readString();
        this.agrtUnitAmt = in.readDouble();
        this.cargoCode = in.readString();
        this.retCode = in.readString();
        this.inputUnitAmt = in.readDouble();
        this.cargoOwnerCode = in.readString();
        this.unit = in.readString();
    }

    public static final Creator<CargoNoContract> CREATOR = new Creator<CargoNoContract>() {
        @Override
        public CargoNoContract createFromParcel(Parcel source) {
            return new CargoNoContract(source);
        }

        @Override
        public CargoNoContract[] newArray(int size) {
            return new CargoNoContract[size];
        }
    };
}
