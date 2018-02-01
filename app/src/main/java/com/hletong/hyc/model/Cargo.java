package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2017/3/9.
 * 发布货源-选择货物
 */

public class Cargo extends Source {
    private String goods_uuid;
    private String member_code;

    public String getGoods_uuid() {
        return goods_uuid;
    }

    @Override
    public String getCargo_uuid() {
        return goods_uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.goods_uuid);
        dest.writeString(this.member_code);
    }

    public Cargo() {
    }

    protected Cargo(Parcel in) {
        super(in);
        this.goods_uuid = in.readString();
        this.member_code = in.readString();
    }

    public static final Creator<Cargo> CREATOR = new Creator<Cargo>() {
        @Override
        public Cargo createFromParcel(Parcel source) {
            return new Cargo(source);
        }

        @Override
        public Cargo[] newArray(int size) {
            return new Cargo[size];
        }
    };
}
