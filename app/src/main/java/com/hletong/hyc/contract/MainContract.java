package com.hletong.hyc.contract;

import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by dongdaqing on 2017/7/31.
 */

public interface MainContract {
    interface View extends UpgradeContract.View, ITransactionView {
        void logout();
    }

    interface Presenter extends UpgradeContract.Presenter {
        void checkAuthority();
    }
}
