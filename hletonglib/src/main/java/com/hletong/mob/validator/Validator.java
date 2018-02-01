package com.hletong.mob.validator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.mob.validator.result.ResultBasic;
import com.hletong.mob.validator.view.ValidateView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证表单核心类
 * Created by chengxin on 2017/7/20.
 */
public class Validator {
    private static final String TAG = Validator.class.getSimpleName();

    public static final String EMPTY = "";
    //InputView缓存
    private final List<ValidateView> validateViews;
    private final Context context;
    /**
     * 当验证出错之后是否继续验证其他属性
     */
    private boolean validateAll;

    private OnValidatorListener onValidatorListener;

    @NonNull
    @UiThread
    public static Validator create(@NonNull Activity target) {
        return new Validator(target.getWindow().getDecorView());
    }

    @NonNull
    @UiThread
    public static Validator create(@NonNull View rootView) {
        return new Validator(rootView);
    }

    @NonNull
    @UiThread
    public static Validator create(@NonNull Dialog target) {
        return new Validator(target.getWindow().getDecorView());
    }

    private Validator(View rootView) {
        this.validateAll = false;
        this.context = rootView.getContext().getApplicationContext();
        this.validateViews = getValidateViews(rootView);
        if (validateViews.isEmpty()) {
            throw new IllegalStateException("have you define FormView in rootView");
        }
        sort(validateViews);
    }

    public ValidateView findViewByKey(String key) {
        for (ValidateView view : validateViews) {
            if (view.key().equals(key)) {
                return view;
            }
        }
        Log.e(TAG, "not find View with key:" + key);
        return null;
    }

    public ValidateView findViewByKey(@StringRes int keyResId) {
        return findViewByKey(context.getString(keyResId));
    }

    public String findTextByKey(String key) {
        ResultBasic resultBasic = findBasicByKey(key);
        if (resultBasic != null) {
            return resultBasic.resultText();
        }
        return null;
    }

    public String findTextByKey(@StringRes int keyResId) {
        return findTextByKey(context.getString(keyResId));
    }

    public ResultBasic findBasicByKey(String key) {
        ValidateView validateView = findViewByKey(key);
        if (validateView != null) {
            return validateView.getResultBasic();
        }
        return null;
    }

    public ResultBasic findBasicByKey(@StringRes int keyResId) {
        return findBasicByKey(context.getString(keyResId));
    }

    public void setOnValidatorListener(OnValidatorListener onValidatorListener) {
        this.onValidatorListener = onValidatorListener;
    }

    public void setValidateAll(boolean validateAll) {
        this.validateAll = validateAll;
    }

    public void bind(String key, @Nullable ResultBasic resultBasic) {
        ValidateView validateView = findViewByKey(key);
        if (validateView != null) {
            validateView.setResultBasic(resultBasic);
        }
    }

    public void bind(@StringRes int keyResId, @Nullable ResultBasic resultBasic) {
        bind(context.getString(keyResId), resultBasic);
    }

    /**
     * 绑定简单的数据类型 ,displayText==resultText
     */
    public void bind(String key, String resultText) {
        bind(key, resultText, resultText);
    }

    public void bind(@StringRes int keyResId, String resultText) {
        bind(context.getString(keyResId), resultText);
    }

    /**
     * 绑定简单的数据类型
     *
     * @param key         key
     * @param resultText  保存的resultText
     * @param displayText 展示的displayText
     */
    public void bind(String key, String resultText, String displayText) {
        ValidateView validateView = findViewByKey(key);
        if (validateView != null) {
            int resultType = validateView.resultType();
            if (ResultBasic.isSimpleType(resultType)) {
                validateView.setResultBasic(ResultBasic.textOf(resultType, resultText, displayText));
            } else {
                throw new IllegalStateException("resultType must be TEXT , NUMBER or DECIMAL");
            }
        }
    }

    public void bind(@StringRes int keyResId, String resultText, String displayText) {
        bind(context.getString(keyResId), resultText, displayText);
    }

    /**
     * 将Map上的数据绑定到InputView上去
     *
     * @param
     */
    public void bindMap(@Nullable Map<String, ResultBasic> map) {
        if (map != null) {
            for (ValidateView validateView : validateViews) {
                if (map.containsKey(validateView.key())) {
                    ResultBasic resultBasic = map.get(validateView.key());
                    validateView.setResultBasic(resultBasic);
                }
            }
        } else {
            Log.e(TAG, "map==null");
        }
    }

    public boolean validate() {
        if (onValidatorListener == null)
            throw new IllegalStateException("onValidatorListener==null");
        boolean isError = false;
        for (ValidateView validateView : validateViews) {
            if (!validateView.isValidate()) {
                continue;
            }
            if (validateView.isEmpty()) {
                isError = true;
                onValidatorListener.onResultEmpty(validateView, this);
            } else {
                if (validateView.isOutRange()) {
                    isError = true;
                    onValidatorListener.onResultOutRange(validateView, this);
                } else {
                    if (!onValidatorListener.onResultFormat(validateView, this)) {
                        isError = true;
                    }
                }
            }
            if (isError && !validateAll) {
                return false;
            }
        }
        if (!isError) {
            onFormSuccess();
            return true;
        }
        return false;
    }

    private void onFormSuccess() {
        Map<String, ResultBasic> map = new LinkedHashMap<>();
        for (ValidateView validateView : validateViews) {
            if (!validateView.isValidate()) {
                continue;
            }
            String key = validateView.key();
            if (map.containsKey(key)) {
                throw new IllegalStateException("have already containsKey key " + key);
            }
            ResultBasic resultBasic = validateView.getResultBasic();
            map.put(key, resultBasic);
        }
        onValidatorListener.onFormSuccess(map, this);
    }

    private void sort(List<ValidateView> formFields) {
        Collections.sort(formFields, new Comparator<ValidateView>() {
            @Override
            public int compare(ValidateView o1, ValidateView o2) {
                int x = o1.order();
                int y = o2.order();
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            }
        });
    }

    /**
     * 递归获取所有的InputView
     *
     * @param rootView 根View
     * @return 返回所有的InputView
     */
    private List<ValidateView> getValidateViews(View rootView) {
        List<ValidateView> validateViews = new ArrayList<>();
        if (rootView instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) rootView;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View child = vp.getChildAt(i);
                if (child instanceof ValidateView) {
                    validateViews.add((ValidateView) child);
                } else {
                    validateViews.addAll(getValidateViews(child));
                }
            }
        }
        return validateViews;
    }
}
