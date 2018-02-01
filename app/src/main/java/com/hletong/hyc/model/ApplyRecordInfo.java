package com.hletong.hyc.model;

/**
 * Created by 阳乾春 on 2017/1/16.
 */
public class ApplyRecordInfo {
    private String documentCode;
    private int birthday;
    private String applyUuid;
    private String createOpid;
    private String hletCardType;
    private double truckTons;
    private String truckType;
    private String documentType;
    private String plate;
    private String addrProvince;
    private String addrCountry;
    private String txflow;
    private String synStatus;
    private String appId;
    private String truckOwnerName;
    private int documentValidDt;
    private String createTs;
    private String documentValid;
    private String hletCardId;
    private String memberUuid;
    private int truckSeats;
    private int applyDt;
    private String sex;
    private String truckColor;
    private String spellName;
    private String addrAddress;
    private int version;
    private String serialNo;
    private String addrCity;
    private String truckIdentifyCode;
    private String phone;
    private String chineseName;
    private String applyCode;
    private double truckLength;
    private String applyStatus;
    private String applyTm;

    public String getPlate() {
        return plate;
    }

    public String getDate() {
        String date = String.valueOf(applyDt);
        if (date.length() == 8) {
            return date.substring(0, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6, 8) + "日";
        }
        return null;
    }
}
