package com.hletong.hyc.model;

import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;

import java.util.Locale;

/**
 * Created by ddq on 2017/3/8.
 * 货源承运信息，货方APP
 */

public class SourceCarryInfo {
    private String cargoUuid;
    private String status_;
    private String loadWeight;
    private String payerMemberCode;
    private String signDttm;
    private String unloadWeight;
    private String planLoadingDttm;
    private String transportAmt;
    private String invoiceUploadDttm;
    private double bookUnitCt;
    private int legalNum;
    private String loadUnitCt;
    private String createDttm;
    private String receiptUploadDttm;
    private String carrierNo;
    private String diffWeight;
    private String realLoadingDttm;
    private String diffUnitCt;
    private double bookWeight;
    private String unloadUnitCt;
    private String tradeUuid;
    private String status;
    private String carrierChargeName;
    private String carrierChargePhone;
    private String carrierMemberCode;
    private String carrierMemberName;

    private String formatedPlanLoadingDttm;//格式化之后的数据
    private String formatedRealLoadingDttm;//格式化之后的数据
    private String formatedSignDttm;//格式化之后的数据

    private int book_ref_type;
    private String unit;

    public void setExtra(int book_ref_type, String unit) {
        this.book_ref_type = book_ref_type;
        this.unit = unit;
    }

    public String getCarrierChargeName() {
        return carrierMemberName;
    }

    public String getCarrierChargePhone() {
        return carrierChargePhone;
    }

    public String getStatus() {
        return status;
    }

    public double getBookCount() {
        if (book_ref_type == 0)
            return bookWeight;
        return bookUnitCt;
    }

    public String getStatus_() {
        return status_;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public String getCarrierACount() {
        return String.format(Locale.CHINESE, "%s(%s%s)", carrierNo, NumberUtil.format3f(getBookCount()), unit);
    }

    public String getInvoiceUploadDttm() {
        return invoiceUploadDttm;
    }

    public String getReceiptUploadDttm() {
        return receiptUploadDttm;
    }

    public String getTimeByStatus(String status) {
        switch (status) {
            case "00":
                if (formatedPlanLoadingDttm == null) {
                    SimpleDate simpleDate = SimpleDate.parse(planLoadingDttm);
                    if (simpleDate != null) {
                        formatedPlanLoadingDttm = "预装货时间：" + simpleDate.chineseFormatWithoutYear() + simpleDate.timeString(true, ":");
                    }
                }
                return formatedPlanLoadingDttm;
            case "99":
                if (formatedSignDttm == null) {
                    SimpleDate simpleDate = SimpleDate.parse(signDttm);
                    if (simpleDate != null) {
                        formatedSignDttm = "完成时间：" + simpleDate.chineseFormatWithoutYear() + simpleDate.timeString(true, ":");
                    }
                }
                return formatedSignDttm;
            case "100":
                if (formatedSignDttm == null) {
                    SimpleDate simpleDate = SimpleDate.parse(signDttm);
                    if (simpleDate != null) {
                        formatedSignDttm = "摘牌时间：" + simpleDate.chineseFormatWithoutYear() + simpleDate.timeString(true, ":");
                    }
                }
                return formatedSignDttm;
            default:
                if (formatedRealLoadingDttm == null) {
                    SimpleDate simpleDate = SimpleDate.parse(realLoadingDttm == null ? planLoadingDttm : realLoadingDttm);
                    if (simpleDate != null) {
                        formatedRealLoadingDttm = "装货时间：" + simpleDate.chineseFormatWithoutYear() + simpleDate.timeString(true, ":");
                    }
                }
                return formatedRealLoadingDttm;
        }
    }
}
