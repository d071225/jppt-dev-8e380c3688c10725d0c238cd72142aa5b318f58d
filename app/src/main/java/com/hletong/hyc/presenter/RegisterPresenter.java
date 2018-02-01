package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.RegisterContract;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;

/**
 * 注册验证码获取及验证
 * Created by cc on 2017/1/4.
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.IRegisterView> implements RegisterContract.IRegisterPresenter {

    public RegisterPresenter(RegisterContract.IRegisterView view) {
        super(view);
    }

    @Override
    public void getVerifyCode() {
        final RegisterInfo registerInfo = getView().getRegisterInfo();
        ItemRequestValue<CommonResult> requestValue = registerInfo.validatePhoneNumber(getView());
        if (requestValue == null) {
            return;
        }
        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(@NonNull CommonResult response) {
                getView().onVerifyCode(response.getErrorInfo());
            }
        });
    }

    /**
     * 先检测验证码，然后提交注册信息
     */
    @Override
    public void register() {
        final RegisterInfo registerInfo = getView().getRegisterInfo();
        ItemRequestValue<CommonResult> requestValue = registerInfo.validateVerifyCode(getView());
        if (requestValue == null) {
            return;
        }
        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(@NonNull CommonResult response) {
                submit(registerInfo);
            }
        });

    }

    private void submit(final RegisterInfo registerInfo) {
        ItemRequestValue<CommonResult> requestValue = registerInfo.validate(getView());
        if (requestValue == null) {
            return;
        }
        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(@NonNull CommonResult response) {
                getView().onSuccess(registerInfo, response.getErrorInfo());
            }
        });
    }
}
