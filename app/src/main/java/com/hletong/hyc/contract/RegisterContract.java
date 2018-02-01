package com.hletong.hyc.contract;

import android.support.annotation.NonNull;

import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by cc on 2017/1/4.
 */
public interface RegisterContract {
    interface IBaseInfoView extends IBaseView {
        /**
         * 提交成功
         **/
        void onSuccess(RegisterInfo registerInfo,String message);

        @NonNull
        RegisterInfo getRegisterInfo();
    }

    interface IBaseInfoPresenter extends IBasePresenter{
        /**
         * isCompany 区分个人和公司
         * 提交基本信息
         **/
        void submitCompanyInfo();

        /**
         * 检查车牌号
         */
        void submitPersonInfo();


    }

    /**
     * 获取验证码
     ***/
    interface IRegisterView extends IBaseInfoView {
        /***
         * 获取到验证码
         ***/
        void onVerifyCode(String message);

    }

    interface IRegisterPresenter extends IBasePresenter{

        void register();

        /**
         * 获取验证码
         ***/
        void getVerifyCode();
    }
}
