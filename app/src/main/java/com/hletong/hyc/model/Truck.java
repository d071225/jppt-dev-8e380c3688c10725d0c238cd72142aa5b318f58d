package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2016/10/25.
 */

public class Truck extends TransporterBase implements Parcelable {

    private String plate;
    private String max_height;
    private double max_heavy;
    private String max_width;
    private String truck_name;
    private String max_length;
    private String truck_type;
    private String truck_uuid;
    private String identify;
    private String truck_type_;
    private String truck_status;
    private String truck_status_;

    public String getPlate() {
        return plate;
    }

    public String getTruck_uuid() {
        return truck_uuid;
    }

    @Override
    public double getLoan() {
        return max_heavy;
    }

    @Override
    public String getValue() {
        return plate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.plate);
        dest.writeString(this.max_height);
        dest.writeDouble(this.max_heavy);
        dest.writeString(this.max_width);
        dest.writeString(this.truck_name);
        dest.writeString(this.max_length);
        dest.writeString(this.truck_type);
        dest.writeString(this.truck_uuid);
        dest.writeString(this.identify);
        dest.writeString(this.truck_type_);
        dest.writeString(this.truck_status);
        dest.writeString(this.truck_status_);
    }

    public Truck() {
    }

    protected Truck(Parcel in) {
        super(in);
        this.plate = in.readString();
        this.max_height = in.readString();
        this.max_heavy = in.readDouble();
        this.max_width = in.readString();
        this.truck_name = in.readString();
        this.max_length = in.readString();
        this.truck_type = in.readString();
        this.truck_uuid = in.readString();
        this.identify = in.readString();
        this.truck_type_ = in.readString();
        this.truck_status = in.readString();
        this.truck_status_ = in.readString();
    }

    public static final Creator<Truck> CREATOR = new Creator<Truck>() {
        @Override
        public Truck createFromParcel(Parcel source) {
            return new Truck(source);
        }

        @Override
        public Truck[] newArray(int size) {
            return new Truck[size];
        }
    };
}
