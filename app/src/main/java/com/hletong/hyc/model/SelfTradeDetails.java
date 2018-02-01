package com.hletong.hyc.model;

import android.os.Parcel;

/**
 * Created by ddq on 2017/1/9.
 * 自主交易待办详情
 */

public class SelfTradeDetails extends Source{
    private String trade_uuid;// 交易流水号
    private String agrt_uuid;// 协议流水号
    private String cargo_contact;// 货方联系人
    private String cargo_contact_tel;// 货方联系电话
    private String cargo_confirm_status;// 货方确认状态
    private long cargo_confirm_dttm;// 货方确认时间
    private String carrier_contact;// 车船方联系人
    private String carrier_contact_tel;// 车船方联系电话
    private String carrier_confirm_status;// 车船确认状态
    private long carrier_confirm_dttm;// 车船确认时间

    private double t_weight;// 货物总吨位
    private double t_unit_ct;// 货物总数量
    private String trade_code;// 交易编号
    private double trans_weight;// 承运重量
    private double trans_unit_ct;// 承运数量
    private double unit_amt;//承运单价

    private String loading_address;// 装货地址
    private String unload_address;// 卸货地址

    public String getCarrier_contact() {
        return carrier_contact;
    }

    public String getCarrier_contact_tel() {
        return carrier_contact_tel;
    }

    @Override
    public String getCargo_owner_tel() {
        return cargo_contact_tel;
    }

    @Override
    public String getCargo_owner_sname() {
        return cargo_contact;
    }

    @Override
    public String getLoading_contacts_tel() {
        return cargo_contact_tel;
    }

    @Override
    public String getLoadingContact() {
        return cargo_contact;
    }

    @Override
    public String getLoadingAddress() {
        return loading_address;
    }

    @Override
    public String getUnLoadingAddress() {
        return unload_address;
    }

    @Override
    public double getTransport_unit_amt() {
        return unit_amt;
    }

    @Override
    public double getUnit_ct() {
        return trans_unit_ct;
    }

    @Override
    public double getWeight() {
        return trans_weight;
    }

    public String getTrade_uuid() {
        return trade_uuid;
    }

    public String getAgrt_uuid() {
        return agrt_uuid;
    }

    public String getTrade_code() {
        return trade_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.trade_uuid);
        dest.writeString(this.agrt_uuid);
        dest.writeString(this.cargo_contact);
        dest.writeString(this.cargo_contact_tel);
        dest.writeString(this.cargo_confirm_status);
        dest.writeLong(this.cargo_confirm_dttm);
        dest.writeString(this.carrier_contact);
        dest.writeString(this.carrier_contact_tel);
        dest.writeString(this.carrier_confirm_status);
        dest.writeLong(this.carrier_confirm_dttm);
        dest.writeDouble(this.t_weight);
        dest.writeDouble(this.t_unit_ct);
        dest.writeString(this.trade_code);
        dest.writeDouble(this.trans_weight);
        dest.writeDouble(this.trans_unit_ct);
        dest.writeString(this.loading_address);
        dest.writeString(this.unload_address);
    }

    public SelfTradeDetails() {
    }

    protected SelfTradeDetails(Parcel in) {
        super(in);
        this.trade_uuid = in.readString();
        this.agrt_uuid = in.readString();
        this.cargo_contact = in.readString();
        this.cargo_contact_tel = in.readString();
        this.cargo_confirm_status = in.readString();
        this.cargo_confirm_dttm = in.readLong();
        this.carrier_contact = in.readString();
        this.carrier_contact_tel = in.readString();
        this.carrier_confirm_status = in.readString();
        this.carrier_confirm_dttm = in.readLong();
        this.t_weight = in.readDouble();
        this.t_unit_ct = in.readDouble();
        this.trade_code = in.readString();
        this.trans_weight = in.readDouble();
        this.trans_unit_ct = in.readDouble();
        this.loading_address = in.readString();
        this.unload_address = in.readString();
    }

    public static final Creator<SelfTradeDetails> CREATOR = new Creator<SelfTradeDetails>() {
        @Override
        public SelfTradeDetails createFromParcel(Parcel source) {
            return new SelfTradeDetails(source);
        }

        @Override
        public SelfTradeDetails[] newArray(int size) {
            return new SelfTradeDetails[size];
        }
    };
}
