package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.AddressContract;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/8/14.
 * 地址管理
 */

public class AddressPresenter2 extends BasePresenter<AddressContract.View2> implements AddressContract.Presenter2 {

    public AddressPresenter2(@NonNull AddressContract.View2 baseView) {
        super(baseView);
    }

    @Override
    public void delete(String contactUuid) {
        if (Validator.isNotNull(contactUuid, getView(), "参数出错")) {

            OkRequest request = EasyOkHttp.get(Constant.CARGO_CONTACT_DELETE)
                    .param("contactUuid", contactUuid)
                    .build();

            new ExecutorCall<CommonResult>(request).enqueue(new EasyCallback<CommonResult>(getView()) {
                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    getView().success(response.getErrorInfo());
                    getView().refreshList();
                }
            });
        }
    }
}
