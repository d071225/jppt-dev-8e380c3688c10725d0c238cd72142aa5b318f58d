package com.hletong.hyc.contract;

import com.hletong.hyc.model.validate.Validate;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by dongdaqing on 2017/6/17.
 */

public interface InfoCompleteContract {
    interface View extends IBaseView, ICommitSuccessView, ITransactionView {

    }

    interface Presenter extends IBasePresenter{
        void submit(Validate<CommonResult> params);
    }
}
