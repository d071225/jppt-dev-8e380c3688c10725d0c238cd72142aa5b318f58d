package com.hletong.hyc.model;

import android.os.Parcel;

/**
 * Created by ddq on 2016/11/24.
 * 历史订单
 */
public class HistoryOrder extends Source {
    private String member_type;
    private String member_type_;
    private String carrier_no;
    private String trade_uuid;
    private String carrier_name;
    private String unit_amt;

    public String getCarrier_no() {
        return carrier_no;
    }

    public String getCarrier_name() {
        return carrier_name;
    }

    @Override
    public int getBook_ref_type() {
        if (super.getBook_ref_type() == -1){
            return getUnit_ct() > getWeight() ? 1 : 0;
        }
        return super.getBook_ref_type();
    }

    public String getTrade_uuid() {
        return trade_uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.member_type);
        dest.writeString(this.member_type_);
        dest.writeString(this.carrier_no);
        dest.writeString(this.trade_uuid);
        dest.writeString(this.carrier_name);
        dest.writeString(this.unit_amt);
    }

    public HistoryOrder() {
    }

    protected HistoryOrder(Parcel in) {
        super(in);
        this.member_type = in.readString();
        this.member_type_ = in.readString();
        this.carrier_no = in.readString();
        this.trade_uuid = in.readString();
        this.carrier_name = in.readString();
        this.unit_amt = in.readString();
    }

    public static final Creator<HistoryOrder> CREATOR = new Creator<HistoryOrder>() {
        @Override
        public HistoryOrder createFromParcel(Parcel source) {
            return new HistoryOrder(source);
        }

        @Override
        public HistoryOrder[] newArray(int size) {
            return new HistoryOrder[size];
        }
    };
}
