package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.IProgress;

/**
 * Created by ddq on 2017/3/31.
 */

public interface CargoRefusedReasonContract {
    interface View extends IBaseView{
        void showReason(String message);
    }
    interface Presenter extends IBasePresenter{

    }
}
