package com.hletong.hyc.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2016/11/10.
 * 收货单
 */
public class Receipt extends Source {
    private String receipt_status_;
    private String create_opid;
    private double trade_weight;
    private int trade_status;
    private String receipt_uuid;
    private String trade_status_;
    private int receipt_status;
    private double r_weight;//收货重量
    private double s_weight;//发货重量
    private double s_unit_ct;//发货数量
    private double r_unit_ct;//收货数量
    private String receipt_code;
    private String trade_uuid;
    private String carrier_no;
    private double trade_unit_ct;
    private String contract_uuid;
    private String upload_dttm;
    //==3代表审核不通过
    private int waybill_status;
    //审核不通过的理由
    @SerializedName(value = "memo", alternate = {"process_memo", "processMemo"}/*备选解析字段*/)
    private String memo;
    //memo显示日期
    private String accept_dttm;

    public String getContract_uuid() {
        return contract_uuid;
    }

    private String waybill_file_id;
    private String waybill_upload_dttm;

    @Override
    public String getAvailableTime() {
        return upload_dttm;
    }

    public String getReceipt_status_() {
        return receipt_status_;
    }

    public int getReceipt_status() {
        return receipt_status;
    }

    public String getReceipt_code() {
        return receipt_code;
    }

    public String getCarrier_no() {
        return carrier_no;
    }

    public String getS_unit_ct() {
        return NumberUtil.format3f(s_unit_ct);
    }

    public String getS_weight() {
        return NumberUtil.format3f(s_weight);
    }

    public String getR_unit_ct() {
        return NumberUtil.format3f(r_unit_ct);
    }

    public boolean isSameCount(String count) {
        final String lCount = getS_unit_ct();
//        Logger.d("r count => " + count + ",l count => " + lCount);
        return lCount.equals(count);
    }

    public String getR_weight() {
        return NumberUtil.format3f(r_weight);
    }

    public boolean isSameWeight(String weight) {
        final String lWeight = getS_weight();
//        Logger.d("r weight => " + weight + ",l weight => " + lWeight);
        return lWeight.equals(weight);
    }

    public String getReceipt_uuid() {
        return receipt_uuid;
    }

    public String getTrade_uuid() {
        return trade_uuid;
    }

    public String getWaybill_file_id() {
        return waybill_file_id;
    }

    public int getWaybill_status() {
        return waybill_status;
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

    public String getContentDescription() {
        return Address.chooseCity(getLoading_province(), getLoading_city()) +
                getLoading_country() +
                "运往" +
                Address.chooseCity(getUnload_province(), getUnload_city()) +
                getUnload_country() +
                "的" +
                getCargoDescWithUnit() +
                getOrgin_cargon_kind_name() +
                "需进行运单回传";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.receipt_status_);
        dest.writeString(this.create_opid);
        dest.writeDouble(this.trade_weight);
        dest.writeInt(this.trade_status);
        dest.writeString(this.receipt_uuid);
        dest.writeString(this.trade_status_);
        dest.writeInt(this.receipt_status);
        dest.writeDouble(this.r_weight);
        dest.writeDouble(this.s_weight);
        dest.writeDouble(this.s_unit_ct);
        dest.writeDouble(this.r_unit_ct);
        dest.writeString(this.receipt_code);
        dest.writeString(this.trade_uuid);
        dest.writeString(this.carrier_no);
        dest.writeDouble(this.trade_unit_ct);
        dest.writeString(this.contract_uuid);
        dest.writeString(this.upload_dttm);
        dest.writeInt(this.waybill_status);
        dest.writeString(this.waybill_file_id);
        dest.writeString(this.waybill_upload_dttm);
        dest.writeString(this.memo);
        dest.writeString(this.accept_dttm);

    }

    public Receipt() {
    }

    protected Receipt(Parcel in) {
        super(in);
        this.receipt_status_ = in.readString();
        this.create_opid = in.readString();
        this.trade_weight = in.readDouble();
        this.trade_status = in.readInt();
        this.receipt_uuid = in.readString();
        this.trade_status_ = in.readString();
        this.receipt_status = in.readInt();
        this.r_weight = in.readDouble();
        this.s_weight = in.readDouble();
        this.s_unit_ct = in.readDouble();
        this.r_unit_ct = in.readDouble();
        this.receipt_code = in.readString();
        this.trade_uuid = in.readString();
        this.carrier_no = in.readString();
        this.trade_unit_ct = in.readDouble();
        this.contract_uuid = in.readString();
        this.upload_dttm = in.readString();
        this.waybill_status = in.readInt();
        this.waybill_file_id = in.readString();
        this.waybill_upload_dttm = in.readString();
        this.memo = in.readString();
        this.accept_dttm = in.readString();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel source) {
            return new Receipt(source);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    public String getMemo() {
        return memo;
    }

    public String getAccept_dttm() {
        return accept_dttm;
    }
}
