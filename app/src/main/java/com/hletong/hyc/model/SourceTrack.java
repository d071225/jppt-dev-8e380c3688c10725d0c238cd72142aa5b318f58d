package com.hletong.hyc.model;

import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2017/3/8.
 * {@link com.hletong.hyc.adapter.SourceTrackAdapter}里面用到的bean
 */

public class SourceTrack {
    /**
     * 四种类型以及下面对应的具体数据：
     * 1.title => message
     * 2.block_title => mStatus
     * 3.item => mCarryInfo
     * 4.expand => message
     */
    private final int type;//viewType
    private SourceCarryInfo mCarryInfo;//type == 3的数据
    private Status parent;//只有当type == 3或者4的时候才有用
    private String message;//type == 1时候的数据
    private Status mStatus;//type == 2时候的数据
    private Expand mExpand;//type == 4时候的数据

    public SourceTrack() {
        this.type = 1;
        message = "货源状态";
    }

    public SourceTrack(int transportType, String status, String statusCode, int carNumber, double cargoDesc, String unit) {
        this.type = 2;
        mStatus = new Status(transportType, status, statusCode, carNumber, cargoDesc, unit);
    }

    public SourceTrack(SourceCarryInfo carryInfo,Status parent) {
        this.type = 3;
        mCarryInfo = carryInfo;
        this.parent = parent;
    }

    public SourceTrack(String text, int image, Status parent) {
        this.type = 4;
        mExpand = new Expand(text, image);
        this.parent = parent;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Expand getExpand() {
        return mExpand;
    }

    public SourceCarryInfo getCarryInfo() {
        return mCarryInfo;
    }

    public Status getStatus() {
        return mStatus;
    }

    public Status getParent() {
        return parent;
    }

    public static class Status {
        private int transportType;//运输方式
        private String status;//状态：待装货，运输中，已完成
        private String statusCode;//
        private int carNumber;//当前状态总共有多少辆车
        private double cargoDesc;//当前状态下有多少货物
        private String unit;//货源单位

        public Status(int transportType, String status, String statusCode, int carNumber, double cargoDesc, String unit) {
            this.transportType = transportType;
            this.status = status;
            this.statusCode = statusCode;
            this.carNumber = carNumber;
            this.cargoDesc = cargoDesc;
            this.unit = unit;
        }

        public String getStatus() {
            return status;
        }

        public String getTransportInfo() {
            return "共" + carNumber + getTransporterDescription() + "，" + NumberUtil.format3f(cargoDesc) + unit;
        }

        private String getTransporterDescription() {
            if (transportType == 1) {
                return "辆车";
            } else {
                return "艘船";
            }
        }

        public int getCarNumber() {
            return carNumber;
        }

        public void setCargoDesc(double cargoDesc) {
            this.cargoDesc = cargoDesc;
        }

        public String getStatusCode() {
            return statusCode;
        }
    }

    public static class Expand {
        private String text;
        private int image;
        private int status;//0 => 收起，1 => 展开

        public Expand(String text, int image) {
            update(text, image, 0);//默认收起
        }

        public void update(String text, int image, int status) {
            this.text = text;
            this.image = image;
            this.status = status;
        }

        public String getText() {
            return text;
        }

        public int getImage() {
            return image;
        }

        //获取当前状态的相反状态
        public int getOppositeStatus() {
            return status ^ 0x01;
        }
    }
}
