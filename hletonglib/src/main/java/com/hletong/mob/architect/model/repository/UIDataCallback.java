package com.hletong.mob.architect.model.repository;

import android.support.annotation.NonNull;

import com.hletong.mob.architect.error.BaseError;
import com.xcheng.view.EasyView;

/**
 * Created by ddq on 2016/12/30.
 * 负责将子线程数据发送到主线程处理
 */
public final class UIDataCallback<R> extends DataCallback<R> {
    private DataCallback<R> mCallback;

    public UIDataCallback(@NonNull DataCallback<R> callback) {
        super(callback.getProgress());
        this.mCallback = callback;
        EasyView.runOnUiThread(new Start());
    }

    @Override
    public void onSuccess(@NonNull R response) {
        EasyView.runOnUiThread(new Success(response));
    }

    @Override
    public void onError(@NonNull BaseError mError) {
        EasyView.runOnUiThread(new Failed(mError));
    }

    private class Start implements Runnable {
        @Override
        public void run() {
            if (getProgress() != null/*in case*/ && !isCanceled()) {
                getProgress().showLoading();
                mCallback.onStart();//调用自己的onStart方法表示请求开始
            }
        }
    }

    private class Success implements Runnable {
        private R mResponse;

        Success(R response) {
            this.mResponse = response;
        }

        @Override
        public void run() {
            if (!isCanceled()) {
                if (getProgress() != null)
                    getProgress().hideLoading();
                //请求成功
                mCallback.onSuccess(mResponse);
                //请求完成
                mCallback.onFinish();
            }
        }
    }

    private class Failed implements Runnable {
        private BaseError mError;

        Failed(BaseError error) {
            this.mError = error;
        }

        @Override
        public void run() {
            if (!isCanceled()) {
                if (getProgress() != null)
                    getProgress().hideLoading();
                //请求失败
                mCallback.onError(mError);
                //请求完成
                mCallback.onFinish();
            }
        }
    }
}
