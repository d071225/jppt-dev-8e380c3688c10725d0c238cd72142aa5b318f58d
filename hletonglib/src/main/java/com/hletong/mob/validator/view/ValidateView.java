package com.hletong.mob.validator.view;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hletong.mob.validator.result.ResultBasic;
import com.hletong.mob.validator.result.ResultType;

/**
 * 需要被验证的View
 * Created by chengxin on 2017/7/20.
 */
public interface ValidateView {
    /**
     * 是否需要验证输入
     *
     * @return true 代表需要
     */
    boolean isValidate();

    /**设置是否需要验证
     * @param validate true 代表需要验证，反之false
     */
    void setValidate(boolean validate);
    /**
     * 表单验证的顺序
     *
     * @return 位置顺序
     */
    int order();

    /**
     * 表当内容的描述
     *
     * @return name
     */
    @NonNull
    String name();

    /**
     * 验证成功后存在存储在Map中的Key
     *
     * @return
     */
    @NonNull
    String key();

    /**
     * 输入为空的提示或者没有得到想要的result
     *
     * @return emptyMsg
     */
    @NonNull
    String emptyMsg();

    /**
     * 检测是否为空
     *
     * @return isEmpty
     */
    boolean isEmpty();

    /**
     * 输入类型
     *
     * @return inputType
     */
    @InputType
    int inputType();

    @ResultType
    int resultType();

    /**
     * 长度初始位置
     *
     * @return from
     */
    @IntRange(from = 0)
    int from();

    /**
     * 长度结束位置
     *
     * @return to
     */
    @IntRange(to = Integer.MAX_VALUE)
    int to();

    /**
     * 长度不满足要求的提示
     *
     * @return rangMsg
     */
    @NonNull
    String outRangMsg();

    /**
     * 长度是否超过范围
     *
     * @return outRange
     */
    boolean isOutRange();

    /**
     * 设置Result
     *
     * @param resultBasic
     */
    void setResultBasic(@Nullable ResultBasic resultBasic);

    /**
     * 获取Result
     *
     * @return resultDto
     */
    ResultBasic getResultBasic();
}
