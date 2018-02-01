package com.hletong.mob.validator.view;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by chengxin on 2017/7/25.
 */
@IntDef({InputType.NONE, InputType.NUMBER, InputType.DECIMAL, InputType.CELLPHONE, InputType.TELEPHONE})
@Retention(RetentionPolicy.SOURCE)
public @interface InputType {
    int NONE = 0;
    int NUMBER = 1;
    int DECIMAL = 2;
    //手机号码
    int CELLPHONE = 3;
    //固座号码
    int TELEPHONE = 4;

}
