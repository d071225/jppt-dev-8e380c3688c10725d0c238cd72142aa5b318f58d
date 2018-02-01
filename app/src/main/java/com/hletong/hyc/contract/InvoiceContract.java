package com.hletong.hyc.contract;

import com.hletong.hyc.model.validate.InvoiceInfo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by ddq on 2017/1/5.
 */

public interface InvoiceContract {
    interface View extends IBaseView, ICommitSuccessView, ITransactionView, LocationView {
        void showTitle(String title);

        void showContact(String label, String contact);

        void showCargo(String doc, String cargoName, String carrierLabel, String carrier, String cargo, String address, String date);

        void showContract();

        void showWeight(String cargo, boolean input);

        void showNumber(String cargo, String unit, boolean fraction, boolean input);

        void showPasswordArea();

        void showSubmitButton();

        void makeCall(String contact, String tel);
    }

    interface Presenter extends IBasePresenter {
        void submit(InvoiceInfo invoiceInfo);

        void viewContract();

        void call();
    }
}
