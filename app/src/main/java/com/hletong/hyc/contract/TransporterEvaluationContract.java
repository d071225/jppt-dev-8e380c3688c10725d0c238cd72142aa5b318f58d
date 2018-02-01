package com.hletong.hyc.contract;

import android.content.DialogInterface;

import com.hletong.hyc.model.TransporterEvaluation;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by dongdaqing on 2017/5/15.
 */

public interface TransporterEvaluationContract {
    interface View extends IBaseView, ICommitSuccessView, ITransactionView {
        void initSelfTradeView();

        void initNormalView();

        void initViewWithData(TransporterEvaluation te, boolean selfTrade);

        void showSubmitAlert(DialogInterface.OnClickListener listener);
    }

    interface Presenter extends IBasePresenter {
        void submit(int timeliness, int damage, int attitude, String remark);
    }
}
