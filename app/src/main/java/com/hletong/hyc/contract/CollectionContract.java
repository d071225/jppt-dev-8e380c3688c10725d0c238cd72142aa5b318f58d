package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/27.
 */

public interface CollectionContract {
    interface View extends IBaseView, ICommitSuccessView {
        void updateItem(String carrierMemberCode);
    }

    interface Presenter extends IBasePresenter {
        void inform(ParamsHelper ph);

        void delete(String tradeUuid);
    }
}
