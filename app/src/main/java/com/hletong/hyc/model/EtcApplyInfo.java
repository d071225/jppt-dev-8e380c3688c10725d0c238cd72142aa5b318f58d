package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.hyc.model.validate.EtcTransporterInfoPart1;
import com.hletong.hyc.model.validate.EtcTransporterInfoPart2;
import com.hletong.mob.util.ParamsHelper;
import com.hletong.mob.util.SimpleDate;

public class EtcApplyInfo implements Parcelable {
    private String memberCode;
    private String hletCardType;
    private String plate;
    private String truckOwnerName;
    private String truckIdentifyCode;
    private String truckType;
    private String truckColor;
    private String truckSeats;
    private String vehicleMass;
    private String loadedVehicleQuality;
    //------
    private String vehicleType; //行驶证车辆类型;
    private String vehicleModel;//行驶证品牌类型;
    private String engineNum;//发动机号
    private String maintenanceMass;//整备质量
    private String permittedTowWeight;//准牵引总质量
    private String outsideDimensions;//外廓尺寸


    private String addrProvince;
    private String addrCity;
    private String addrCountry;
    private String addrAddress;
    private String truckLength;

    private String chineseName;
    private String spellName;
    private int documentType;//证件类型 1=>身份证
    private String documentCode;
    private int documentValid = 0;//有效期 0=>非长期有效，1=>长期有效，默认非长期有效
    private String documentValidDt;
    private int sex;//0-男，1-女
    private String birthday;
    private String phone;
    private String password;//子账号初始密码
    private String recommendation;

    public EtcApplyInfo(EtcTransporterInfoPart1 ti, VehicleInfo vehicleInfo) {
        plate = ti.getPlate();
        truckOwnerName = ti.getTruckOwnerName();
        truckIdentifyCode = ti.getTruckIdentifyCode();
        if (ti.getTruckType() != null)
            truckType = ti.getTruckType().getId();
        if (ti.getTruckColor() != null)
            truckColor = ti.getTruckColor().getId();
        truckSeats = ti.getSeats();
        vehicleMass = ti.getVehicleMass();
        loadedVehicleQuality = ti.getLoadedVehicleQuality();
        truckLength = ti.getTruckLength();
        addrProvince = ti.getAddress().getRealProvince();
        addrCity = ti.getAddress().getRealCity();
        addrCountry = ti.getAddress().getRealCounty();
        addrAddress = ti.getAddress().getDetails();
        hletCardType = ti.getCardID();

        vehicleType = ti.getVehicleType();
        vehicleModel = ti.getVehicleModel();
        engineNum = ti.getEngineNum();
        maintenanceMass = ti.getMaintenanceMass();
        permittedTowWeight = ti.getPermittedTowWeight();
        outsideDimensions = ti.getOutsideDimensions();

        if (vehicleInfo == null)
            return;

        chineseName = vehicleInfo.getTruckName();
        documentType = 1;//默认身份证
        documentCode = vehicleInfo.getDentityNo();
        phone = vehicleInfo.getMemberTel();
        documentValidDt = vehicleInfo.getEndDt();
        if (documentValidDt != null && documentValidDt.length() == 8) {
            documentValid = 0;
        } else {
            documentValid = 1;
        }
    }

    public void set(EtcTransporterInfoPart2 ti) {
        chineseName = ti.getName();
        spellName = ti.getSpell();
        documentType = Integer.parseInt(ti.getId_type().getId());
        documentCode = ti.getId_no();
        documentValid = Integer.parseInt(ti.getId_validity().getId());
        //非长期有效的情况下再赋值，否则报空指针错误
        if (ti.getId_validity().getId().equals("0")) {
            documentValidDt = ti.getDate().dateString(true, "");
        }
        birthday = ti.getBirthday().dateString(true, "");
        sex = Integer.parseInt(ti.getSex().getId());
        phone = ti.getPhone();
        recommendation = ti.getRecommendation();
    }

    public void setHletCardType(String hletCardType) {
        this.hletCardType = hletCardType;
    }

