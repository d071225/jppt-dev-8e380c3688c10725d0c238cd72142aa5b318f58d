package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2016/11/8.
 */
public class Order implements Parcelable {
    private double wrtrServUnitAmt;
    private String wrtrTipsMsg;
    private double bookAmt;
    private String carrierNo;
    private double bookUnitAmt;
    private String bookDtlUuid;
    private double bookUnitCt;
    private double bookWeight;
    private double transportUnitPrice;
    private double otherUnitAmt;

    public double getBookAmt() {
        return bookAmt;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public double getBookUnitAmt() {
        return bookUnitAmt;
    }

    public String getBookDtlUuid() {
        return bookDtlUuid;
    }


    public String getBookWeight() {
        return NumberUtil.format3f(bookWeight);
    }


    public String getBookUnitCt() {
        return NumberUtil.format3f(bookUnitCt);
    }

    public double getCargo(int book_ref_type) {
        if (book_ref_type == 0)
            return bookWeight;
        return bookUnitCt;
    }

    public double getTransportUnitPrice() {
        return transportUnitPrice;
    }

    public double getOtherUnitAmt() {
        return otherUnitAmt;
    }

    public String getCargoDesc(String unit) {
        return getBookUnitCt() + unit + "|" + getBookWeight() + "吨";
    }

    public boolean showWrtrTips(){
        return wrtrServUnitAmt > 0;
    }

    public String getWrtrTipsMsg() {
        return wrtrTipsMsg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.wrtrServUnitAmt);
        dest.writeString(this.wrtrTipsMsg);
        dest.writeDouble(this.bookAmt);
        dest.writeString(this.carrierNo);
        dest.writeDouble(this.bookUnitAmt);
        dest.writeString(this.bookDtlUuid);
        dest.writeDouble(this.bookUnitCt);
        dest.writeDouble(this.bookWeight);
    }

    public Order() {
    }

    protected Order(Parcel in) {
        this.wrtrServUnitAmt = in.readDouble();
        this.wrtrTipsMsg = in.readString();
        this.bookAmt = in.readDouble();
        this.carrierNo = in.readString();
        this.bookUnitAmt = in.readDouble();
        this.bookDtlUuid = in.readString();
        this.bookUnitCt = in.readDouble();
        this.bookWeight = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
