package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.EmptyContract;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by dongdaqing on 2017/8/7.
 */

public class EmptyPresenter extends BasePresenter<IBaseView> implements EmptyContract.Presenter {

    public EmptyPresenter(@NonNull IBaseView baseView) {
        super(baseView);
    }
}
