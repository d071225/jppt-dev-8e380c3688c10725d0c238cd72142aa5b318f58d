package com.hletong.mob.architect.model.repository;

import com.hletong.mob.architect.error.BaseError;

/**
 * Created by dongdaqing on 2017/5/5.
 * 请求生命周期
 */

interface Lifecycle<T> {
    /**
     * 请求开始
     */
    void onStart();

    /**
     * 请求成功
     * @param response 请求的响应
     */
    void onSuccess(T response);

    /**
     * 请求出错
     * @param error 错误内容
     */
    void onError(BaseError error);

    /**
     * 请求结束
     */
    void onFinish();
}
