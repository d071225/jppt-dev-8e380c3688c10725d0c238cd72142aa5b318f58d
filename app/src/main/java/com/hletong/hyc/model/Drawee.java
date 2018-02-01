package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2017/3/15.
 * 受票方信息
 */

public class Drawee implements Parcelable {
    private String invoice_address;
    private String invoice_bank;
    private String invoice_bank_code;
    private String invoice_file;
    private String invoice_name;
    private String invoice_taxpayer;
    private String invoice_tel;
    private String member_uuid;

    public Drawee(String invoice_address, String invoice_bank, String invoice_bank_code,
                  String invoice_file, String invoice_name, String invoice_taxpayer,
                  String invoice_tel, String member_uuid) {
        this.invoice_address = invoice_address;
        this.invoice_bank = invoice_bank;
        this.invoice_bank_code = invoice_bank_code;
        this.invoice_file = invoice_file;
        this.invoice_name = invoice_name;
        this.invoice_taxpayer = invoice_taxpayer;
        this.invoice_tel = invoice_tel;
        this.member_uuid = member_uuid;
    }

    public String getInvoiceNameWithTel() {
        return invoice_name + "(" + invoice_tel + ")";
    }

    public String getInvoice_tel() {
        return invoice_tel;
    }

    public String getInvoice_name() {
        return invoice_name;
    }

    public String getInvoice_bank() {
        return invoice_bank;
    }

    public String getInvoice_taxpayer() {
        return invoice_taxpayer;
    }

    public String getInvoice_bank_code() {
        return invoice_bank_code;
    }

    public String getInvoice_address() {
        return invoice_address;
    }

    public String getMember_uuid() {
        return member_uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.invoice_address);
        dest.writeString(this.invoice_bank);
        dest.writeString(this.invoice_bank_code);
        dest.writeString(this.invoice_file);
        dest.writeString(this.invoice_name);
        dest.writeString(this.invoice_taxpayer);
        dest.writeString(this.invoice_tel);
        dest.writeString(this.member_uuid);
    }

    public Drawee() {
    }

    protected Drawee(Parcel in) {
        this.invoice_address = in.readString();
        this.invoice_bank = in.readString();
        this.invoice_bank_code = in.readString();
        this.invoice_file = in.readString();
        this.invoice_name = in.readString();
        this.invoice_taxpayer = in.readString();
        this.invoice_tel = in.readString();
        this.member_uuid = in.readString();
    }

    public static final Creator<Drawee> CREATOR = new Creator<Drawee>() {
        @Override
        public Drawee createFromParcel(Parcel source) {
            return new Drawee(source);
        }

        @Override
        public Drawee[] newArray(int size) {
            return new Drawee[size];
        }
    };
}
