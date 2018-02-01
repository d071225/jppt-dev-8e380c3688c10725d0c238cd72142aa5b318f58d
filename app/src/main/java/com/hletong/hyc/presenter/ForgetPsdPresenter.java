package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.ForgetPsw;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.AnimCallBack;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by chengxin on 2017/12/25.
 */
public class ForgetPsdPresenter extends BasePresenter<ForgetPsw.View> implements ForgetPsw.Presenter {
    private String sessionId = Constant.getDeviceId();

    public ForgetPsdPresenter(@NonNull ForgetPsw.View baseView) {
        super(baseView);
    }

    @Override
    public void getVerifyCode(String memberName, String phoneNumber) {
        if (isEmpty(memberName)) {
            handleMessage("会员名不能为空");
            return;
        } else if (isEmpty(phoneNumber)) {
            handleMessage("手机号不能为空");
            return;
        }
        OkRequest request = EasyOkHttp.get("/mobile/public/hletong/api/sys/security/code")
                .param("loginName", memberName)
                .param("tel", phoneNumber)
                .param("appType", BuildConfig.app_type)
                .param("sessionId", sessionId)
                .build();
        new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(getView()) {
            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                handleMessage(response.getErrorInfo());
                getView().httpVerifyCode();
            }
        });
    }

    @Override
    public void resetPsd(String memberName, String phoneNumber, String verifyCode) {
        if (isEmpty(memberName)) {
            handleMessage("会员名不能为空");
            return;
        } else if (isEmpty(phoneNumber)) {
            handleMessage("手机号不能为空");
            return;
        } else if (isEmpty(verifyCode)) {
            handleMessage("验证码不能为空");
            return;
        }
        OkRequest request = EasyOkHttp.get("/mobile/public/hletong/api/sys/login/pwd")
                .param("loginName", memberName)
                .param("tel", phoneNumber)
                .param("securityCode", verifyCode)
                .param("sessionId", sessionId)
                .build();
        new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(getView()) {
            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                getView().httpResetSuccess();
            }
        });
    }
}
