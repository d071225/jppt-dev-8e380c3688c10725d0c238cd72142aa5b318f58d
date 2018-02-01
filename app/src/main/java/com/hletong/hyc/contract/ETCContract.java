package com.hletong.hyc.contract;

import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.EtcApplyInfo;
import com.hletong.hyc.model.VehicleInfo;
import com.hletong.hyc.model.validate.EtcTransporterInfoPart1;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

import java.util.List;

/**
 * Created by ddq on 2017/2/3.
 */

public interface ETCContract {
    interface View extends IBaseView{
        void initAddMode();//添加车辆
        void initAppendMode(VehicleInfo vehicleInfo);//选择车辆-补填信息
        void initTruckType(DictionaryItem truckType);
        void success(EtcApplyInfo transporterInfo);
    }

    interface Presenter extends IBasePresenter{
        void checkPlate(String plate);
        void vehicleInfoValidate(EtcTransporterInfoPart1 vehicleInfo);
        void chooseTruckType(List<DictionaryItem> items);
    }
}
