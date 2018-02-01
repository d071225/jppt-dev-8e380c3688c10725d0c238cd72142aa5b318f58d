package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.SimpleDate;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by ddq on 2017/3/8.
 * 货源开票信息，货方APP
 */

public class SourceInvoice implements Parcelable{
    private List<InvoiceDetail> invoiceDetailList;
    private double sInvoiceTotalAmt;//需开票
    private double rInvoiceTotalAmt;//已开票

    public double getsTotalBilling() {
        return sInvoiceTotalAmt;
    }

    public double getRemainBilling() {
        return rInvoiceTotalAmt;
    }

    public double getBilled() {
        return sInvoiceTotalAmt - rInvoiceTotalAmt;
    }

    public int getBilledItem() {
        if (invoiceDetailList != null)
            return invoiceDetailList.size();
        return 0;
    }

    public List<InvoiceDetail> getInvoiceDetailList() {
        return invoiceDetailList;
    }

    public List<HashMap<String, String>> getBillingInfo() {
        if (ParamUtil.isEmpty(invoiceDetailList))
            return null;

        List<HashMap<String, String>> maps = new ArrayList<>();
        for (InvoiceDetail invoiceDetail : invoiceDetailList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", invoiceDetail.getTitle());
            map.put("time", invoiceDetail.getTime());
            maps.add(map);
        }
        return maps;
    }

    //开票信息
    public static class InvoiceDetail implements Parcelable{
        private double rInvoiceAmt;
        private String billStatuesEnum;
        private String billCode;
        private String transDt;
        private String billStatuesEnum_;
        private String formattedDT;

        public String getTitle() {
            return String.format(Locale.CHINESE, "%s(%s元)", billCode, rInvoiceAmt);
        }

        public String getTime() {
            if (formattedDT == null) {
                SimpleDate simpleDate = SimpleDate.parse(transDt);
                formattedDT = simpleDate.chineseFormatWithoutYear() + simpleDate.timeString(true, ":");
            }
            return formattedDT;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.rInvoiceAmt);
            dest.writeString(this.billStatuesEnum);
            dest.writeString(this.billCode);
            dest.writeString(this.transDt);
            dest.writeString(this.billStatuesEnum_);
            dest.writeString(this.formattedDT);
        }

        public InvoiceDetail() {
        }

        protected InvoiceDetail(Parcel in) {
            this.rInvoiceAmt = in.readDouble();
            this.billStatuesEnum = in.readString();
            this.billCode = in.readString();
            this.transDt = in.readString();
            this.billStatuesEnum_ = in.readString();
            this.formattedDT = in.readString();
        }

        public static final Creator<InvoiceDetail> CREATOR = new Creator<InvoiceDetail>() {
            @Override
            public InvoiceDetail createFromParcel(Parcel source) {
                return new InvoiceDetail(source);
            }

            @Override
            public InvoiceDetail[] newArray(int size) {
                return new InvoiceDetail[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.invoiceDetailList);
        dest.writeDouble(this.sInvoiceTotalAmt);
        dest.writeDouble(this.rInvoiceTotalAmt);
    }

    public SourceInvoice() {
    }

    protected SourceInvoice(Parcel in) {
        this.invoiceDetailList = in.createTypedArrayList(InvoiceDetail.CREATOR);
        this.sInvoiceTotalAmt = in.readDouble();
        this.rInvoiceTotalAmt = in.readDouble();
    }

    public static final Creator<SourceInvoice> CREATOR = new Creator<SourceInvoice>() {
        @Override
        public SourceInvoice createFromParcel(Parcel source) {
            return new SourceInvoice(source);
        }

        @Override
        public SourceInvoice[] newArray(int size) {
            return new SourceInvoice[size];
        }
    };
}
