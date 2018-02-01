package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;

/**
 * 设置的协议模块，包含所有设置相关的接口
 * Created by cc on 2017/1/5.
 */
public interface SettingContract {
    interface IExitView extends IBaseView, ICommitSuccessView {
    }

    /***
     * 退出处理逻辑
     ***/
    interface IExitPresenter extends IBasePresenter {
        void exit();
    }

    /**
     * 退会申请
     ***/
    interface IExitMemberView extends IBaseView {
        void exitMemberSuccess();

        String getExitReason();

        /**
         * 获取用户类型
         */
        String getUserType();
    }

    /***
     * 退回请求处理
     ***/
    interface IExitMemberPresenter extends IBasePresenter {
        void exitMember();

        boolean validate();
    }

    /**
     * 退回申请
     ***/
    interface IModifyPsdView extends IBaseView, ICommitSuccessView {

    }

    /***
     * 退回请求处理
     ***/
    interface IIModifyPsdPresenter extends IBasePresenter {
        void changePassword(String oldPsw, String newPsw, String confirmPsw);
    }
}
