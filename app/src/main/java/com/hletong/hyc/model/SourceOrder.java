package com.hletong.hyc.model;

import android.os.Parcel;

import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.StringUtil;
import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ddq on 2016/11/8.
 * 承运合同的部分详情
 */
public class SourceOrder extends Source {
    private String carrier_member_mgt_unit_code;//所属会员管理单位
    private String carrier_member_code;//承运会员编号

    private String book_uuid;//订单流水号
    private String book_code;//摘牌订单号

    private double book_unit_ct;//承运数量
    private double book_weight;//承运重量

    private double book_ct_total_amt;//
    private double book_weight_total_amt;//

    private long book_dttm;//下单时间
    private String expire_dttm;//签约截止时间

    public String getBook_uuid() {
        return book_uuid;
    }

    public long getExpireTimeInMilliSeconds() {
        if (StringUtil.isBlank(expire_dttm))
            return Long.MAX_VALUE;

        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.PRC);
//        sdf.setTimeZone(TimeZone.getTimeZone("CST"));
        try {
            Date date = sdf.parse(expire_dttm);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Logger.d(e.getLocalizedMessage());
        }
        return Long.MAX_VALUE;
    }

    @Override
    public double getWeight() {
        /**
         * 这里这样取的原因
         *
         * 在未签合同列表里面，车船app返回的是book_weight，货方app返回的是weight
         * 由于界面是共享的，不能分开取，所以这里取大的，因为另一个一定是0
         */
        return Math.max(super.getWeight(), book_weight);
    }

    @Override
    public double getUnit_ct() {
        //同上
        return Math.max(super.getUnit_ct(), book_unit_ct);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.carrier_member_mgt_unit_code);
        dest.writeDouble(this.book_unit_ct);
        dest.writeDouble(this.book_ct_total_amt);
        dest.writeString(this.carrier_member_code);
        dest.writeString(this.book_code);
        dest.writeString(this.book_uuid);
        dest.writeString(this.expire_dttm);
        dest.writeDouble(this.book_weight);
        dest.writeDouble(this.book_weight_total_amt);
        dest.writeLong(this.book_dttm);
    }

    public SourceOrder() {
    }

    protected SourceOrder(Parcel in) {
        super(in);
        this.carrier_member_mgt_unit_code = in.readString();
        this.book_unit_ct = in.readDouble();
        this.book_ct_total_amt = in.readDouble();
        this.carrier_member_code = in.readString();
        this.book_code = in.readString();
        this.book_uuid = in.readString();
        this.expire_dttm = in.readString();
        this.book_weight = in.readDouble();
        this.book_weight_total_amt = in.readDouble();
        this.book_dttm = in.readLong();
    }

    public static final Creator<SourceOrder> CREATOR = new Creator<SourceOrder>() {
        @Override
        public SourceOrder createFromParcel(Parcel source) {
            return new SourceOrder(source);
        }

        @Override
        public SourceOrder[] newArray(int size) {
            return new SourceOrder[size];
        }
    };
}
