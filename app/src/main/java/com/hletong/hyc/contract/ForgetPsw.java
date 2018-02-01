package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by ddq on 2017/3/14.
 * 货方-地址管理
 */

public interface ForgetPsw {
    interface View extends IBaseView, ITransactionView {
        void httpVerifyCode();

        void httpResetSuccess();
    }

    interface Presenter extends IBasePresenter {
        void getVerifyCode(String memberName, String phoneNumber);

        void resetPsd(String memberName, String phoneNumber, String verifyCode);
    }
}
