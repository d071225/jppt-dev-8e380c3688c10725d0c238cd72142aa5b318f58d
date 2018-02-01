package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoContract;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;

/**
 * Created by dongdaqing on 2017/8/16.
 */

public class CargoPresenter2 extends BasePresenter<CargoContract.View2> implements CargoContract.Presenter2 {

    public CargoPresenter2(@NonNull CargoContract.View2 baseView) {
        super(baseView);
    }

    @Override
    public void delete(String cargoUUid) {
        if (Validator.isNotNull(cargoUUid, getView(), "参数有误，无法删除货源")) {
            new ExecutorCall<CommonResult>(EasyOkHttp.get(Constant.DELETE_CARGO).param("goodsUuid",cargoUUid).build()).enqueue(new EasyCallback<CommonResult>(getView()) {
                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    getView().success(response.getErrorInfo());
                    getView().refreshView();
                }
            });
        }
    }
}
