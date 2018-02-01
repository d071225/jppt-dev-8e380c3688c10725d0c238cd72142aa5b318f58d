package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2017/1/24.
 */

public class Ship extends TransporterBase implements Parcelable{
    private String deep;
    private String ship;
    private String full_draft;
    private String ton;
    private String nationality_cert;
    private String has_protocol;
    private String ship_name;
    private String create_time;
    private String net_ton;
    private String ship_uuid;
    private String ship_type;
    private String ship_status_;
    private String ship_length;
    private String hatch_no;
    private String ship_status;
    private String nationality;
    private String update_dttm;
    private String ship_type_;
    private String above_height;
    private String sub_classify;
    private String ship_width;
    private double load_ton;//最大载重

    public String getShip() {
        return ship;
    }

    public String getShip_uuid() {
        return ship_uuid;
    }

    @Override
    public double getLoan() {
        return load_ton;
    }

    @Override
    public String getValue() {
        return ship;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.deep);
        dest.writeString(this.ship);
        dest.writeString(this.full_draft);
        dest.writeString(this.ton);
        dest.writeString(this.nationality_cert);
        dest.writeString(this.has_protocol);
        dest.writeString(this.ship_name);
        dest.writeString(this.create_time);
        dest.writeString(this.net_ton);
        dest.writeString(this.ship_uuid);
        dest.writeString(this.ship_type);
        dest.writeString(this.ship_status_);
        dest.writeString(this.ship_length);
        dest.writeString(this.hatch_no);
        dest.writeString(this.ship_status);
        dest.writeString(this.nationality);
        dest.writeString(this.update_dttm);
        dest.writeString(this.ship_type_);
        dest.writeString(this.above_height);
        dest.writeString(this.sub_classify);
        dest.writeString(this.ship_width);
        dest.writeDouble(this.load_ton);
    }

    public Ship() {
    }

    protected Ship(Parcel in) {
        super(in);
        this.deep = in.readString();
        this.ship = in.readString();
        this.full_draft = in.readString();
        this.ton = in.readString();
        this.nationality_cert = in.readString();
        this.has_protocol = in.readString();
        this.ship_name = in.readString();
        this.create_time = in.readString();
        this.net_ton = in.readString();
        this.ship_uuid = in.readString();
        this.ship_type = in.readString();
        this.ship_status_ = in.readString();
        this.ship_length = in.readString();
        this.hatch_no = in.readString();
        this.ship_status = in.readString();
        this.nationality = in.readString();
        this.update_dttm = in.readString();
        this.ship_type_ = in.readString();
        this.above_height = in.readString();
        this.sub_classify = in.readString();
        this.ship_width = in.readString();
        this.load_ton = in.readDouble();
    }

    public static final Creator<Ship> CREATOR = new Creator<Ship>() {
        @Override
        public Ship createFromParcel(Parcel source) {
            return new Ship(source);
        }

        @Override
        public Ship[] newArray(int size) {
            return new Ship[size];
        }
    };
}
