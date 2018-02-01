package com.hletong.mob.architect.view;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.hletong.mob.architect.error.BaseError;

/**
 * Created by ddq on 2016/12/6.
 * mvp中V的基础
 */
public interface IBaseView extends IProgress {
    /**
     * UI层处理所有的错误
     *
     * @param error 错误信息
     */
    @UiThread
    void handleError(@NonNull BaseError error);
}
