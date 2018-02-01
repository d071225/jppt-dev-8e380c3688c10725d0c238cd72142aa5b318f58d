package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

import java.io.UnsupportedEncodingException;

/**
 * Created by dongdaqing on 2017/6/8.
 */

public interface CargoForecastPasswordContract {
    interface View extends IBaseView, ITransactionView, ICommitSuccessView {
        void showHint(String text);
    }

    interface Presenter extends IBasePresenter {
        void submit(String dp, String dpc, String rp, String rpc) throws UnsupportedEncodingException;
    }
}
