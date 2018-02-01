package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by ddq on 2017/3/6.
 */

public class CargoSourceItem extends Source implements Parcelable {
    private String cargoUuid;

    private String matchStatus;
    private String matchStatus_;
    private String cargoMatchStatus;
    private int bookRefType = -1;
    private int chargeReferType;

    private String loadingProvince;
    private String loadingCity;
    private String loadingCountry;

    private String unloadProvince;
    private String unloadCity;
    private String unloadCountry;

    private String unloadContacts;
    private String unloadContactsName;
    private String unloadContactsTel;

    private int billingType;
    private String billingType_;

    private String loadingStartDt;
    private String loadingEndDt;

    private int transportType;
    private String createTs;
    private List<TransporterInfo> transportTradeDtlRespDtoList;

    private String revokeBtnFlag;
    private String copyBtnFlag;
    private String orginCargonKindName;
    private double unitCt = -1;
    private double conferUnitAmt;

    public List<TransporterInfo> getTransportTradeDtlRespDtoList() {
        return transportTradeDtlRespDtoList;
    }

    public boolean isButtonAvailable() {
        return (getBilling_type() == 3 && "50".equals(getStatus()))
                || "00".equals(getStatus())
                || "20".equals(getStatus())
                || "40".equals(getStatus())
                || "50".equals(getStatus());
    }

    @Override
    public int getTransport_type() {
        if (super.getTransport_type() != -1)
            return super.getTransport_type();
        return transportType;
    }

    @Override
    public String getUnload_contacts() {
        return unloadContacts;
    }

    @Override
    public String getUnload_contacts_name() {
        return unloadContactsName;
    }

    @Override
    public String getUnload_contacts_tel() {
        return unloadContactsTel;
    }

    @Override
    public double getUnit_ct() {
        return unitCt;
    }

    @Override
    public String getOrgin_cargon_kind_name() {
        return orginCargonKindName;
    }

    @Override
    public int getBook_ref_type() {
        return bookRefType;
    }

    @Override
    public int getBilling_type() {
        return billingType;
    }

    @Override
    public String getCargo_uuid() {
        return cargoUuid;
    }

    @Override
    public String getLoading_province() {
        return loadingProvince;
    }

    @Override
    public String getLoading_city() {
        return loadingCity;
    }

    @Override
    public String getLoading_country() {
        return loadingCountry;
    }

    @Override
    public String getUnload_province() {
        return unloadProvince;
    }

    @Override
    public String getUnload_city() {
        return unloadCity;
    }

    @Override
    public String getUnload_country() {
        return unloadCountry;
    }

    @Override
    public String getLoading_start_dt() {
        return loadingStartDt;
    }

    @Override
    public String getLoading_end_dt() {
        return loadingEndDt;
    }

    @Override
    public String getAvailableTime() {
        if (super.getAvailableTime() != null)
            return super.getAvailableTime();
        return createTs;
    }

    public String getRemainCargoDesc() {
        return NumberUtil.format3f(getRemainCargo()) + getCargoUnit();
    }

    public boolean isCarried() {
        return transportTradeDtlRespDtoList != null;
    }

    private double getRemainCargo() {
        double cargoRemain = getCargoDesc();
        if (!ParamUtil.isEmpty(transportTradeDtlRespDtoList)) {
            for (TransporterInfo ti : transportTradeDtlRespDtoList) {
                cargoRemain -= ti.getBookCargo(getBook_ref_type());
            }
        }
        return cargoRemain;
    }

    public String getMatchStatus() {
        if (getMatch_status() != null)
            return getMatch_status();
        return matchStatus;
    }

    public boolean isMatchSuccess() {
        return "20".equals(matchStatus);
    }

    public String getStatusDesc() {
        return getStatus_();
//        switch (getStatus()) {
//            case "00":
//                return "待提交";
//            case "10":
//                return "待审核";
//            case "20":
//                return "审核未通过";
//            case "40":
//                return "待网签锁货";
//            case "50": {
//                if (getMatchStatus() != null)
//                    switch (getMatchStatus()) {
//                        case "00":
//                            return "待匹配";
//                        case "10":
//                            return "匹配中";
//                        case "20":
//                            return "匹配成功";
//                        case "30":
//                            return "匹配结束";
//                    }
//            }
//            case "99":
//                return "已结束";
//            case "A0":
//                return "已撤销";
//        }
//        return "";
    }

