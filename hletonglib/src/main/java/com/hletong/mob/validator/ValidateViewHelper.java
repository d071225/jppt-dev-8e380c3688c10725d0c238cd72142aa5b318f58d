package com.hletong.mob.validator;

import android.content.res.TypedArray;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.hletong.mob.R;
import com.hletong.mob.validator.result.ResultType;
import com.hletong.mob.validator.view.InputType;
import com.hletong.mob.validator.view.ValidateView;

import java.util.Locale;

/**
 * 表单View解析代理类
 * Created by chengxin on 2017/7/29.
 */

public class ValidateViewHelper {
    private static final int ORDER_DEFAULT = 0;
    private static final int FROM_DEFAULT = 1;
    private ValidateView validateView;
    private String name;
    private String key;
    private int order;
    private String emptyMsg;
    private int inputType;
    private int from;
    private int to;
    private String outRangMsg;
    private int resultType;
    //是否需要验证
    private boolean validate;

    public ValidateViewHelper(ValidateView validateView) {
        this.validateView = validateView;
        this.validate = true;
    }

    @CallSuper
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedValue = ((View) validateView).getContext().obtainStyledAttributes(attrs, R.styleable.ValidateView, defStyleAttr, 0);
        key = typedValue.getString(R.styleable.ValidateView_val_key);
        if (key == null) {
            key = String.format(Locale.getDefault(), "%s@%d", validateView.getClass().getSimpleName(), hashCode());
        }
        name = typedValue.getString(R.styleable.ValidateView_val_name);
        if (name == null) {
            name = key;
        }
        order = typedValue.getInteger(R.styleable.ValidateView_val_order, ORDER_DEFAULT);

        emptyMsg = typedValue.getString(R.styleable.ValidateView_val_emptyMsg);
        if (emptyMsg == null) {
            emptyMsg = String.format(Locale.getDefault(), "%s不能为空", name);
        }
        inputType = typedValue.getInteger(R.styleable.ValidateView_val_inputType, InputType.NONE);
        resultType = typedValue.getInteger(R.styleable.ValidateView_val_resultType, ResultType.TEXT);
        from = typedValue.getInteger(R.styleable.ValidateView_val_from, FROM_DEFAULT);
        to = typedValue.getInteger(R.styleable.ValidateView_val_to, Integer.MAX_VALUE);
        if (to < from) {
            throw new IllegalStateException("val_to must be not less than than val_from");
        }
        outRangMsg = typedValue.getString(R.styleable.ValidateView_val_outRangeMsg);
        if (outRangMsg == null) {
            if (to != Integer.MAX_VALUE) {
                if (to > from) {
                    outRangMsg = String.format(Locale.getDefault(), "%s长度必须>=%d位 且 <=%d位 ", name(), from, to);
                } else {
                    outRangMsg = String.format(Locale.getDefault(), "%s长度必须为%d位 ", name, from);
                }
            } else {
                outRangMsg = String.format(Locale.getDefault(), "%s长度必须>=%d位", name, from);
            }
        }
        typedValue.recycle();
    }

    public int order() {
        return order;
    }

    @NonNull
    public String name() {
        return name;
    }

    @NonNull
    public String key() {
        return key;
    }

    @NonNull
    public String emptyMsg() {
        return emptyMsg;
    }

    @InputType
    public int inputType() {
        return inputType;
    }

    @ResultType
    public int resultType() {
        return resultType;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }


    /**
     * @return 默认为true
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * 设置是否需要验证
     *
     * @param validate true 代表需要验证，反之false
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @NonNull
    public String outRangMsg() {
        return outRangMsg;
    }

    public boolean isOutRange(CharSequence sequence) {
        int length = TextUtils.isEmpty(sequence) ? 0 : sequence.toString().length();
        return length < from() || length > to();
    }

}
