package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VehicleInfo implements Parcelable {

    private String memberCode;
    private String memberTel;
    private String identify;
    private String plate;
    private String memberUuid;
    private String memberAddressProvince;
    private String truckType;
    private String truckUuid;
    private String dentityNo;
    private String memberName;

    //车辆申请卡片的状态
    private int memberStatus;
    private String memberAddress;
    private String endDt;
    private String maxHeavy;
    private String memberAddressArea;
    private String memberAddressCity;
    private String unitUuid;
    private String truckName;
    private String registerDt;
    private String personalMemberCode;
    private String maxLength;
    //核定载质量
    private double vehicleMass;
    //车牌颜色枚举ID
    @SerializedName(value = "licensePlateColor")
    private String licensePlateId;
    //满载车辆质量
    private double loadedVehicleQuality;

    public Address getAddress() {
        return new Address(memberAddressProvince, memberAddressCity, memberAddressArea, memberAddress);
    }

    public String getEndDt() {
        return endDt;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setTruckName(String truckName) {
        this.truckName = truckName;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public String getDentityNo() {
        return dentityNo;
    }

    public String getTruckType() {
        return truckType;
    }

    public String getMaxHeavy() {
        return maxHeavy == null ? "0" : maxHeavy;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public String getPlate() {

        return plate;
    }

    public String getIdentify() {
        return identify;
    }

    public String getTruckName() {
        return truckName;
    }

    public String getAddressForShow() {
        return getMemberAddressProvince() + " " + getMemberAddressCity() + " " + getMemberAddressArea();
    }

    public String getMemberAddressProvince() {
        return memberAddressProvince;
    }

    public String getMemberAddressArea() {
        return memberAddressArea;
    }

    public String getMemberAddressCity() {
        return memberAddressCity;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberCode);
        dest.writeString(this.memberTel);
        dest.writeString(this.identify);
        dest.writeString(this.plate);
        dest.writeString(this.memberUuid);
        dest.writeString(this.memberAddressProvince);
        dest.writeString(this.truckType);
        dest.writeString(this.truckUuid);
        dest.writeString(this.dentityNo);
        dest.writeString(this.memberName);
        dest.writeInt(this.memberStatus);
        dest.writeString(this.memberAddress);
        dest.writeString(this.endDt);
        dest.writeString(this.maxHeavy);
        dest.writeString(this.memberAddressArea);
        dest.writeString(this.memberAddressCity);
        dest.writeString(this.unitUuid);
        dest.writeString(this.truckName);
        dest.writeString(this.registerDt);
        dest.writeString(this.personalMemberCode);
        dest.writeString(this.maxLength);
        dest.writeDouble(vehicleMass);
        dest.writeDouble(loadedVehicleQuality);
        dest.writeString(this.licensePlateId);

    }

    public VehicleInfo() {
    }

    protected VehicleInfo(Parcel in) {
        this.memberCode = in.readString();
        this.memberTel = in.readString();
        this.identify = in.readString();
        this.plate = in.readString();
        this.memberUuid = in.readString();
        this.memberAddressProvince = in.readString();
        this.truckType = in.readString();
        this.truckUuid = in.readString();
        this.dentityNo = in.readString();
        this.memberName = in.readString();
        this.memberStatus = in.readInt();
        this.memberAddress = in.readString();
        this.endDt = in.readString();
        this.maxHeavy = in.readString();
        this.memberAddressArea = in.readString();
        this.memberAddressCity = in.readString();
        this.unitUuid = in.readString();
        this.truckName = in.readString();
        this.registerDt = in.readString();
        this.personalMemberCode = in.readString();
        this.maxLength = in.readString();
        vehicleMass = in.readDouble();
        loadedVehicleQuality = in.readDouble();
        licensePlateId = in.readString();
    }

    public static final Creator<VehicleInfo> CREATOR = new Creator<VehicleInfo>() {
        @Override
        public VehicleInfo createFromParcel(Parcel source) {
            return new VehicleInfo(source);
        }

        @Override
        public VehicleInfo[] newArray(int size) {
            return new VehicleInfo[size];
        }
    };

    public double getVehicleMass() {
        return vehicleMass;
    }

    public double getLoadedVehicleQuality() {
        return loadedVehicleQuality;
    }

    public String getStateStr() {
        String statusStr = "";
        switch (memberStatus) {
            case 1:
                statusStr = "(正常)";
                break;
            case 2:
                statusStr = "(禁用)";
                break;
            case 3:
                statusStr = "(入会审核)";
                break;
            case 4:
                statusStr = "(退会)";
                break;
            case 5:
                statusStr = "(待提交)";
                break;
            case 6:
                statusStr = "(退会审核)";
                break;
            case 7:
                statusStr = "(退会复核)";
                break;
            case 8:
                statusStr = "(会员管理单位审核)";
                break;
            case 9:
                statusStr = "(审核未通过)";
                break;
            case 10:
                statusStr = "(复核未通过)";
                break;
            case 12:
                statusStr = "(二次入会)";
                break;
        }
        return statusStr;
    }

    public String getLicensePlateId() {
        return licensePlateId;
    }
}
