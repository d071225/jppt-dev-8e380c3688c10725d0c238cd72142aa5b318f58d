package com.hletong.mob.validator;

import com.hletong.mob.validator.result.ResultBasic;
import com.hletong.mob.validator.view.ValidateView;

import java.util.Map;

/**
 * Created by chengxin on 2017/7/20.
 */

public interface OnValidatorListener {

    void onResultEmpty(ValidateView view, Validator validator);

    void onResultOutRange(ValidateView view, Validator validator);

    boolean onResultFormat(ValidateView view, Validator validator);

    void onFormSuccess(Map<String, ResultBasic> formMap, Validator validator);
}
