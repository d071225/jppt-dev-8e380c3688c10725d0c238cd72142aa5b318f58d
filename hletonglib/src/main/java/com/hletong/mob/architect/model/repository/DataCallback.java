package com.hletong.mob.architect.model.repository;

import android.support.annotation.Nullable;

import com.hletong.mob.architect.view.IProgress;

import okhttp3.Call;

/**
 * Created by ddq on 2016/12/30.
 * 数据回调接口
 */

public abstract class DataCallback<R> implements Lifecycle<R> {
    private static final String REQUEST_START = "request_start";
    private static final String REQUEST_FINISH = "request_finish";
    //请求动画
    private IProgress mProgress;
    /**
     * 判断请求是否取消需要
     **/
    private Call mCall;

    /**
     * 没有动画
     **/
    public DataCallback() {
    }

    public DataCallback(IProgress progress) {
        this.mProgress = progress;
    }

    /**
     * 在Http请求回调函数的时候设置
     **/
    public void setCall(Call call) {
        this.mCall = call;
    }

    /**
     * 在回调到UI层的时候判断
     **/
    public boolean isCanceled() {
        return mCall != null && mCall.isCanceled();
    }

    @Nullable
    public IProgress getProgress() {
        return mProgress;
    }

    @Override
    public void onStart() {
       // LocalBroadcastManager.getInstance(null).sendBroadcast(new Intent(REQUEST_START));
    }

    @Override
    public void onFinish() {
       // LocalBroadcastManager.getInstance(null).sendBroadcast(new Intent(REQUEST_FINISH));
    }
}