    public int getDocumentValid() {
        return documentValid;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getNameForDisplay() {
        if (chineseName != null)
            return chineseName;
        return truckOwnerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getPlate() {
        return plate;
    }

    public String getHletCardType() {
        return hletCardType;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public String getDocumentValidDt() {
        return documentValidDt;
    }

    public SimpleDate getParsedDVD() {
        return SimpleDate.parse(documentValidDt);
    }

    public ParamsHelper getParams() {
        ParamsHelper object = new ParamsHelper();
        object.put("memberCode", memberCode);
        object.put("hletCardType", hletCardType);
        object.put("plate", plate);
        object.put("truckOwnerName", truckOwnerName);
        object.put("truckIdentifyCode", truckIdentifyCode);
        object.put("truckType", truckType);
        object.put("truckColor", truckColor);
        object.put("truckSeats", truckSeats);
        object.put("vehicleMass", vehicleMass);
        object.put("loadedVehicleQuality", loadedVehicleQuality);

        object.put("vehicleModel", vehicleModel);
        object.put("vehicleType", vehicleType);
        object.put("engineNum", engineNum);
        object.put("maintenanceMass", maintenanceMass);
        object.put("permittedTowWeight", permittedTowWeight);
        object.put("outsideDimensions", outsideDimensions);
        
        object.put("addrProvince", addrProvince);
        object.put("addrCity", addrCity);
        object.put("addrCountry", addrCountry);
        object.put("addrAddress", addrAddress);
        object.put("truckLength", truckLength);
        object.put("chineseName", chineseName);
        object.put("spellName", spellName);
        object.put("documentType", documentType);//证件类型 1=>身份证
        object.put("documentCode", documentCode);
        object.put("documentValid", documentValid);//有效期 0=>非长期有效，1=>长期有效，默认非长期有效
        if (documentValid == 0)
            object.put("documentValidDt", documentValidDt);

        object.put("sex", sex);//0-男，1-女
        object.put("birthday", birthday);
        object.put("phone", phone);
        if (recommendation != null) {
            object.put("recommendation", recommendation);
        }
        return object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberCode);
        dest.writeString(this.hletCardType);
        dest.writeString(this.plate);
        dest.writeString(this.truckOwnerName);
        dest.writeString(this.truckIdentifyCode);
        dest.writeString(this.truckType);
        dest.writeString(this.truckColor);
        dest.writeString(this.truckSeats);
        dest.writeString(this.vehicleMass);
        dest.writeString(this.loadedVehicleQuality);

        dest.writeString(this.vehicleModel);
        dest.writeString(this.vehicleType);
        dest.writeString(this.engineNum);
        dest.writeString(this.maintenanceMass);
        dest.writeString(this.permittedTowWeight);
        dest.writeString(this.outsideDimensions);

        dest.writeString(this.addrProvince);
        dest.writeString(this.addrCity);
        dest.writeString(this.addrCountry);
        dest.writeString(this.addrAddress);
        dest.writeString(this.truckLength);
        dest.writeString(this.chineseName);
        dest.writeString(this.spellName);
        dest.writeInt(this.documentType);
        dest.writeString(this.documentCode);
        dest.writeInt(this.documentValid);
        dest.writeString(this.documentValidDt);
        dest.writeInt(this.sex);
        dest.writeString(this.birthday);
        dest.writeString(this.phone);
        dest.writeString(this.password);
    }

    protected EtcApplyInfo(Parcel in) {
        this.memberCode = in.readString();
        this.hletCardType = in.readString();
        this.plate = in.readString();
        this.truckOwnerName = in.readString();
        this.truckIdentifyCode = in.readString();
        this.truckType = in.readString();
        this.truckColor = in.readString();
        this.truckSeats = in.readString();
        this.vehicleMass = in.readString();
        this.loadedVehicleQuality = in.readString();

        this.vehicleModel = in.readString();
        this.vehicleType = in.readString();
        this.engineNum = in.readString();
        this.maintenanceMass = in.readString();
        this.permittedTowWeight = in.readString();
        this.outsideDimensions = in.readString();

        this.addrProvince = in.readString();
        this.addrCity = in.readString();
        this.addrCountry = in.readString();
        this.addrAddress = in.readString();
        this.truckLength = in.readString();
        this.chineseName = in.readString();
        this.spellName = in.readString();
        this.documentType = in.readInt();
        this.documentCode = in.readString();
        this.documentValid = in.readInt();
        this.documentValidDt = in.readString();
        this.sex = in.readInt();
        this.birthday = in.readString();
        this.phone = in.readString();
        this.password = in.readString();
    }

    public static final Creator<EtcApplyInfo> CREATOR = new Creator<EtcApplyInfo>() {
        @Override
        public EtcApplyInfo createFromParcel(Parcel source) {
            return new EtcApplyInfo(source);
        }

        @Override
        public EtcApplyInfo[] newArray(int size) {
            return new EtcApplyInfo[size];
        }
    };
}
