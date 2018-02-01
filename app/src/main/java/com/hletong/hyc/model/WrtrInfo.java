package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dongdaqing on 2017/8/10.
 */

public class WrtrInfo implements Parcelable{
    private String bookUnitAmt;//
    private String wrtrServUnitAmt;//
    private String agrtUnitAmt;//
    private String units_;//
    private String wrtrServRecvName;//
    private String tipsMsg;

    public String getTipsMsg() {
        return tipsMsg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookUnitAmt);
        dest.writeString(this.wrtrServUnitAmt);
        dest.writeString(this.agrtUnitAmt);
        dest.writeString(this.units_);
        dest.writeString(this.wrtrServRecvName);
        dest.writeString(this.tipsMsg);
    }

    public WrtrInfo() {
    }

    protected WrtrInfo(Parcel in) {
        this.bookUnitAmt = in.readString();
        this.wrtrServUnitAmt = in.readString();
        this.agrtUnitAmt = in.readString();
        this.units_ = in.readString();
        this.wrtrServRecvName = in.readString();
        this.tipsMsg = in.readString();
    }

    public static final Creator<WrtrInfo> CREATOR = new Creator<WrtrInfo>() {
        @Override
        public WrtrInfo createFromParcel(Parcel source) {
            return new WrtrInfo(source);
        }

        @Override
        public WrtrInfo[] newArray(int size) {
            return new WrtrInfo[size];
        }
    };
}
