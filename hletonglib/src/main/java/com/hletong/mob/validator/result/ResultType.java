package com.hletong.mob.validator.result;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 需要保存的结果的格式
 * Created by chengxin on 2017/7/25.
 */
@IntDef({ResultType.TEXT, ResultType.NUMBER, ResultType.DECIMAL, ResultType.MAP, ResultType.LIST})
@Retention(RetentionPolicy.SOURCE)
public @interface ResultType {
    /**
     * 任何类型
     */
    int TEXT = 0;
    /**
     * 整数型
     */
    int NUMBER = 1;
    /**
     * 小数,保存为double
     */
    int DECIMAL = 2;

    int MAP = 3;

    int LIST = 4;
}
