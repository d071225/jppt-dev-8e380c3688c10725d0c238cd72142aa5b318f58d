package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoContactContract;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/8/21.
 */

public class CargoContactPresenter2 extends BasePresenter<CargoContactContract.View2> implements CargoContactContract.Presenter2 {

    public CargoContactPresenter2(@NonNull CargoContactContract.View2 baseView) {
        super(baseView);
    }

    @Override
    public void delete(String id) {
        OkRequest request = EasyOkHttp.get(Constant.DELETE_CONSIGNOR)
                .tag(getView().hashCode())
                .param("contactUuid", id)
                .build();

        new ExecutorCall<CommonResult>(request).enqueue(new EasyCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                getView().success(response.getErrorInfo());
                getView().refresh();
            }
        });
    }
}
