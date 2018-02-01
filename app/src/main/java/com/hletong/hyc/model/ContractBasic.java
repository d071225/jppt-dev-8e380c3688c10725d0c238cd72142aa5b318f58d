package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.SimpleDate;

/**
 * Created by ddq on 2016/11/8.
 */
public class ContractBasic implements Parcelable{
    private String memberName;
    private String platformName;
    private String gzPlatName;
    private int currDate;

    public String getMemberName() {
        return memberName;
    }

    public String getGzPlatName() {
        return gzPlatName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public int getCurrDate() {
        return currDate;
    }

    public String getCurrDateInChinese(){
        SimpleDate sd = SimpleDate.parse(String.valueOf(currDate));
        if (sd == null)
            return null;
        return sd.chineseFormat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberName);
        dest.writeString(this.platformName);
        dest.writeInt(this.currDate);
    }

    public ContractBasic() {
    }

    protected ContractBasic(Parcel in) {
        this.memberName = in.readString();
        this.platformName = in.readString();
        this.currDate = in.readInt();
    }

    public static final Creator<ContractBasic> CREATOR = new Creator<ContractBasic>() {
        @Override
        public ContractBasic createFromParcel(Parcel source) {
            return new ContractBasic(source);
        }

        @Override
        public ContractBasic[] newArray(int size) {
            return new ContractBasic[size];
        }
    };
}
