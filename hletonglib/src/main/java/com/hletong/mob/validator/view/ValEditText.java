package com.hletong.mob.validator.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.hletong.mob.validator.ValidateViewHelper;
import com.hletong.mob.validator.Validator;
import com.hletong.mob.validator.result.ResultBasic;

/**
 * Created by chengxin on 2017/7/28.
 */
public class ValEditText extends android.support.v7.widget.AppCompatEditText implements ValidateView {
    private ValidateViewHelper mValidateViewHelper;

    public ValEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mValidateViewHelper = new ValidateViewHelper(this);
        mValidateViewHelper.loadFromAttributes(attrs, 0);
    }

    public ValEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mValidateViewHelper = new ValidateViewHelper(this);
        mValidateViewHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @Override
    public int order() {
        return mValidateViewHelper.order();
    }

    @Override
    public int inputType() {
        return mValidateViewHelper.inputType();
    }

    @Override
    public int resultType() {
        return mValidateViewHelper.resultType();
    }

    @NonNull
    @Override
    public String name() {
        return mValidateViewHelper.name();
    }

    @NonNull
    @Override
    public String key() {
        return mValidateViewHelper.key();
    }

    @NonNull
    @Override
    public String emptyMsg() {
        return mValidateViewHelper.emptyMsg();
    }

    @NonNull
    @Override
    public String outRangMsg() {
        return mValidateViewHelper.outRangMsg();
    }

    @Override
    public int from() {
        return mValidateViewHelper.from();
    }

    @Override
    public int to() {
        return mValidateViewHelper.to();
    }

    @Override
    public boolean isEmpty() {
        return from() > 0 && TextUtils.isEmpty(getText());
    }

    @Override
    public boolean isOutRange() {
        return mValidateViewHelper.isOutRange(getText());
    }


    @Override
    public void setResultBasic(@Nullable ResultBasic resultBasic) {
        setText(resultBasic != null ? resultBasic.getDisplayText() : Validator.EMPTY);
    }

    @Override
    public ResultBasic getResultBasic() {
        return ResultBasic.textOf(resultType(), getText().toString());
    }

    @Override
    public boolean isValidate() {
        return mValidateViewHelper.isValidate();
    }

    @Override
    public void setValidate(boolean validate) {
        mValidateViewHelper.setValidate(validate);
    }
}
