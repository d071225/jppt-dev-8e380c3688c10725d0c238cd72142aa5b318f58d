package com.hletong.hyc.model;

import java.io.Serializable;

public class ShipInfo implements Serializable {
    private static final long serialVersionUID = 8023493938930443489L;

    private DictionaryItem ship_type;

    private String shipLength;
    /**
     * 满载吃水
     */
    private String loadedWater;

    /**
     * 净吨位
     */
    private String newTonnage;

    /**
     * 载重吨位
     */
    private String loadTon;
    /**
     * 道路运输字号
     */
    private String gjzsNumber;

    public String getLoadTon() {
        return loadTon;
    }

    public void setLoadTon(String loadTon) {
        this.loadTon = loadTon;
    }

    public DictionaryItem getShip_type() {
        return ship_type;
    }

    public void setShip_type(DictionaryItem ship_type) {
        this.ship_type = ship_type;
    }

    public String getShipLength() {
        return shipLength;
    }

    public void setShipLength(String shipLength) {
        this.shipLength = shipLength;
    }

    public String getLoadedWater() {
        return loadedWater;
    }

    public void setLoadedWater(String loadedWater) {
        this.loadedWater = loadedWater;
    }

    public String getNewTonnage() {
        return newTonnage;
    }

    public void setNewTonnage(String newTonnage) {
        this.newTonnage = newTonnage;
    }


    public String getGjzsNumber() {
        return gjzsNumber;
    }

    public void setGjzsNumber(String gjzsNumber) {
        this.gjzsNumber = gjzsNumber;
    }
}
