package com.hletong.hyc.model;

import android.os.Parcel;

import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2017/2/16.
 */

public class PreDeListSource extends Source{
    private String biz_contact;//会管联系人
    private String unit_address;//会管地址
    private String unit_name;//会管名称
    private String loading_address;
    private String unload_address;
    private String carrier_no;

    @Override
    public String getLoadingAddress() {
        return loading_address;
    }

    @Override
    public String getUnLoadingAddress() {
        return unload_address;
    }

    @Override
    public String getSelfTradeReferUnitPrice() {
        return NumberUtil.format2f(getConfer_unit_amt()) + getUnitForFee();
    }

    public String getCarrierNo() {
        return carrier_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.biz_contact);
        dest.writeString(this.unit_address);
        dest.writeString(this.unit_name);
        dest.writeString(this.loading_address);
        dest.writeString(this.unload_address);
        dest.writeString(this.carrier_no);
    }

    public PreDeListSource() {
    }

    protected PreDeListSource(Parcel in) {
        super(in);
        this.biz_contact = in.readString();
        this.unit_address = in.readString();
        this.unit_name = in.readString();
        this.loading_address = in.readString();
        this.unload_address = in.readString();
        this.carrier_no = in.readString();
    }

    public static final Creator<PreDeListSource> CREATOR = new Creator<PreDeListSource>() {
        @Override
        public PreDeListSource createFromParcel(Parcel source) {
            return new PreDeListSource(source);
        }

        @Override
        public PreDeListSource[] newArray(int size) {
            return new PreDeListSource[size];
        }
    };
}