    //能否撤销货源
    public boolean canRevoke() {
        return "1".equals(revokeBtnFlag);
    }

    public boolean canCopy() {
        return "1".equals(copyBtnFlag);
    }

    public boolean activate() {
        return !"00".equals(getStatus())
                && !"99".equals(getStatus())
                && !"A0".equals(getStatus())
                && !"A9".equals(getStatus());
    }

    public static class TransporterInfo implements Parcelable {
        private String cargoUuid;
        private String tradeUuid;
        private String status_;
        private String status;
        private String createDttm;
        private String carrierNo;

        private String payerMemberCode;
        private String carrierMemberCode;
        private String signDttm;

        private String transportAmt;

        private int legalNum;

        private String invoiceUploadDttm;
        private String receiptUploadDttm;

        private String planLoadingDttm;
        private String realLoadingDttm;

        private double bookWeight;
        private double loadWeight;
        private double diffWeight;
        private double unloadWeight;

        private double bookUnitCt;
        private double unloadUnitCt;
        private double loadUnitCt;
        private double diffUnitCt;

        private String damage;
        private String attitude;
        private String remark;
        private String timeliness;

        private String cachedDays;

        public double getBookCargo(int book_ref_type) {
            if (book_ref_type == 0)
                return bookWeight;
            return bookUnitCt;
        }

        public double getLoss(int book_ref_type) {
            if (book_ref_type == 0)
                return loadWeight - unloadWeight;
            return loadUnitCt - unloadUnitCt;
        }

        public String getLegalNum() {
            if (legalNum == 0)
                return "无";
            return legalNum + "次";
        }

        public String getTransportDays() {
            if (invoiceUploadDttm == null || receiptUploadDttm == null)
                return "0天";
            if (cachedDays != null)
                return cachedDays;
            Date iud = SimpleDate.parseDate(invoiceUploadDttm);
            Date rud = SimpleDate.parseDate(receiptUploadDttm);
            int day = (int) ((rud.getTime() - iud.getTime()) / SimpleDate.DAY_MILLISECONDS);
            cachedDays = day + "天";
            return cachedDays;
        }

        public String getCarrierNo() {
            return carrierNo;
        }

        public String getDamage() {
            return damage == null ? "未评价" : damage;
        }

        public String getAttitude() {
            return attitude == null ? "未评价" : attitude;
        }

        public String getTimeliness() {
            return timeliness == null ? "未评价" : timeliness;
        }

        public String getRemark() {
            return remark;
        }

