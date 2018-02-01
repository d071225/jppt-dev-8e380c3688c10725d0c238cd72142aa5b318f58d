package com.hletong.hyc.contract;

import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.validate.AddressInfo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by ddq on 2017/3/14.
 * 货方-地址管理
 */

public interface AddressContract {
    interface View extends IBaseView, ICommitSuccessView, ITransactionView {
        void showTitle(String title);


        void showAddress(String address, String detail);

        void showContact(String contact, String tel);

        void showDeep(String deep);
    }

    interface Presenter extends IBasePresenter {
        void submit(AddressInfo addressInfo);

        void addressChanged(Address address);
    }

    interface View2 extends IBaseView, ICommitSuccessView {
        void refreshList();
    }

    interface Presenter2 extends IBasePresenter {
        void delete(String contactUuid);
    }
}
