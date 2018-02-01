package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.dialog.selector.IItemShow;

/**
 * Created by ddq on 2017/1/25.
 */

public class ContractCarrierInfo implements IItemShow, Parcelable {
    private String truck_uuid;
    private String contact_name;
    private String contact_type;
    private String member_code;
    private String contact_uuid;
    private String create_dt;
    private String plate;
    private String identity_no;
    private int version;

    public String getContact_name() {
        return contact_name;
    }

    public String getIdentity_no() {
        return identity_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.truck_uuid);
        dest.writeString(this.contact_name);
        dest.writeString(this.contact_type);
        dest.writeString(this.member_code);
        dest.writeString(this.contact_uuid);
        dest.writeString(this.create_dt);
        dest.writeString(this.plate);
        dest.writeString(this.identity_no);
        dest.writeInt(this.version);
    }

    public ContractCarrierInfo() {
    }

    protected ContractCarrierInfo(Parcel in) {
        this.truck_uuid = in.readString();
        this.contact_name = in.readString();
        this.contact_type = in.readString();
        this.member_code = in.readString();
        this.contact_uuid = in.readString();
        this.create_dt = in.readString();
        this.plate = in.readString();
        this.identity_no = in.readString();
        this.version = in.readInt();
    }

    public static final Creator<ContractCarrierInfo> CREATOR = new Creator<ContractCarrierInfo>() {
        @Override
        public ContractCarrierInfo createFromParcel(Parcel source) {
            return new ContractCarrierInfo(source);
        }

        @Override
        public ContractCarrierInfo[] newArray(int size) {
            return new ContractCarrierInfo[size];
        }
    };

    @Override
    public String getValue() {
        return String.format("%s（%s）", contact_name, identity_no);
    }
}
