package com.hletong.mob.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({LoginAction.NORMAL, LoginAction.REGISTER, LoginAction.SESSION_TIMEOUT,
        LoginAction.NO_LOGIN, LoginAction.RE_LOGIN})
@Retention(RetentionPolicy.SOURCE)
public @interface LoginAction {
    String KEY_LOGIN_ACTION = "_loginAction";
    //正常状态
    int NORMAL = 0;
    //从注册页跳转过来
    int REGISTER = 1;
    //未登录操作要求登录
    int NO_LOGIN = 2;
    //会话过期
    int SESSION_TIMEOUT = 3;
    //重新登录，如退出账号，修改密码等
    int RE_LOGIN = 4;
}