package com.hletong.hyc.contract;

import com.hletong.hyc.model.Address;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.IProgress;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by ddq on 2017/2/10.
 */

public interface FavoriteRoutineContract {
    interface View extends IBaseView, ICommitSuccessView, ITransactionView {
    }

    interface Presenter extends IBasePresenter {
        void add(Address sa, Address ea);

        void delete(String routineUuid, IProgress progress);

        boolean isCargoParamValid();

        Address getStartAddress();

        Address getEndAddress();
    }
}
