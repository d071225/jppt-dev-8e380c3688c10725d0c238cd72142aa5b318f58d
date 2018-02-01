package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.CompleteVehicleContract;

/**
 * Created by dongdaqing on 2017/8/7.
 */

public class CompleteVehicleInfoPresenter extends FilePresenter<CompleteVehicleContract.View> implements CompleteVehicleContract.Presenter {
    public CompleteVehicleInfoPresenter(CompleteVehicleContract.View view) {
        super(view);
    }
}
