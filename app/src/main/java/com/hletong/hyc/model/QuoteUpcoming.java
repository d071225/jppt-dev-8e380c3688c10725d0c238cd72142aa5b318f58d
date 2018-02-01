package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by ddq on 2017/3/29.
 */

public class QuoteUpcoming implements Parcelable {
    private String cargoUuid;
    private String unloadAddr;
    private String clickUnits;
    private String orginCargonKindName;
    private String loadingAddr;
    private double weight;
    private String quoteUuid;
    private double unitAmt;
    private double unitCt;
    private String carrierNo;
    private String transportType;
    private String uniqueId;
    private String status;

    public String getClickUnits() {
        return clickUnits;
    }

    public double getUnitAmt() {
        return unitAmt;
    }

    public String getCargoUuid() {
        return cargoUuid;
    }

    public String getQuoteUuid() {
        return quoteUuid;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    @Override
    public String toString() {
        if (carrierNo == null){
            return String.format(Locale.getDefault(), "%s：从%s到%s", orginCargonKindName, loadingAddr, unloadAddr);
        }else {
            return String.format(Locale.getDefault(), "%s运送%s，从%s到%s", carrierNo, orginCargonKindName, loadingAddr, unloadAddr);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cargoUuid);
        dest.writeString(this.unloadAddr);
        dest.writeString(this.clickUnits);
        dest.writeString(this.orginCargonKindName);
        dest.writeString(this.loadingAddr);
        dest.writeDouble(this.weight);
        dest.writeString(this.quoteUuid);
        dest.writeDouble(this.unitAmt);
        dest.writeDouble(this.unitCt);
        dest.writeString(this.carrierNo);
        dest.writeString(this.transportType);
        dest.writeString(this.uniqueId);
        dest.writeString(this.status);
    }

    public QuoteUpcoming() {
    }

    protected QuoteUpcoming(Parcel in) {
        this.cargoUuid = in.readString();
        this.unloadAddr = in.readString();
        this.clickUnits = in.readString();
        this.orginCargonKindName = in.readString();
        this.loadingAddr = in.readString();
        this.weight = in.readDouble();
        this.quoteUuid = in.readString();
        this.unitAmt = in.readDouble();
        this.unitCt = in.readDouble();
        this.carrierNo = in.readString();
        this.transportType = in.readString();
        this.uniqueId = in.readString();
        this.status = in.readString();
    }

    public static final Creator<QuoteUpcoming> CREATOR = new Creator<QuoteUpcoming>() {
        @Override
        public QuoteUpcoming createFromParcel(Parcel source) {
            return new QuoteUpcoming(source);
        }

        @Override
        public QuoteUpcoming[] newArray(int size) {
            return new QuoteUpcoming[size];
        }
    };
}
