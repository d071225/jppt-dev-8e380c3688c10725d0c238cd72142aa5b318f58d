package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.RegisterContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.validate.RegisterCompanyInfo;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.model.validate.cargo.RegisterPersonalCargoInfo;
import com.hletong.hyc.model.validate.ship.RegisterPersonalShipInfo;
import com.hletong.hyc.model.validate.truck.RegisterPersonalTruckInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * 注册基本信息
 * Created by cc on 2017/1/4.
 */
public class RegisterTabPresenter extends BasePresenter<RegisterContract.IBaseInfoView> implements RegisterContract.IBaseInfoPresenter {
    public RegisterTabPresenter(RegisterContract.IBaseInfoView iBaseInfoView) {
        super(iBaseInfoView);
    }

    @Override
    public void submitCompanyInfo() {
        if (AppTypeConfig.isTransporter()) {
            commit(getView().getRegisterInfo());
        } else {
            //只有货主版验证公司名是否被注册
            commit(getView().getRegisterInfo(), Constant.getUrl(Constant.CHECK_COMPANY_MEMBER_NAME));
        }
    }

    @Override
    public void submitPersonInfo() {
        final RegisterInfo registerInfo = getView().getRegisterInfo();
        ItemRequestValue<CommonResult> requestValue = registerInfo.validateBaseInfo(getView(), Constant.getUrl(Constant.CHECK_PLATE_OR_SHIP));
        if (requestValue == null) {
            return;
        }
        /****先检查车牌号码***/
        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(@NonNull CommonResult response) {
                commit(registerInfo);
            }
        });
    }

    public void commit(final RegisterInfo registerInfo, String url) {
        if (registerInfo instanceof RegisterCompanyInfo) {
            realCommit(registerInfo, url);
            return;
        }
        checkIdCard(registerInfo, url);
    }

    public void commit(RegisterInfo registerInfo) {
        commit(registerInfo, Constant.getUrl(Constant.CHECK_MEMBER_NAME));
    }

    private void realCommit(final RegisterInfo registerInfo, String url) {
        ItemRequestValue<CommonResult> requestValue = registerInfo.validateBaseInfo(getView(), url);
        if (requestValue == null) {
            return;
        }
        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(@NonNull CommonResult response) {
                getView().onSuccess(registerInfo, null);
            }
        });
    }

    private void checkIdCard(final RegisterInfo registerInfo, final String url) {
        String name = null;
        String id = null;
        String regType = null;
        if (registerInfo instanceof RegisterPersonalCargoInfo) {
            regType = "1";
            name = ((RegisterPersonalCargoInfo) registerInfo).getPersonal_name();
            id = ((RegisterPersonalCargoInfo) registerInfo).getPersonal_identity();
        } else if (registerInfo instanceof RegisterPersonalTruckInfo) {
            regType = "2";
            name = ((RegisterPersonalTruckInfo) registerInfo).getPersonal_name();
            id = ((RegisterPersonalTruckInfo) registerInfo).getPersonal_identity();
        } else if (registerInfo instanceof RegisterPersonalShipInfo) {
            regType = "3";
            name = ((RegisterPersonalShipInfo) registerInfo).getPersonal_name();
            id = ((RegisterPersonalShipInfo) registerInfo).getPersonal_identity();
        }
        if (id == null) {
            realCommit(registerInfo, url);
            return;
        }
        OkRequest request = EasyOkHttp.get(Constant.CHECK_ID_CARD)
                .param("memberClassify", 2)
                .param("name", name)
                .param("regType", regType)
                .param("personalIdentity", id).build();
        new ExecutorCall<CommonResult>(request).enqueue(new UICallback<CommonResult>() {
            @Override
            public void onError(OkCall<CommonResult> okCall, EasyError error) {
                if (error instanceof BusiError&&((BusiError) error).getBusiCode()==-1){
                    realCommit(registerInfo, url);
                }else{
                    getView().handleError((BaseError) error);
                }
            }

            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                getView().handleError(new BaseError(ErrorState.BUSINESS_ERROR,"身份证已被注册"));
            }
        });
    }
}
