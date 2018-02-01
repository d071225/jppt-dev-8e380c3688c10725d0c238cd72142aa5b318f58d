package com.hletong.hyc.model.validate.truck;

import android.text.TextUtils;

import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.TruckCompleteInfo;
import com.hletong.hyc.model.validate.Paperwork;
import com.hletong.hyc.model.validate.Validate;
import com.hletong.hyc.model.validate.ship.ShipVerifyInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.ParamsHelper;

import java.util.List;

/**
 * Created by dongdaqing on 2017/6/17.
 */

public class TruckVerifyInfo implements Validate<CommonResult> {
    private String plate;// 车牌号
    private DictionaryItem licensePlateColor;// 车牌颜色
    private DictionaryItem truckType;// 车辆类型
    private DictionaryItem sex;// 驾驶员性别
    private String maxLength;// 车厢长度
    private String vehicleMass;// 车辆载质量
    private String loadedVehicleQuality;// 满载车辆质量
    private List<Paperwork> paperStr;//证件资料
    private TruckCompleteInfo mTruckInfo;

    public TruckVerifyInfo(String plate, TruckCompleteInfo truckInfo, List<Paperwork> paperStr) {
        this.plate = plate;
        this.mTruckInfo = truckInfo;
        this.paperStr = paperStr;
        if (mTruckInfo != null) {
            this.licensePlateColor = mTruckInfo.getTruck_color();
            this.truckType = mTruckInfo.getTruck_type();
            this.sex = mTruckInfo.getSexType();
            this.maxLength = mTruckInfo.getTruckLength();
            this.vehicleMass = mTruckInfo.getVehicleMass();
            this.loadedVehicleQuality = mTruckInfo.getLoadedVehicleQuality();
        }
    }

    @Override
    public ItemRequestValue<CommonResult> validate(IBaseView baseView) {
        boolean valid = false;
        for (Paperwork p : paperStr) {
            valid = valid || p.paramsValid();
        }
        if (!valid) {
            baseView.handleError(ErrorFactory.getParamError("请选择一种证件上传"));
            return null;
        }

        if (mTruckInfo == null){
            baseView.handleError(ErrorFactory.getParamError("请填写车辆信息"));
            return null;
        }

        if (truckType == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择车辆类型"));
            return null;
        }

        if (licensePlateColor == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择车牌颜色"));
            return null;
        }

        if (sex == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择驾驶员性别"));
            return null;
        }

        if (TextUtils.isEmpty(plate)) {
            baseView.handleError(ErrorFactory.getParamError("请填写车牌号"));
            return null;
        }
        if (TextUtils.isEmpty(maxLength)) {
            baseView.handleError(ErrorFactory.getParamError("请填写车辆长度"));
            return null;
        }
        if (TextUtils.isEmpty(vehicleMass)) {
            baseView.handleError(ErrorFactory.getParamError("请填写车辆载重量"));
            return null;
        }
        if (TextUtils.isEmpty(loadedVehicleQuality)) {
            baseView.handleError(ErrorFactory.getParamError("请填写满载车辆重量"));
            return null;
        }

        return new ItemRequestValue<CommonResult>(
                baseView.hashCode(),
                Constant.getUrl(Constant.COMPLETE_VEHICLE_INFO),
                new ParamsHelper()
                        .put("plate", plate)
                        .put("licensePlateColor", licensePlateColor.getId())
                        .put("truckType", truckType.getId())
                        .put("sex", sex.getId())
                        .put("maxLength", maxLength)
                        .put("vehicleMass", vehicleMass)
                        .put("loadedVehicleQuality", loadedVehicleQuality)
                        .put("paperStr", ShipVerifyInfo.formatAsString(paperStr))) {
        };
    }
}
