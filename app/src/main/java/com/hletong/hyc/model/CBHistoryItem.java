package com.hletong.hyc.model;

import android.os.Parcel;

import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2016/11/16.
 */
public class CBHistoryItem extends Source{
    private double bid_unit_ct;
    private String bid_uuid;
    private String last_mod_ts;//显示时用这个时间,其他时间字段都没有值
    private double bid_unit_amt;
    private String carrier_no;
    private double bid_weight;
    private String bidder_code;

    @Override
    public String getCarrierAndLoadingPeriod() {
        return carrier_no + " 装货日期：" + getLoadingPeriod();
    }

    @Override
    public double getWeight() {
        return bid_weight;
    }

    @Override
    public double getUnit_ct() {
        return bid_unit_ct;
    }

    @Override
    public String getAvailableTime() {
        return last_mod_ts;
    }

    @Override
    public int getBook_ref_type() {
        return bid_unit_ct > bid_weight ? 1 : 0;
    }

    public String getBidder_code() {
        return bidder_code;
    }

    public String getBidPrice() {
        return NumberUtil.format2f(bid_unit_amt);
    }

    public String getBidUuid() {
        return bid_uuid;
    }

    public boolean canModifyBidPrice() {
        return "0".equals(getStatus());//竞价中的货源才能修改价格
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.bid_unit_ct);
        dest.writeString(this.bid_uuid);
        dest.writeString(this.last_mod_ts);
        dest.writeDouble(this.bid_unit_amt);
        dest.writeString(this.carrier_no);
        dest.writeDouble(this.bid_weight);
        dest.writeString(this.bidder_code);
    }

    public CBHistoryItem() {
    }

    protected CBHistoryItem(Parcel in) {
        super(in);
        this.bid_unit_ct = in.readDouble();
        this.bid_uuid = in.readString();
        this.last_mod_ts = in.readString();
        this.bid_unit_amt = in.readDouble();
        this.carrier_no = in.readString();
        this.bid_weight = in.readDouble();
        this.bidder_code = in.readString();
    }

    public static final Creator<CBHistoryItem> CREATOR = new Creator<CBHistoryItem>() {
        @Override
        public CBHistoryItem createFromParcel(Parcel source) {
            return new CBHistoryItem(source);
        }

        @Override
        public CBHistoryItem[] newArray(int size) {
            return new CBHistoryItem[size];
        }
    };
}
