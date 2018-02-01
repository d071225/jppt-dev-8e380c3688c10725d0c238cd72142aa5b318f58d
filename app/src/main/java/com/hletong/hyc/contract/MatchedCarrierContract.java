package com.hletong.hyc.contract;

import android.support.v4.app.FragmentManager;

import com.hletong.hyc.model.MatchedCarrier;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by ddq on 2017/3/27.
 */

public interface MatchedCarrierContract {
    interface View extends IBaseView{
        void initForecast(MatchedCarrier fti);
        void initMatched(MatchedCarrier fti);
    }

    interface Presenter extends IBasePresenter{
        void call(FragmentManager manager);
    }
}
