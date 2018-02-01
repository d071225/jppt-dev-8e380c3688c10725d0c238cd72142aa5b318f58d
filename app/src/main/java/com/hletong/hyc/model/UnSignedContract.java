package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2016/11/8.
 * 未签合同
 */
public class UnSignedContract extends SourceOrder{

    private String carrier_no_all;//车牌号

    @Override
    public String getCarrierAndLoadingPeriod() {
        if (carrier_no_all == null)
            return "装货日期：" + getLoadingPeriod();
        return carrier_no_all + " 装货日期：" + getLoadingPeriod();
    }

    @Override
    public String getPlate() {
        return carrier_no_all;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.carrier_no_all);
    }

    public UnSignedContract() {
    }

    protected UnSignedContract(Parcel in) {
        super(in);
        this.carrier_no_all = in.readString();
    }

    public static final Creator<UnSignedContract> CREATOR = new Creator<UnSignedContract>() {
        @Override
        public UnSignedContract createFromParcel(Parcel source) {
            return new UnSignedContract(source);
        }

        @Override
        public UnSignedContract[] newArray(int size) {
            return new UnSignedContract[size];
        }
    };
}
