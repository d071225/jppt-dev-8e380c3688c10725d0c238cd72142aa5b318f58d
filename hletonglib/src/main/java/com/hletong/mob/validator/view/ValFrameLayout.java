package com.hletong.mob.validator.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hletong.mob.validator.ValidateViewHelper;
import com.hletong.mob.validator.Validator;
import com.hletong.mob.validator.result.ResultBasic;

/**
 * 结果表单
 * Created by chengxin on 2017/8/2.
 */
public class ValFrameLayout extends FrameLayout implements ValidateView {
    private ResultBasic resultBasic;
    private TextView textView;

    private ValidateViewHelper mValidateViewHelper;

    public ValFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ValFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mValidateViewHelper = new ValidateViewHelper(this);
        mValidateViewHelper.loadFromAttributes(attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = findViewWithTag("displayText");
        if (view instanceof TextView) {
            textView = (TextView) view;
        } else {
            Log.e(getClass().getSimpleName(), "can not find View with tag named displayText");
        }
    }

    @Override
    public int order() {
        return mValidateViewHelper.order();
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

    @Override
    public boolean isValidate() {
        return mValidateViewHelper.isValidate();
    }

    @Override
    public void setValidate(boolean validate) {
        mValidateViewHelper.setValidate(validate);
    }

    @Override
    public boolean isEmpty() {
        return from() > 0 && resultBasic == null;
    }

    @Override
    public int inputType() {
        return InputType.NONE;
    }

    /**
     * 返回结果类型,默认为Map
     *
     * @return resultType
     */
    @Override
    public int resultType() {
        if (resultBasic != null) {
            resultBasic.resultType();
        }
        return mValidateViewHelper.resultType();
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
    public boolean isOutRange() {
        return false;
    }

    @NonNull
    @Override
    public String outRangMsg() {
        return Validator.EMPTY;
    }

    @Override
    public void setResultBasic(@Nullable ResultBasic resultBasic) {
        if (textView != null) {
            textView.setText(resultBasic != null ? resultBasic.getDisplayText() : Validator.EMPTY);
        }
        this.resultBasic = resultBasic;
    }

    @Override
    public ResultBasic getResultBasic() {
        return resultBasic;
    }
}
