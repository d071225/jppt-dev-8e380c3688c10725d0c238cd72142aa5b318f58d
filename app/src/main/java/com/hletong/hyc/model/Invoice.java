package com.hletong.hyc.model;

import android.os.Parcel;

import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2016/11/9.
 * 发货单
 */
public class Invoice extends Source{
    private String create_opid;
    private double trade_weight;
    private String trade_status;
    private String invoice_uuid;
    private String invoice_unit_ct;
    private String trade_status_;
    private String invoice_code;
    private String invoice_weight;
    private String trade_uuid;
    private String carrier_no;
    private double trade_unit_ct;
    private String invoice_status_;
    private String contract_uuid;
    private String upload_dttm;
    private int invoice_status;// 0 => 待回传 1 => 已回传 A => 已作废

    @Override
    public String getAvailableTime() {
        return upload_dttm;
    }

    public int getInvoice_status() {
        return invoice_status;
    }

    public String getInvoice_status_() {
        return invoice_status_;
    }

    public String getInvoice_code() {
        return invoice_code;
    }

    public String getInvoice_uuid() {
        return invoice_uuid;
    }

    public String getTrade_uuid() {
        return trade_uuid;
    }

    public String getCarrier_no() {
        return carrier_no;
    }

    public String getTradeWeight() {
        return NumberUtil.format3f(trade_weight);
    }

    public String getTradeUnitCt() {
        return NumberUtil.format3f(trade_unit_ct);
    }

    public String getInvoiceUnitCt() {
        return NumberUtil.format3f(invoice_unit_ct);
    }

    public String getInvoiceWeight() {
        return NumberUtil.format3f(invoice_weight);
    }

    public String getContract_uuid() {
        return contract_uuid;
    }



    @Override
    public double getWeight() {
        return trade_weight;
    }

    @Override
    public double getUnit_ct() {
        return trade_unit_ct;
    }

    @Override
    public String getCarrierAndLoadingPeriod() {
        return carrier_no + " 装货日期：" + getLoadingPeriod();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.create_opid);
        dest.writeDouble(this.trade_weight);
        dest.writeString(this.trade_status);
        dest.writeString(this.invoice_uuid);
        dest.writeString(this.invoice_unit_ct);
        dest.writeString(this.trade_status_);
        dest.writeString(this.invoice_code);
        dest.writeString(this.invoice_weight);
        dest.writeString(this.trade_uuid);
        dest.writeString(this.carrier_no);
        dest.writeDouble(this.trade_unit_ct);
        dest.writeString(this.invoice_status_);
        dest.writeString(this.contract_uuid);
        dest.writeString(this.upload_dttm);
        dest.writeInt(this.invoice_status);
    }

    public Invoice() {
    }

    protected Invoice(Parcel in) {
        super(in);
        this.create_opid = in.readString();
        this.trade_weight = in.readDouble();
        this.trade_status = in.readString();
        this.invoice_uuid = in.readString();
        this.invoice_unit_ct = in.readString();
        this.trade_status_ = in.readString();
        this.invoice_code = in.readString();
        this.invoice_weight = in.readString();
        this.trade_uuid = in.readString();
        this.carrier_no = in.readString();
        this.trade_unit_ct = in.readDouble();
        this.invoice_status_ = in.readString();
        this.contract_uuid = in.readString();
        this.upload_dttm = in.readString();
        this.invoice_status = in.readInt();
    }

    public static final Creator<Invoice> CREATOR = new Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel source) {
            return new Invoice(source);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };
}
