package com.hletong.hyc.contract;

import android.support.v7.widget.Toolbar;

import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.validate.Transport;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.IDialogView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by ddq on 2017/1/4.
 */

public interface TransportContract {
    interface View extends IBaseView, ICommitSuccessView, IDialogView, ITransactionView {
        void showTitle(String title);

        void inflateMenu(int menu, Toolbar.OnMenuItemClickListener listener);

        void setAddressClickListener();

        void showAddress(Address startAddress, Address endAddress);

        void showAvailableTime(String from, String to);

        void showPlate(String plate);

        void showLoad(String load);

        void showSpare(String spare);

        void hideSpare();

        void prefetchTransporter();
    }

    interface Presenter extends IBasePresenter {
        void submit(Transport mTransport);

        void cancel();
    }
}