        public TransporterInfo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.cargoUuid);
            dest.writeString(this.tradeUuid);
            dest.writeString(this.status_);
            dest.writeString(this.status);
            dest.writeString(this.createDttm);
            dest.writeString(this.carrierNo);
            dest.writeString(this.payerMemberCode);
            dest.writeString(this.carrierMemberCode);
            dest.writeString(this.signDttm);
            dest.writeString(this.transportAmt);
            dest.writeInt(this.legalNum);
            dest.writeString(this.invoiceUploadDttm);
            dest.writeString(this.receiptUploadDttm);
            dest.writeString(this.planLoadingDttm);
            dest.writeString(this.realLoadingDttm);
            dest.writeDouble(this.bookWeight);
            dest.writeDouble(this.loadWeight);
            dest.writeDouble(this.diffWeight);
            dest.writeDouble(this.unloadWeight);
            dest.writeDouble(this.bookUnitCt);
            dest.writeDouble(this.unloadUnitCt);
            dest.writeDouble(this.loadUnitCt);
            dest.writeDouble(this.diffUnitCt);
            dest.writeString(this.damage);
            dest.writeString(this.attitude);
            dest.writeString(this.remark);
            dest.writeString(this.timeliness);
            dest.writeString(this.cachedDays);
        }

        protected TransporterInfo(Parcel in) {
            this.cargoUuid = in.readString();
            this.tradeUuid = in.readString();
            this.status_ = in.readString();
            this.status = in.readString();
            this.createDttm = in.readString();
            this.carrierNo = in.readString();
            this.payerMemberCode = in.readString();
            this.carrierMemberCode = in.readString();
            this.signDttm = in.readString();
            this.transportAmt = in.readString();
            this.legalNum = in.readInt();
            this.invoiceUploadDttm = in.readString();
            this.receiptUploadDttm = in.readString();
            this.planLoadingDttm = in.readString();
            this.realLoadingDttm = in.readString();
            this.bookWeight = in.readDouble();
            this.loadWeight = in.readDouble();
            this.diffWeight = in.readDouble();
            this.unloadWeight = in.readDouble();
            this.bookUnitCt = in.readDouble();
            this.unloadUnitCt = in.readDouble();
            this.loadUnitCt = in.readDouble();
            this.diffUnitCt = in.readDouble();
            this.damage = in.readString();
            this.attitude = in.readString();
            this.remark = in.readString();
            this.timeliness = in.readString();
            this.cachedDays = in.readString();
        }

        public static final Creator<TransporterInfo> CREATOR = new Creator<TransporterInfo>() {
            @Override
            public TransporterInfo createFromParcel(Parcel source) {
                return new TransporterInfo(source);
            }

            @Override
            public TransporterInfo[] newArray(int size) {
                return new TransporterInfo[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.cargoUuid);
        dest.writeString(this.matchStatus);
        dest.writeString(this.matchStatus_);
        dest.writeString(this.cargoMatchStatus);
        dest.writeInt(this.bookRefType);
        dest.writeInt(this.chargeReferType);
        dest.writeString(this.loadingProvince);
        dest.writeString(this.loadingCity);
        dest.writeString(this.loadingCountry);
        dest.writeString(this.unloadProvince);
        dest.writeString(this.unloadCity);
        dest.writeString(this.unloadCountry);
        dest.writeString(this.unloadContacts);
        dest.writeString(this.unloadContactsName);
        dest.writeString(this.unloadContactsTel);
        dest.writeInt(this.billingType);
        dest.writeString(this.billingType_);
        dest.writeString(this.loadingStartDt);
        dest.writeString(this.loadingEndDt);
        dest.writeInt(this.transportType);
        dest.writeString(this.createTs);
        dest.writeTypedList(this.transportTradeDtlRespDtoList);
        dest.writeString(this.revokeBtnFlag);
        dest.writeString(this.copyBtnFlag);
        dest.writeString(this.orginCargonKindName);
        dest.writeDouble(this.unitCt);
        dest.writeDouble(this.conferUnitAmt);
    }

    public CargoSourceItem() {
    }

    protected CargoSourceItem(Parcel in) {
        super(in);
        this.cargoUuid = in.readString();
        this.matchStatus = in.readString();
        this.matchStatus_ = in.readString();
        this.cargoMatchStatus = in.readString();
        this.bookRefType = in.readInt();
        this.chargeReferType = in.readInt();
        this.loadingProvince = in.readString();
        this.loadingCity = in.readString();
        this.loadingCountry = in.readString();
        this.unloadProvince = in.readString();
        this.unloadCity = in.readString();
        this.unloadCountry = in.readString();
        this.unloadContacts = in.readString();
        this.unloadContactsName = in.readString();
        this.unloadContactsTel = in.readString();
        this.billingType = in.readInt();
        this.billingType_ = in.readString();
        this.loadingStartDt = in.readString();
        this.loadingEndDt = in.readString();
        this.transportType = in.readInt();
        this.createTs = in.readString();
        this.transportTradeDtlRespDtoList = in.createTypedArrayList(TransporterInfo.CREATOR);
        this.revokeBtnFlag = in.readString();
        this.copyBtnFlag = in.readString();
        this.orginCargonKindName = in.readString();
        this.unitCt = in.readDouble();
        this.conferUnitAmt = in.readDouble();
    }

    public static final Creator<CargoSourceItem> CREATOR = new Creator<CargoSourceItem>() {
        @Override
        public CargoSourceItem createFromParcel(Parcel source) {
            return new CargoSourceItem(source);
        }

        @Override
        public CargoSourceItem[] newArray(int size) {
            return new CargoSourceItem[size];
        }
    };
}
