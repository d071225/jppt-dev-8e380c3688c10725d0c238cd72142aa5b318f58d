package com.hletong.hyc.model;

import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dongdaqing on 2017/5/15.
 * 货方会员评价
 */

public class TransporterEvaluation extends Source {
    private String tradeUuid;
    private String tradeCode;
    private int tradeAccount;

    private String carrierNo;
    private String cargoName;
    private String carrierMemberName;
    private double carrierMemberGrade;

    private String loadingAddress;
    private String unloadAddress;

    private String loadingDttm;
    private String unloadDttm;

    private double cargoLoss;
    private int bookRefType;
    private int billingType;

    private double deliverWeight;
    private double deliverUnitCt;
    private double receivceWeight;
    private double receivceUnitCt;

    private int transportDays = -1;

    @Override
    public String getOrgin_cargon_kind_name() {
        return cargoName;
    }

    @Override
    public String getLoadingAddress() {
        return loadingAddress;
    }

    @Override
    public String getUnLoadingAddress() {
        return unloadAddress;
    }

    @Override
    public String getLoading_start_dt() {
        return super.getLoading_start_dt();
    }

    @Override
    public int getBook_ref_type() {
        return bookRefType;
    }

    @Override
    public int getBilling_type() {
        return billingType;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public double getCarrierMemberGrade() {
        return carrierMemberGrade / 100;
    }

    public String getCarrierMemberGradeWithUnit() {
        return NumberUtil.format3f(carrierMemberGrade) + "%";
    }

    public String getCarrierMemberName() {
        return carrierMemberName;
    }

    public double getDeliverCargo(){
        if (bookRefType == 0){
            return deliverWeight;
        }
        return deliverUnitCt;
    }

    public String getDeliverCargoWithUnit(){
        return NumberUtil.format3f(getDeliverCargo()) + getCargoUnit();
    }

    @Override
    public String getCargoUnit() {
        if (bookRefType == 0)
            return "吨";
        return getUnits();
    }

    public double getReceiveCargo(){
        if (bookRefType == 0){
            return receivceWeight;
        }
        return receivceUnitCt;
    }

    public String getReceiveCargoWithUnit(){
        return NumberUtil.format3f(getReceiveCargo()) + getCargoUnit();
    }

    public String getCargoLabel(){
        if (bookRefType == 0)
            return "重量";
        return "数量";
    }

    public int getTradeAccount() {
        return tradeAccount;
    }

    public String getLoadingDttm() {
        SimpleDate sd = SimpleDate.parse(loadingDttm);
        if (sd == null)
            return "";
        return sd.dateString(true,"-") + " " + sd.timeString();
    }

    public String getUnloadDttm() {
        SimpleDate sd = SimpleDate.parse(unloadDttm);
        if (sd == null)
            return "";
        return sd.dateString(true,"-") + " " + sd.timeString();
    }

    public String getLoss(){
        return String.format(Locale.getDefault(),"%s%s(发/收：%s/%s%s)",
                NumberUtil.format3f(getReceiveCargo() - getDeliverCargo()),
                getCargoUnit(),
                NumberUtil.format3f(getDeliverCargo()),
                NumberUtil.format3f(getReceiveCargo()),
                getCargoUnit());
    }
}
