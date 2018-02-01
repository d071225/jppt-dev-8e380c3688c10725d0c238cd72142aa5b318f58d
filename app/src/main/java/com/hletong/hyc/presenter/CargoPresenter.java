package com.hletong.hyc.presenter;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoContract;
import com.hletong.hyc.model.Cargo;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.FileInfo;
import com.hletong.hyc.model.Images;
import com.hletong.hyc.model.validate.CargoInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2017/3/10.
 * 发布货源-新增，删除，修改货源
 */

public class CargoPresenter extends FilePresenter<CargoContract.View> implements CargoContract.Presenter {
    private Cargo mCargo;
    private DictionaryItem measureType;
    private DictionaryItem unit;
    private DictionaryItem transportType;
    private DictionaryItem carrierModel;
    private DictionaryItem carrierLength;

    private CargoInfo mCargoInfo;

    public CargoPresenter(CargoContract.View view, Cargo cargo) {
        super(view);
        this.mCargo = cargo;
    }

    @Override
    public void start() {
        if (mCargo == null) {
            getView().showTitle("添加货物");
            getView().showPrimaryPicker();
            getView().showSubmitButton("上传");

            List<DictionaryItem> list = new ArrayList<>();
            list.add(new DictionaryItem(String.valueOf(R.id.measureType), "measureTypeEnum"));
            list.add(new DictionaryItem(String.valueOf(R.id.unit), "unitsEnum"));
            list.add(new DictionaryItem(String.valueOf(R.id.transporter_type), "transportTypeEnum"));
            getView().prefetch(list);
        } else {
            getView().showTitle("编辑货物");

            measureType = mCargo.getMeasureTypeAsDI();
            unit = mCargo.getUnitAsDI();
            transportType = mCargo.getTransportTypeAsDI();
            carrierModel = mCargo.getCarrierModelAsDI();
            carrierLength = mCargo.getCarrierLengthAsDI();

            getView().showCargo(
                    mCargo.getOrgin_cargon_kind_name(),
                    measureType.getText(),
                    unit.getText(),
                    mCargo.getLength(),
                    mCargo.getWidth(),
                    mCargo.getHeight(),
                    mCargo.getMax_wastage_rt(true),
                    transportType.getText()
            );

            if ("车辆".equals(transportType.getText())) {
                getView().showCarrierLength(carrierLength.getText());
                getView().showCarrierModel(carrierModel.getText(), "车型要求");
            } else {
                getView().showCarrierModel(carrierModel.getText(), "船型要求");
            }

            getView().showSubmitButton("保存");

            if (mCargo.getCargoFileId() != null) {
                OkRequest request = EasyOkHttp.get(String.format(Constant.getUrl(Constant.FETCH_GROUP_PICTURES_URL), mCargo.getCargoFileId())).build();

                new ExecutorCall<Images>(request).enqueue(new UICallback<Images>() {
                    @Override
                    public void onError(OkCall<Images> okCall, EasyError error) {

                    }

                    @Override
                    public void onSuccess(OkCall<Images> okCall, Images response) {
                        if (response.empty())
                            return;
                        List<String> images = new ArrayList<>(response.getList().size());
                        for (int index = 0; index < response.getList().size(); index++) {
                            images.add(response.getList().get(index).getFileDownloadUrl());
                        }
                        getView().showImages(images);
                    }
                });
                getView().showSecondPicker();
            } else {
                getView().showPrimaryPicker();
            }
        }
    }

    @Override
    public void itemChanged(DictionaryItem item, int extra) {
        switch (extra) {
            case R.id.measureType:
                measureType = item;
                break;
            case R.id.unit:
                unit = item;
                break;
            case R.id.transporter_type:
                if (transportType == null || !item.getText().equals(transportType.getText())) {
                    carrierLength = null;
                    carrierModel = null;
                    List<DictionaryItem> list = new ArrayList<>();
                    if ("船舶".equals(item.getText())) {
                        getView().hideCarrierLength();
                        getView().showCarrierModel(null, "船型要求");
                        list.add(new DictionaryItem(String.valueOf(R.id.transporter_model), "shipCarrierModelTypeEnum"));
                    } else {
                        getView().showCarrierLength(null);
                        getView().showCarrierModel(null, "车型要求");
                        list.add(new DictionaryItem(String.valueOf(R.id.transporter_model), "truckCarrierModelTypeEnum"));
                        list.add(new DictionaryItem(String.valueOf(R.id.transporter_length), "truckCarrierLengthTypeEnum"));
                    }
                    getView().prefetch(list);
                }
                transportType = item;
                break;
            case R.id.transporter_model:
                carrierModel = item;
                break;
            case R.id.transporter_length:
                carrierLength = item;
                break;
        }
    }

    @Override
    public void submit(CargoInfo cargoInfo, List<String> images) {//新增或编辑
        mCargoInfo = cargoInfo;
        cargoInfo.setCargo(mCargo);
        cargoInfo.set(measureType, unit, transportType, carrierModel, carrierLength);
        if (cargoInfo.validate(getView()) != null) {
            if (ParamUtil.isEmpty(images)) {
                new ExecutorCall<CommonResult>(mCargoInfo.validate(getView())).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
            } else {
                upload(images, Constant.getUrl(Constant.IMAGE_UPLOAD),false);
            }
        }
    }

    @Override
    public void selectTransportModel() {
        if (transportType == null) {
            getView().handleError(ErrorFactory.getParamError("请先选择运输方式"));
        } else {
            if ("车辆".equals(transportType.getText())) {
                getView().showSelector("truckCarrierModelTypeEnum", R.id.transporter_model);
            } else {
                getView().showSelector("shipCarrierModelTypeEnum", R.id.transporter_model);
            }
        }
    }

    @Override
    protected void uploadFinished(int remain, boolean error, FileInfo fileInfo) {
        if (remain == 0 && !error) {
            mCargoInfo.setAttachment(getGroupId());
            new ExecutorCall<CommonResult>(mCargoInfo.validate(getView())).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
        }
    }
}
