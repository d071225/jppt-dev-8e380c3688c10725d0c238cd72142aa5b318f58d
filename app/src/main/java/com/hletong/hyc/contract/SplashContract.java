package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.CountDownView;

/**
 * Created by ddq on 2017/2/8.
 */

public interface SplashContract {
    interface View extends CountDownView {
        void showGuideView();
    }

    interface Presenter extends IBasePresenter {
        void guideFinished();
    }
}
