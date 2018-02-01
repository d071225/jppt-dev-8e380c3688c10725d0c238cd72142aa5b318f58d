package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.model.validate.Validate;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.FinishOptions;

/**
 * Created by dongdaqing on 2017/6/17.
 */

public class InfoCompletePresenter extends BasePresenter<InfoCompleteContract.View> implements InfoCompleteContract.Presenter {

    public InfoCompletePresenter(@NonNull InfoCompleteContract.View view) {
        super(view);
    }

    @Override
    public void submit(Validate<CommonResult> params) {
        ItemRequestValue<CommonResult> requestValue = params.validate(getView());
        if (requestValue == null)
            return;

        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(CommonResult response) {
                getView().success(response.getErrorInfo());
                getView().finishWithOptions(FinishOptions.FORWARD_RESULT());
            }
        });
    }
}
