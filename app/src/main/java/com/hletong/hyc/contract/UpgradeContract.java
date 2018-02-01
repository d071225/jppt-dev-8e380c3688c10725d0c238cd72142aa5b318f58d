package com.hletong.hyc.contract;

import com.hletong.hyc.model.VersionInfo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.IDialogView;

/**
 * Created by ddq on 2017/1/12.
 */

public interface UpgradeContract {
    interface View extends IBaseView, IDialogView, ICommitSuccessView {
        void showUpgradeDialog(VersionInfo versionInfo);
    }

    interface Presenter extends IBasePresenter {
        void checkVersionInfo();
    }
}
