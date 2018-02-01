package com.hletong.hyc.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TruckCompleteInfo implements Serializable {
    private static final long serialVersionUID = 2757247067783524991L;
    /**
     * 车辆类型
     */
    private DictionaryItem truck_type;

    private DictionaryItem truck_color;
    /**
     * 性别
     */
    private DictionaryItem sexType;

    /**
     * 车辆长度
     */
    @SerializedName("maxLength")
    private String truckLength;
    /**
     * 车辆载质量
     */
    private String vehicleMass;
    /**
     * 满载车辆质量
     */
    private String loadedVehicleQuality;
    /**
     * 载重吨位
     */
    @SerializedName("maxHeavy")
    private String loadTon;
    /**
     * 从业资格证号
     */
    @SerializedName("qualificationCertificate")
    private String cyzgzNumber;
    /**
     * 道路运输字号
     */
    @SerializedName("roadTransportCertificate")
    private String dlysNumber;

    /**
     * 车辆识别代号
     */
    @SerializedName("identify")
    private String sbdhNumber;

    public String getLoadTon() {
        return loadTon;
    }

    public void setLoadTon(String loadTon) {
        this.loadTon = loadTon;
    }

    public String getCyzgzNumber() {
        return cyzgzNumber;
    }

    public void setCyzgzNumber(String cyzgzNumber) {
        this.cyzgzNumber = cyzgzNumber;
    }

    public String getDlysNumber() {
        return dlysNumber;
    }

    public void setDlysNumber(String dlysNumber) {
        this.dlysNumber = dlysNumber;
    }

    public DictionaryItem getTruck_type() {
        return truck_type;
    }

    public void setTruck_type(DictionaryItem truck_type) {
        this.truck_type = truck_type;
    }

    public DictionaryItem getTruck_color() {
        return truck_color;
    }

    public void setTruck_color(DictionaryItem truck_color) {
        this.truck_color = truck_color;
    }

    public DictionaryItem getSexType() {
        return sexType;
    }

    public void setSexType(DictionaryItem sexType) {
        this.sexType = sexType;
    }

    public String getVehicleMass() {
        return vehicleMass;
    }

    public void setVehicleMass(String vehicleMass) {
        this.vehicleMass = vehicleMass;
    }

    public String getLoadedVehicleQuality() {
        return loadedVehicleQuality;
    }

    public void setLoadedVehicleQuality(String loadedVehicleQuality) {
        this.loadedVehicleQuality = loadedVehicleQuality;
    }

    public String getTruckLength() {
        return truckLength;
    }

    public void setTruckLength(String truckLength) {
        this.truckLength = truckLength;
    }

    public String getSbdhNumber() {
        return sbdhNumber;
    }

    public void setSbdhNumber(String sbdhNumber) {
        this.sbdhNumber = sbdhNumber;
    }

}
