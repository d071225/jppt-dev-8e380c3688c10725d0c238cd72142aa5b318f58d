package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by cc on 2017/1/4.
 */
public interface LoginContract {

    interface View extends IBaseView {
        void loginSuccess();
    }

    interface Presenter extends IBasePresenter {
        
        void login(String loginName, String password);
    }
}