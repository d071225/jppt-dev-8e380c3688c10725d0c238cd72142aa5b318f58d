package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.okhttp.util.ParamUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by ddq on 2017/1/18.
 */

public class ContractItemForDetails extends Source {
    private String part_a_name;
    private String part_a_code;
    private String part_a_contact_tel;
    private String part_a_contact_addr;
    private String part_a_legal_person_name;

    private String part_b_name;
    private String part_b_code;
    private String part_b_contact_addr;
    private String part_b_contact_tel;
    private String part_b_tax_code;
    private String part_b_legal_person_name;

    private String contract_code;
    private String contract_type;
    private String contract_uuid;

    private String trans_trade_uuid;
    private String carrier_member_code;
    private String tax_rt;

    private String book_weight_total_amt;
    private String book_ct_total_amt;
    private String book_uuid;

    private List<Order> orderDtlList;

    public String getBook_uuid() {
        return book_uuid;
    }

    public List<Order> getOrderDtlList() {
        return orderDtlList;
    }

    public String getContract_code() {
        return contract_code;
    }

    public String getPart_a_name() {
        return part_a_name;
    }

    public String getPart_b_name() {
        return part_b_name;
    }

    public String getContract_uuid() {
        return contract_uuid;
    }

    public String getTruckNo() {
        if (ParamUtil.isEmpty(orderDtlList)) {
            return "";
        }
        return orderDtlList.get(0).getCarrier_no();
    }

    public String getTransportTotalFee() {
        double value = 0;
        for (Order item : orderDtlList) {
            value += item.getBook_amt();
        }
        return NumberUtil.format2f(value);
    }

    public String getCreateTimeInChinese() {
        /////////////////////////////////////////////2016-11-15 11:44:10.0
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.PRC);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(getCreate_ts()));
            SimpleDate sd = new SimpleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            return sd.chineseFormat();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTotalFeeByIndex(int index) {
        if (ParamUtil.isEmpty(orderDtlList))
            return "";
        return NumberUtil.format2f(orderDtlList.get(index).getBook_amt());
    }

    public String getTransportUnitPrice(int index) {
        if (ParamUtil.isEmpty(orderDtlList))
            return "";
        return NumberUtil.format3f(orderDtlList.get(index).getTransport_unit_price());
    }

    public String getOtherUnitPrice(int index) {
        if (ParamUtil.isEmpty(orderDtlList))
            return "";
        return NumberUtil.format3f(orderDtlList.get(index).getOther_unit_amt());
    }

    public static class Order implements Parcelable {
        private String transporter_name;
        private double book_amt;
        private double unit_ct;
        private String plan_loading_dttm;
        private double weight;
        private String carrier_no;
        private String transporter_identity_code;
        private double transport_unit_price;
        private double other_unit_amt;
        private double unit_amt;

        public String getPlanLoadingDtTm() {
            SimpleDate sd = SimpleDate.parse(plan_loading_dttm);
            if (sd == null)
                return null;
            return sd.dateString(true, "-") + " " + sd.timeString(true, ":", false) + "左右";
        }

        public String getWeight() {
            return NumberUtil.format2f(weight);
        }

        public double getBook_amt() {
            return book_amt;
        }

        public String getUnit_amt() {
            return NumberUtil.format2f(unit_amt);
        }

        public String getUnitCt() {
            return NumberUtil.format2f(unit_ct);
        }

        public double getTransport_unit_price() {
            return transport_unit_price;
        }

        public double getOther_unit_amt() {
            return other_unit_amt;
        }

        public String getCarrier_no() {
            return carrier_no;
        }

        public String getCargoDesc(String unit){
            return getUnitCt() + unit + "|" + getWeight() + "吨";
        }

        public String getTransportFee(boolean withUnit) {
            if (withUnit)
                return NumberUtil.format2f(book_amt) + " 元";
            else
                return NumberUtil.format2f(book_amt);
        }

        public String getTransporterName() {
            return transporter_name;
        }

        public String getTransporterID() {
            return transporter_identity_code;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.transporter_name);
            dest.writeDouble(this.book_amt);
            dest.writeDouble(this.unit_ct);
            dest.writeString(this.plan_loading_dttm);
            dest.writeDouble(this.weight);
            dest.writeString(this.carrier_no);
            dest.writeString(this.transporter_identity_code);
            dest.writeDouble(this.unit_amt);
        }

        public Order() {
        }

        protected Order(Parcel in) {
            this.transporter_name = in.readString();
            this.book_amt = in.readDouble();
            this.unit_ct = in.readDouble();
            this.plan_loading_dttm = in.readString();
            this.weight = in.readDouble();
            this.carrier_no = in.readString();
            this.transporter_identity_code = in.readString();
            this.unit_amt = in.readDouble();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.book_weight_total_amt);
        dest.writeString(this.contract_code);
        dest.writeString(this.part_b_contact_addr);
        dest.writeString(this.contract_type);
        dest.writeString(this.trans_trade_uuid);
        dest.writeString(this.part_a_contact_tel);
        dest.writeString(this.part_a_name);
        dest.writeString(this.contract_uuid);
        dest.writeString(this.carrier_member_code);
        dest.writeString(this.tax_rt);
        dest.writeString(this.part_a_contact_addr);
        dest.writeString(this.book_uuid);
        dest.writeString(this.part_b_name);
        dest.writeTypedList(this.orderDtlList);
        dest.writeString(this.book_ct_total_amt);
    }

    public ContractItemForDetails() {
    }

    protected ContractItemForDetails(Parcel in) {
        super(in);
        this.book_weight_total_amt = in.readString();
        this.contract_code = in.readString();
        this.part_b_contact_addr = in.readString();
        this.contract_type = in.readString();
        this.trans_trade_uuid = in.readString();
        this.part_a_contact_tel = in.readString();
        this.part_a_name = in.readString();
        this.contract_uuid = in.readString();
        this.carrier_member_code = in.readString();
        this.tax_rt = in.readString();
        this.part_a_contact_addr = in.readString();
        this.book_uuid = in.readString();
        this.part_b_name = in.readString();
        this.orderDtlList = in.createTypedArrayList(Order.CREATOR);
        this.book_ct_total_amt = in.readString();
    }

    public static final Creator<ContractItemForDetails> CREATOR = new Creator<ContractItemForDetails>() {
        @Override
        public ContractItemForDetails createFromParcel(Parcel source) {
            return new ContractItemForDetails(source);
        }

        @Override
        public ContractItemForDetails[] newArray(int size) {
            return new ContractItemForDetails[size];
        }
    };
}
