package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2017/3/23.
 */

public class PayerMemberInfo implements Parcelable {
    private int serialversionuid;
    private String member_code;
    private String personal_tel;
    private String delegate_uuid;
    private String bank_address;
    private String delegate_taxpayer_code;
    private String personal_name;
    private String member_address_area;
    private String zip_code;
    private String invoice_name;
    private String simple_name;
    private String personal_identity;
    private String biz_contact_tel;
    private String unit_code;
    private String fax;
    private String bank_type_;
    private String invoice_file;
    private String delegate_end_dt;
    private String bank_type;
    private String bank_info;
    private String taxpayer_code;
    private String member_address;
    private String delegate_begin_dt;
    private String delegate_name;
    private String invoice_tel;
    private String member_address_city;
    private String organiz_code;
    private String unit_name;
    private String phone;
    private String member_identity;
    private String company_name;
    private String name;
    private String delegate_classify;
    private String member_uuid;
    private String invoice_bank;
    private String member_type;
    private String classify;
    private String assign_code;
    private String remark;
    private String unit_uuid;
    private String member_sname;
    private String member_status;
    private String member_classify;
    private String is_invoice;
    private String delegate_file;
    private String memeber_legal;
    private String email;
    private String member_address_province;
    private String bank_code;
    private String member_tel;
    private String invoice_taxpayer;
    private String member_name;
    private String company_tel;
    private String invoice_address;
    private String invoice_bank_code;
    private String biz_contact;

    public String getAssign_code() {
        return assign_code;
    }

    public String getBank_address() {
        return bank_address;
    }

    public String getBank_code() {
        return bank_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getDelegate_uuid() {
        return delegate_uuid;
    }

    public String getDelegate_name() {
        return delegate_name;
    }

    public String getName() {
        return name;
    }

    public String getBank_type() {
        return bank_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.serialversionuid);
        dest.writeString(this.member_code);
        dest.writeString(this.personal_tel);
        dest.writeString(this.delegate_uuid);
        dest.writeString(this.bank_address);
        dest.writeString(this.delegate_taxpayer_code);
        dest.writeString(this.personal_name);
        dest.writeString(this.member_address_area);
        dest.writeString(this.zip_code);
        dest.writeString(this.invoice_name);
        dest.writeString(this.simple_name);
        dest.writeString(this.personal_identity);
        dest.writeString(this.biz_contact_tel);
        dest.writeString(this.unit_code);
        dest.writeString(this.fax);
        dest.writeString(this.bank_type_);
        dest.writeString(this.invoice_file);
        dest.writeString(this.delegate_end_dt);
        dest.writeString(this.bank_type);
        dest.writeString(this.bank_info);
        dest.writeString(this.taxpayer_code);
        dest.writeString(this.member_address);
        dest.writeString(this.delegate_begin_dt);
        dest.writeString(this.delegate_name);
        dest.writeString(this.invoice_tel);
        dest.writeString(this.member_address_city);
        dest.writeString(this.organiz_code);
        dest.writeString(this.unit_name);
        dest.writeString(this.phone);
        dest.writeString(this.member_identity);
        dest.writeString(this.company_name);
        dest.writeString(this.name);
        dest.writeString(this.delegate_classify);
        dest.writeString(this.member_uuid);
        dest.writeString(this.invoice_bank);
        dest.writeString(this.member_type);
        dest.writeString(this.classify);
        dest.writeString(this.assign_code);
        dest.writeString(this.remark);
        dest.writeString(this.unit_uuid);
        dest.writeString(this.member_sname);
        dest.writeString(this.member_status);
        dest.writeString(this.member_classify);
        dest.writeString(this.is_invoice);
        dest.writeString(this.delegate_file);
        dest.writeString(this.memeber_legal);
        dest.writeString(this.email);
        dest.writeString(this.member_address_province);
        dest.writeString(this.bank_code);
        dest.writeString(this.member_tel);
        dest.writeString(this.invoice_taxpayer);
        dest.writeString(this.member_name);
        dest.writeString(this.company_tel);
        dest.writeString(this.invoice_address);
        dest.writeString(this.invoice_bank_code);
        dest.writeString(this.biz_contact);
    }

    public PayerMemberInfo() {
    }

    protected PayerMemberInfo(Parcel in) {
        this.serialversionuid = in.readInt();
        this.member_code = in.readString();
        this.personal_tel = in.readString();
        this.delegate_uuid = in.readString();
        this.bank_address = in.readString();
        this.delegate_taxpayer_code = in.readString();
        this.personal_name = in.readString();
        this.member_address_area = in.readString();
        this.zip_code = in.readString();
        this.invoice_name = in.readString();
        this.simple_name = in.readString();
        this.personal_identity = in.readString();
        this.biz_contact_tel = in.readString();
        this.unit_code = in.readString();
        this.fax = in.readString();
        this.bank_type_ = in.readString();
        this.invoice_file = in.readString();
        this.delegate_end_dt = in.readString();
        this.bank_type = in.readString();
        this.bank_info = in.readString();
        this.taxpayer_code = in.readString();
        this.member_address = in.readString();
        this.delegate_begin_dt = in.readString();
        this.delegate_name = in.readString();
        this.invoice_tel = in.readString();
        this.member_address_city = in.readString();
        this.organiz_code = in.readString();
        this.unit_name = in.readString();
        this.phone = in.readString();
        this.member_identity = in.readString();
        this.company_name = in.readString();
        this.name = in.readString();
        this.delegate_classify = in.readString();
        this.member_uuid = in.readString();
        this.invoice_bank = in.readString();
        this.member_type = in.readString();
        this.classify = in.readString();
        this.assign_code = in.readString();
        this.remark = in.readString();
        this.unit_uuid = in.readString();
        this.member_sname = in.readString();
        this.member_status = in.readString();
        this.member_classify = in.readString();
        this.is_invoice = in.readString();
        this.delegate_file = in.readString();
        this.memeber_legal = in.readString();
        this.email = in.readString();
        this.member_address_province = in.readString();
        this.bank_code = in.readString();
        this.member_tel = in.readString();
        this.invoice_taxpayer = in.readString();
        this.member_name = in.readString();
        this.company_tel = in.readString();
        this.invoice_address = in.readString();
        this.invoice_bank_code = in.readString();
        this.biz_contact = in.readString();
    }

    public static final Creator<PayerMemberInfo> CREATOR = new Creator<PayerMemberInfo>() {
        @Override
        public PayerMemberInfo createFromParcel(Parcel source) {
            return new PayerMemberInfo(source);
        }

        @Override
        public PayerMemberInfo[] newArray(int size) {
            return new PayerMemberInfo[size];
        }
    };
}