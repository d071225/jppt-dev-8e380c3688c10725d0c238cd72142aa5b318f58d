package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2017/3/15.
 */

public class Payer implements Parcelable{
    private String assign_code;
    private String bank_address;
    private String bank_code;
    private String bank_type;
    private String delegate_name;
    private String delegate_uuid;
    private String name;

    public Payer(String assign_code, String bank_address, String bank_code, String bank_type, String delegate_name, String delegate_uuid, String name) {
        this.assign_code = assign_code;
        this.bank_address = bank_address;
        this.bank_code = bank_code;
        this.bank_type = bank_type;
        this.delegate_name = delegate_name;
        this.delegate_uuid = delegate_uuid;
        this.name = name;
    }

    public String getDelegate_uuid() {
        return delegate_uuid;
    }

    public String getAssign_code() {
        return assign_code;
    }

    public String getBankName(){
        return bank_address;
    }

    public String getAccountName(){
        return name;
    }

    public String getCardNo(){
        return bank_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.assign_code);
        dest.writeString(this.bank_address);
        dest.writeString(this.bank_code);
        dest.writeString(this.bank_type);
        dest.writeString(this.delegate_name);
        dest.writeString(this.delegate_uuid);
        dest.writeString(this.name);
    }

    public Payer() {
    }

    protected Payer(Parcel in) {
        this.assign_code = in.readString();
        this.bank_address = in.readString();
        this.bank_code = in.readString();
        this.bank_type = in.readString();
        this.delegate_name = in.readString();
        this.delegate_uuid = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Payer> CREATOR = new Creator<Payer>() {
        @Override
        public Payer createFromParcel(Parcel source) {
            return new Payer(source);
        }

        @Override
        public Payer[] newArray(int size) {
            return new Payer[size];
        }
    };
}
