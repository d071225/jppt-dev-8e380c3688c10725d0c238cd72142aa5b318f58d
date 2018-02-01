package com.hletong.hyc.model.validate;

import android.text.TextUtils;

import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.StringUtil;

/**
 * Created by ddq on 2017/2/3.
 */

public class EtcTransporterInfoPart1 {
    private Address mAddress;//住址
    private DictionaryItem truckType;//车辆类型
    private DictionaryItem truckColor;//车辆颜色
    private String truckLength;//车长
    private String truckOwnerName;//车主
    private String truckIdentifyCode;//车辆识别代号
    private String seats;//座位数
    private String vehicleMass;//核定载质量
    private String loadedVehicleQuality;//满载车辆质量

    private String vehicleType; //行驶证车辆类型;
    private String vehicleModel;//行驶证品牌类型;
    private String engineNum;//发动机号
    private String maintenanceMass;//整备质量
    private String outL;//外廓尺寸
    private String outW;//外廓尺寸
    private String outH;//外廓尺寸

    private String permittedTowWeight;//准牵引总质量

    public String getVehicleMass() {
        return vehicleMass;
    }

    public String getLoadedVehicleQuality() {
        return loadedVehicleQuality;
    }

    private String plate;//车牌号
    private String memberCode;//会员编号
    private String cardID;//卡片类型

    public EtcTransporterInfoPart1(Address address, DictionaryItem truckType, DictionaryItem truckColor, String truckLength, String truckOwnerName, String truckIdentifyCode, String seats, String vehicleMass, String loadedVehicleQuality, String cardID,
                                   String vehicleType, String vehicleModel, String engineNum, String maintenanceMass, String outL, String outW, String outH, String permittedTowWeight) {
        mAddress = address;
        this.truckType = truckType;
        this.truckColor = truckColor;
        this.truckLength = truckLength;
        this.truckOwnerName = truckOwnerName;
        this.truckIdentifyCode = truckIdentifyCode;
        this.seats = seats;
        this.vehicleMass = vehicleMass;
        this.loadedVehicleQuality = loadedVehicleQuality;

        this.vehicleType = vehicleType;
        this.vehicleModel = vehicleModel;

        this.engineNum = engineNum;

        this.maintenanceMass = maintenanceMass;

        this.permittedTowWeight = permittedTowWeight;

        this.outL = outL;
        this.outW = outW;
        this.outH = outH;

        this.cardID = cardID;
    }

    /**
     * 参数校验
     *
     * @param baseView
     * @param fullCheck 有两种模式下会用到这个校验:1 => etc-新增车辆，2 => etc-选择车辆，补填车辆信息
     *                  其中第二种方式能获取到大部分的车辆信息，获取到的部分信息不进行校验
     *                  第一种方式要全部都校验
     * @return
     */
    public boolean validate(IBaseView baseView, boolean fullCheck) {
        if (fullCheck) {
            if (!RegexUtil.isCarNumber(plate)) {
                baseView.handleError(ErrorFactory.getParamError("车牌号格式不正确"));
                return false;
            }
            if (!StringUtil.isChinese(truckOwnerName)) {
                baseView.handleError(ErrorFactory.getParamError("车辆所有人必须为中文"));
                return false;
            }
            if (TextUtils.isEmpty(truckIdentifyCode)) {
                baseView.handleError(ErrorFactory.getParamError("车辆识别代号不能为空"));
                return false;
            }

            if (truckType == null) {
                baseView.handleError(ErrorFactory.getParamError("车辆类型不能为空"));
                return false;
            }

            if (TextUtils.isEmpty(truckLength)) {
                baseView.handleError(ErrorFactory.getParamError("车辆长度不能为空"));
                return false;
            }

            //if (TextUtils.isEmpty(loadedVehicleQuality)) {
            //    baseView.handleError(ErrorFactory.getParamError("行驶证总质量不能为空"));
            //    return false;
            //}

            //if (TextUtils.isEmpty(vehicleMass)) {
            //    baseView.handleError(ErrorFactory.getParamError("核定载质量不能为空"));
            //    return false;
            //}

            if (mAddress == null) {
                baseView.handleError(ErrorFactory.getParamError("地址不能为空"));
                return false;
            }

            if (TextUtils.isEmpty(mAddress.getDetails())) {
                baseView.handleError(ErrorFactory.getParamError("详细地址不能为空"));
                return false;
            }
        }

        if (cardID == null) {
            baseView.handleError(ErrorFactory.getParamError("卡片类型丢失"));
            return false;
        }

        if (truckColor == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择车牌颜色"));
            return false;
        }

        if (TextUtils.isEmpty(seats)) {
            baseView.handleError(ErrorFactory.getParamError("车辆座位数不能为空"));
            return false;
        }
        if (TextUtils.isEmpty(vehicleType)) {
            baseView.handleError(ErrorFactory.getParamError("行驶证车辆类型不能为空"));
            return false;
        }
        if (TextUtils.isEmpty(vehicleModel)) {
            baseView.handleError(ErrorFactory.getParamError("行驶证品牌型号不能为空"));
            return false;
        }
        if (TextUtils.isEmpty(engineNum)) {
            baseView.handleError(ErrorFactory.getParamError("发动机号不能为空"));
            return false;
        }
        if (TextUtils.isEmpty(maintenanceMass)) {
            baseView.handleError(ErrorFactory.getParamError("整备质量不能为空"));
            return false;
        }
        if (TextUtils.isEmpty(outL)) {
            baseView.handleError(ErrorFactory.getParamError("外廓长度不能为空"));
            return false;
        }
        if (TextUtils.isEmpty(outW)) {
            baseView.handleError(ErrorFactory.getParamError("外廓宽度不能为空"));
            return false;
        }
        if (TextUtils.isEmpty(outH)) {
            baseView.handleError(ErrorFactory.getParamError("外廓高度不能为空"));
            return false;
        }
        //if (TextUtils.isEmpty(permittedTowWeight)) {
        //    baseView.handleError(ErrorFactory.getParamError("准牵引总质量不能为空"));
        //    return false;
        //}
        return true;
    }

//    private ItemRequestValue<CommonResult> getRequestValue(String url, IBaseView mBaseView) {
//        JSONObject jsonObject = null;
//        try {
//            String json = JSONUtils.toJson(this, new TypeToken<EtcApplyInfo>() {
//            }.getType());
//            Log.i("test", json + "=====json");
//            jsonObject = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new ItemRequestValue<>(mBaseView.hashCode(), url, jsonObject, null, new TypeToken<CommonResult>() {});
//    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCardID() {
        return cardID;
    }

    public String getPlate() {
        return plate;
    }

    public Address getAddress() {
        return mAddress;
    }

    public String getSeats() {
        return seats;
    }

    public String getTruckIdentifyCode() {
        return truckIdentifyCode;
    }

    public String getTruckLength() {
        return truckLength;
    }

    public String getTruckOwnerName() {
        return truckOwnerName;
    }

    public DictionaryItem getTruckColor() {
        return truckColor;
    }

    public DictionaryItem getTruckType() {
        return truckType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public String getMaintenanceMass() {
        return maintenanceMass;
    }

    //外廓尺寸
    public String getOutsideDimensions() {
        return outL + "X" + outW + "X" + outH;
    }

    public String getPermittedTowWeight() {
        return permittedTowWeight;
    }
}
