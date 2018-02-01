package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.hyc.util.RichTextFormat;
import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2016/12/14.
 */
public class ZhiYaDan implements Parcelable{
    private String start_location;
    private String unit;
    private String cargo_kind_name;
    private double unit_ct;
    private String end_location;
    private double weight;
    private String carrier_no;
    private String start_date;

    public String getContentDescription(){
        return "由于在" +
                cargo_kind_name +
                "(" +
                NumberUtil.format3f(unit_ct) +
                unit +
                "/" +
                NumberUtil.format3f(weight) +
                "吨" +
                ")" +
                "货运至" +
                end_location +
                "后，未及时卸货，造成滞压，请" +
                carrier_no +
                "及时提供滞压凭证";
    }

    public String getContent(){
        return "尊敬的 " +
                RichTextFormat.getUnderLineAndBold(carrier_no) +
                " 车辆会员<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "您好，很抱歉在 " +
                RichTextFormat.getUnderLineAndBold(cargo_kind_name) +
                "（" +
                NumberUtil.format3f(unit_ct) +
                unit +
                "/" +
                NumberUtil.format3f(weight) +
                "吨）从" +
                RichTextFormat.getUnderLineAndBold(start_location) +
                "货运至" +
                RichTextFormat.getUnderLineAndBold(end_location) +
                "后未及时卸货，造成" +
                RichTextFormat.getUnderLineAndBold(carrier_no) +
                "滞压带来的不便。\n    " +
                "请及时上传滞压单据，以便于法务部门处理，避免给您造成更大的损失。";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.start_location);
        dest.writeString(this.unit);
        dest.writeString(this.cargo_kind_name);
        dest.writeDouble(this.unit_ct);
        dest.writeString(this.end_location);
        dest.writeDouble(this.weight);
        dest.writeString(this.carrier_no);
        dest.writeString(this.start_date);
    }

    public ZhiYaDan() {
    }

    protected ZhiYaDan(Parcel in) {
        this.start_location = in.readString();
        this.unit = in.readString();
        this.cargo_kind_name = in.readString();
        this.unit_ct = in.readDouble();
        this.end_location = in.readString();
        this.weight = in.readDouble();
        this.carrier_no = in.readString();
        this.start_date = in.readString();
    }

    public static final Creator<ZhiYaDan> CREATOR = new Creator<ZhiYaDan>() {
        @Override
        public ZhiYaDan createFromParcel(Parcel source) {
            return new ZhiYaDan(source);
        }

        @Override
        public ZhiYaDan[] newArray(int size) {
            return new ZhiYaDan[size];
        }
    };
}
