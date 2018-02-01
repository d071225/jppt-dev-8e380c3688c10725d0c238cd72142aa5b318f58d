package com.hletong.hyc.model.validate;

import com.hletong.hyc.model.Cargo;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/10.
 */

public class CargoInfo implements Validate2 {
    private String cargoName;
    private DictionaryItem measureType;
    //    private String freightPrice;
    private String length;
    private String width;
    private String height;
    private DictionaryItem unit;
    private String wasteRt;
    private DictionaryItem transporterType;
    private DictionaryItem transporterModel;
    private DictionaryItem transporterLength;
    private String attachment;

    private String cargoUuid;

    public CargoInfo(String cargoName, String length, String width, String height, String wasteRt) {
        this.cargoName = cargoName;
        this.length = length;
        this.width = width;
        this.height = height;
        this.wasteRt = wasteRt;
    }

    public void set(DictionaryItem measureType, DictionaryItem unit, DictionaryItem transporterType, DictionaryItem transporterModel, DictionaryItem transporterLength) {
        this.measureType = measureType;
        this.unit = unit;
        this.transporterType = transporterType;
        this.transporterModel = transporterModel;
        this.transporterLength = transporterLength;
    }

    public void setCargo(Cargo cargo) {
        if (cargo != null) {
            cargoUuid = cargo.getCargo_uuid();
            attachment = cargo.getCargoFileId();
        }
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isNotNull(cargoName, baseView, "货物名称不能为空")
                && Validator.isNotNull(measureType, baseView, "计量方式不能为空")
                && Validator.isNotNull(unit, baseView, "单位不能为空")
                && Validator.isNotNull(transporterType, baseView, "运输方式不能为空")
                && Validator.isNotNull(wasteRt, baseView, "运输损耗不能为空")
                && Validator.isNotNull(transporterModel, baseView, transporterType.getText().equals("船舶") ? "船舶类型不能为空" : "车辆类型不能为空")
                && (transporterType.getText().equals("船舶") || Validator.isNotNull(transporterLength, baseView, "车辆长度不能为空")))
            return EasyOkHttp.get(cargoUuid == null ? Constant.ADD_NEW_CARGO : Constant.MODIFY_CARGO)
                    .tag(baseView.hashCode())
                    .param("goodsUuid", cargoUuid)
                    .param("orginCargonKindName", cargoName)
                    .param("length", length)
                    .param("width", width)
                    .param("height", height)
                    .param("measureType", measureType.getId())
                    .param("units", unit.getId())
                    .param("maxWastageRt", Double.parseDouble(wasteRt) / 1000)
                    .param("cargoFileId", attachment)
                    .param("transportType", transporterType.getId())
                    .param("carrierModelType", transporterModel.getId())
                    .param("carrierLengthType", transporterLength == null ? "" : transporterLength.getId())
                    .build();
        return null;
    }
}