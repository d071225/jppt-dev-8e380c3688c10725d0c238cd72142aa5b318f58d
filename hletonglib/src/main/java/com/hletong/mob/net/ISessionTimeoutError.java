package com.hletong.mob.net;

/**
 * Created by chengxin on 2017/4/6.
 */
public interface ISessionTimeoutError {

    /**
     * 静默登录失败会后处理被拦截的session过期错误
     */
    void onSessionError();
}
