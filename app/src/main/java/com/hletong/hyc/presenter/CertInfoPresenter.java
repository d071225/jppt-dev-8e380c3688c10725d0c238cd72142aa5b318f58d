package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CertInfoContract;
import com.hletong.hyc.model.CertInfo;
import com.hletong.hyc.util.CertParamHelper;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.GetRequest;
import com.xcheng.okhttp.request.OkCall;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by chengxin on 2017/7/3.
 */
public class CertInfoPresenter extends BasePresenter<CertInfoContract.View> implements CertInfoContract.Presenter {

    public CertInfoPresenter(@NonNull CertInfoContract.View baseView) {
        super(baseView);
    }

    @Override
    public void getCertInfo() {
        GetRequest request = EasyOkHttp.get(Constant.INFO_EXIST_CERT).tag(tag()).build();
        new ExecutorCall<CertInfo>(request).enqueue(new EasyCallback<CertInfo>(getView()) {
            @Override
            public void onSuccess(OkCall<CertInfo> okCall, CertInfo response) {
                getView().initCertInfo(response);
            }
        });
    }

    @Override
    public void summitTruckPerson(Map<String, Object> params) {
        JSONObject jsonObject = CertParamHelper.getMapJson(params);
        GetRequest request = EasyOkHttp.get(Constant.SUBMIT_CERT).tag(tag()).param("params", jsonObject.toString()).build();
        new ExecutorCall<CommonResult>(request).enqueue(new EasyCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                getView().certSuccess(response.getErrorInfo());
            }
        });
    }
}
