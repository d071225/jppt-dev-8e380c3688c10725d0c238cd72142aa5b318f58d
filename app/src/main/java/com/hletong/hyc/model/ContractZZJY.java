package com.hletong.hyc.model;

import android.os.Parcel;

import com.hletong.hyc.enums.AppTypeConfig;

/**
 * Created by ddq on 2017/1/9.
 * 自主交易详情
 */

public class ContractZZJY extends Source {
    private String agrt_uuid;// 协议流水号
    private String agrt_code;// 协议编号
    private int sign_dt;// 签约日期yyyyMMdd
    private String platform_code;// 平台编号
    private String platform_sname;// 平台名称
    private String platform_tel;// 平台联系电话
    private String platform_fax;// 平台传真号码
    private String platform_addr;// 平台联系地址

    private String carrier_member_code;// 车船会员编号
    private String carrier_member_sname;// 车船会员名称
    private String carrier_member_mobile;// 车船会员手机号码
    private String carrier_member_tel;// 车船会员联系电话
    private String carrier_member_addr;// 车船会员联系地址

    private double t_weight;// 货源重量
    private double t_unit_ct;// 货源数量
    private String trade_uuid;// 交易流水号
    private String trade_code;// 交易编号
    private double trans_weight;// 承运重量
    private double trans_unit_ct;// 承运数量
    private double unit_amt;// 承运单价
    private String loading_address;// 装货地址
    private String unload_address;// 卸货地址

    public void setCargo(double d) {
        if (getBook_ref_type() == 0) {
            trans_weight = d;
        } else
            trans_unit_ct = d;
    }

    public void setUnit_amt(double unit_amt) {
        this.unit_amt = unit_amt;
    }

    @Override
    public double getWeight() {
        /**
         * 这里的情况有点麻烦
         * ContractZZJY这个bean只在查看自主交易三方合同时用到
         * 而普通自主交易和议价，车船和货方，看到的合同虽然展现的效果是一样的，但服务端传递的值却不是同一个，
         * 比如说这个重量，车船给的是trans_weight，而货方是t_weight，下面的数量也是如此
         *
         * 这样不会造成其他问题，因为这个bean的getWeight()和getUnit_ct()函数只在展示合同的时候被调用，不会有其他情况
         */
        if (AppTypeConfig.isCargo())
            return t_weight;
        else
            return trans_weight;
    }

    @Override
    public double getUnit_ct() {
        /**
         * 同{@link #getWeight()}
         */
        if (AppTypeConfig.isCargo()){
            return t_unit_ct;
        }else {
            return trans_unit_ct;
        }
    }

    @Override
    public double getTransport_unit_amt() {
        return unit_amt;
    }

    @Override
    public String getLoadingAddress() {
        return loading_address;
    }

    @Override
    public String getUnLoadingAddress() {
        return unload_address;
    }

    public String getCarrier_member_sname() {
        return carrier_member_sname;
    }

    public String getCarrier_member_mobile() {
        if (carrier_member_mobile == null)
            return carrier_member_tel;
        return carrier_member_mobile;
    }

    public String getCarrier_member_tel() {
        return carrier_member_tel;
    }

    public String getCarrier_member_addr() {
        return carrier_member_addr;
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
        dest.writeString(this.agrt_uuid);
        dest.writeString(this.agrt_code);
        dest.writeInt(this.sign_dt);
        dest.writeString(this.platform_code);
        dest.writeString(this.platform_sname);
        dest.writeString(this.platform_tel);
        dest.writeString(this.platform_fax);
        dest.writeString(this.platform_addr);
        dest.writeString(this.carrier_member_code);
        dest.writeString(this.carrier_member_sname);
        dest.writeString(this.carrier_member_mobile);
        dest.writeString(this.carrier_member_tel);
        dest.writeString(this.carrier_member_addr);
        dest.writeDouble(this.t_weight);
        dest.writeDouble(this.t_unit_ct);
        dest.writeString(this.trade_uuid);
        dest.writeString(this.trade_code);
        dest.writeDouble(this.trans_weight);
        dest.writeDouble(this.trans_unit_ct);
        dest.writeDouble(this.unit_amt);
        dest.writeString(this.loading_address);
        dest.writeString(this.unload_address);
    }

    public ContractZZJY() {
    }

    protected ContractZZJY(Parcel in) {
        super(in);
        this.agrt_uuid = in.readString();
        this.agrt_code = in.readString();
        this.sign_dt = in.readInt();
        this.platform_code = in.readString();
        this.platform_sname = in.readString();
        this.platform_tel = in.readString();
        this.platform_fax = in.readString();
        this.platform_addr = in.readString();
        this.carrier_member_code = in.readString();
        this.carrier_member_sname = in.readString();
        this.carrier_member_mobile = in.readString();
        this.carrier_member_tel = in.readString();
        this.carrier_member_addr = in.readString();
        this.t_weight = in.readDouble();
        this.t_unit_ct = in.readDouble();
        this.trade_uuid = in.readString();
        this.trade_code = in.readString();
        this.trans_weight = in.readDouble();
        this.trans_unit_ct = in.readDouble();
        this.unit_amt = in.readDouble();
        this.loading_address = in.readString();
        this.unload_address = in.readString();
    }

    public static final Creator<ContractZZJY> CREATOR = new Creator<ContractZZJY>() {
        @Override
        public ContractZZJY createFromParcel(Parcel source) {
            return new ContractZZJY(source);
        }

        @Override
        public ContractZZJY[] newArray(int size) {
            return new ContractZZJY[size];
        }
    };
}
