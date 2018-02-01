package com.hletong.mob.validator;

import com.hletong.mob.validator.result.ResultBasic;
import com.hletong.mob.validator.view.ValidateView;
import com.xcheng.view.util.ToastLess;

import java.util.Map;

/**
 * Created by chengxin on 2017/7/20.
 */

public class SimpleValidatorListener implements OnValidatorListener {

    @Override
    public void onResultEmpty(ValidateView view, Validator validator) {
        ToastLess.showToast(view.emptyMsg());

    }

    @Override
    public void onResultOutRange(ValidateView view, Validator validator) {
        ToastLess.showToast(view.outRangMsg());

    }

    @Override
    public boolean onResultFormat(ValidateView view, Validator validator) {
        return true;
    }

    @Override
    public void onFormSuccess(Map<String, ResultBasic> formMap, Validator validator) {

    }
}
