package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.ETCContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.EtcApplyInfo;
import com.hletong.hyc.model.VehicleInfo;
import com.hletong.hyc.model.validate.EtcTransporterInfoPart1;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.util.ParamsHelper;

import java.util.List;

/**
 * Created by ddq on 2017/2/3.
 */

public class ETCPresenter extends BasePresenter<ETCContract.View> implements ETCContract.Presenter {
    private VehicleInfo mVehicleInfo;
    private String cardType;
    private boolean plateAvailable = false;//车牌号是否可用

    public ETCPresenter(ETCContract.View view, VehicleInfo vehicleInfo, String cardType) {
        super(view);
        mVehicleInfo = vehicleInfo;
        this.cardType = cardType;
    }

    @Override
    public void start() {
        if (mVehicleInfo == null)
            getView().initAddMode();
        else {
            plateAvailable = true;//不需要校验车牌号
            getView().initAppendMode(mVehicleInfo);
        }
    }

    @Override
    public void checkPlate(String plate) {
        if (!RegexUtil.isCarNumber(plate))
        return;

        simpleSubmit(
                Constant.getUrl(Constant.CHECK_PLATE_OR_SHIP),
                new ParamsHelper().put("plate", plate)
        );
    }

    @Override
    protected void simpleSubmitSucceed(CommonResult cr) {
        plateAvailable = true;
    }

    @Override
    public void vehicleInfoValidate(EtcTransporterInfoPart1 etcTransporterInfoPart1) {
        if (!plateAvailable) {
            handleMessage("车牌号不可用");
            return;
        }

        if (etcTransporterInfoPart1.validate(getView(), true)) {
            EtcApplyInfo etcApplyInfo = new EtcApplyInfo(etcTransporterInfoPart1, mVehicleInfo);
            etcApplyInfo.setHletCardType(cardType);
            getView().success(etcApplyInfo);
        }
    }

    @Override
    public void chooseTruckType(List<DictionaryItem> items) {
        if (mVehicleInfo == null || mVehicleInfo.getTruckType() == null){//新增车辆
            getView().initTruckType(items.get(0));
            return;
        }

        //选择车辆
        for (DictionaryItem type : items){
            if (type.getId().equals(mVehicleInfo.getTruckType())){
                getView().initTruckType(type);
                break;
            }
        }
    }
}
