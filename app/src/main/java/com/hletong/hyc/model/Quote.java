package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;

/**
 * Created by ddq on 2017/3/27.
 * 承运人的报价信息
 */

public class Quote implements Parcelable{
    private String quoteUuid;
    private String quoteCode;

    private String cargoUuid;

    private String memberCode;
    private String memberTel;
    private String memberSubCode;
    private String memberName;
    private float memberGrade;
    private int tradeAccount;

    private String createOpid;
    private String createTs;

    private double weight;

    private double unitAmt;
    private double unitCt;
    private String carrierNo;

    private String status;
    private String status_;

    private String warrantStatus;
    private String warrantStatus_;

    private int version;

    private int book_type;
    private String units;

    private SimpleDate mSimpleDate;

    public String getUnits() {
        return units;
    }

    public int getBook_type() {
        return book_type;
    }

    public void update(String ct, String price) {
        try {
            this.unitAmt = Double.parseDouble(price);
            double tmp = Double.parseDouble(ct);
            if (book_type == 0)
                weight = tmp;
            else
                unitCt = tmp;
        } catch (NumberFormatException w) {
            w.printStackTrace();
        }
    }

    public String getMemberCode() {
        return memberCode;
    }

    public float getMemberGrade() {
        return memberGrade;
    }

    public int getTradeAccount() {
        return tradeAccount;
    }

    public String getCargoLabel() {
        if (book_type == 0)
            return "承运重量";
        return "承运数量";
    }

    public double getCargo() {
        if (book_type == 0)
            return weight;
        return unitCt;
    }

    public String getCargoWithUnit() {
        return NumberUtil.format3f(getCargo()) + units;
    }

    public String getPriceWithUnit() {
        return NumberUtil.format2f(unitAmt) + "元/" + units;
    }

    public String getTotalPrice() {
        return NumberUtil.format2f(unitAmt * getCargo()) + "元";
    }

    public void setBook_type(int book_type) {
        this.book_type = book_type;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getQuoteUuid() {
        return quoteUuid;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public String getCargoUuid() {
        return cargoUuid;
    }

    public double getWeight() {
        return weight;
    }

    public double getUnitAmt() {
        return unitAmt;
    }

    public String getUnitAmt(String units) {
        return NumberUtil.format2f(unitAmt) + "元/" + units;
    }

    public double getUnitCt() {
        return unitCt;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberSubCode() {
        return memberSubCode;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public String getCreateTs() {
        if (mSimpleDate == null) {
            mSimpleDate = SimpleDate.parse(createTs);
        }
        if (mSimpleDate != null)
            return mSimpleDate.getDateDescriptionWithTime();
        return null;
    }

    public String getCargoByBookRefType(int type, String unit) {
        if (type == 0)
            return NumberUtil.format3f(weight) + unit;
        return NumberUtil.format3f(unitCt) + unit;
    }

    public int getQuoteType() {
        switch (status) {
            case "00"://待确认
            case "10"://已确认-待摘牌
                return 0;
            case "20"://已摘牌
            case "A"://已取消
                return 1;
            default://未知类型
                return 99;
        }
    }

    public String getStatus() {
        String result = status_;

        if (warrantStatus_ != null)
            result += "|" + warrantStatus_;

        return result;
    }

    public String getWarrantStatus() {
        return warrantStatus;
    }

    public String getRealStatus() {
        return status;
    }

    public boolean isStatus(String s) {
        return status != null && status.equals(s);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quoteUuid);
        dest.writeString(this.quoteCode);
        dest.writeString(this.cargoUuid);
        dest.writeString(this.memberCode);
        dest.writeString(this.memberTel);
        dest.writeString(this.memberSubCode);
        dest.writeString(this.memberName);
        dest.writeString(this.createOpid);
        dest.writeString(this.createTs);
        dest.writeDouble(this.weight);
        dest.writeDouble(this.unitAmt);
        dest.writeDouble(this.unitCt);
        dest.writeString(this.carrierNo);
        dest.writeString(this.status);
        dest.writeString(this.status_);
        dest.writeString(this.warrantStatus);
        dest.writeString(this.warrantStatus_);
        dest.writeInt(this.version);
        dest.writeInt(this.book_type);
        dest.writeString(this.units);
    }

    public Quote() {
    }

    protected Quote(Parcel in) {
        this.quoteUuid = in.readString();
        this.quoteCode = in.readString();
        this.cargoUuid = in.readString();
        this.memberCode = in.readString();
        this.memberTel = in.readString();
        this.memberSubCode = in.readString();
        this.memberName = in.readString();
        this.createOpid = in.readString();
        this.createTs = in.readString();
        this.weight = in.readDouble();
        this.unitAmt = in.readDouble();
        this.unitCt = in.readDouble();
        this.carrierNo = in.readString();
        this.status = in.readString();
        this.status_ = in.readString();
        this.warrantStatus = in.readString();
        this.warrantStatus_ = in.readString();
        this.version = in.readInt();
        this.book_type = in.readInt();
        this.units = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel source) {
            return new Quote(source);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };
}
