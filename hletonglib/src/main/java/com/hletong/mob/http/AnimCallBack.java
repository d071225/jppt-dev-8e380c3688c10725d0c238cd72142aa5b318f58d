package com.hletong.mob.http;

import com.hletong.mob.architect.view.IProgress;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.view.util.ToastLess;

/**
 * Created by chengxin on 2017/7/6.
 */

public abstract class AnimCallBack<T> extends UICallback<T> {
    private IProgress progress;

    public AnimCallBack(IProgress progress) {
        this.progress = progress;
    }

    @Override
    public void onError(OkCall<T> okCall, EasyError error) {
        ToastLess.showToast(error.getMessage());
    }

    @Override
    public void onStart(OkCall<T> okCall) {
        if (progress != null)
            progress.showLoading();
    }

    @Override
    public void onFinish(OkCall<T> okCall) {
        if (progress != null)
            progress.hideLoading();
    }
}
