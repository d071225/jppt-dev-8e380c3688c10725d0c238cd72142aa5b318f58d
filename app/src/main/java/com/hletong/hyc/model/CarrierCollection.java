package com.hletong.hyc.model;

import com.hletong.mob.util.NumberUtil;

import java.util.Locale;

/**
 * Created by ddq on 2017/3/27.
 * 收藏车船
 */

public class CarrierCollection {
    private String cargoOwnerCode;
    private String cargoUuid;
    private String carrierMemberCode;
    private String contact;
    private String contactTel;
    private String createOpid;
    private String createTs;
    private String informStatus;
    private String status;
    private String storedCode;
    private String storedUuid;
    private String tradeUuid;
    private int tradeAccount;
    private float memberGrade;
    private int transportType;
    private String vehicles;
    private String vehiclesCapacity;
    private String vehiclesLength;
    private String vehiclesType;
    private String version;

    public String getTradeUuid() {
        return tradeUuid;
    }

    public String getCarrierWithNo() {
        return String.format(Locale.getDefault(), "%s(%s)", vehicles, contact);
    }

    public String getModel() {
        if (transportType == 1) {
            return vehiclesType + "，" + NumberUtil.format3f(vehiclesLength) + "米";
        }
        return vehiclesType;
    }

    public boolean canInform() {
        return !"1".equals(informStatus);
    }

    public void informed(){
        informStatus = "1";
    }

    public String getContactTel() {
        return contactTel;
    }

    public String getContact() {
        return contact;
    }

    public String getCargoUuid() {
        return cargoUuid;
    }

    public String getStoredUuid() {
        return storedUuid;
    }

    public String getVehicles() {
        return vehicles;
    }

    public String getCarrierMemberCode() {
        return carrierMemberCode;
    }

    public String getMemberGradeWithUnit() {
        return "好评率：" + memberGrade + "%";
    }

    public float getMemberGrade() {
        return memberGrade;
    }

    public String getTradeCountWithUnit() {
        return "交易笔数：" + (tradeAccount == 0 ? "无" : tradeAccount);
    }
}
