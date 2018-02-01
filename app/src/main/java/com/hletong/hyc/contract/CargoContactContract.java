package com.hletong.hyc.contract;

import com.hletong.hyc.model.validate.ContactInfo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by ddq on 2017/3/20.
 */

public interface CargoContactContract {
    interface View extends IBaseView, ICommitSuccessView, ITransactionView {
        void showTitle(String title);

        void showName(String name);

        void showTaxCode(String taxCode);
    }

    interface Presenter extends IBasePresenter {
        void submit(ContactInfo contactInfo);
    }

    interface View2 extends IBaseView, ICommitSuccessView {
        void refresh();
    }

    interface Presenter2 extends IBasePresenter{
        void delete(String id);
    }
}
