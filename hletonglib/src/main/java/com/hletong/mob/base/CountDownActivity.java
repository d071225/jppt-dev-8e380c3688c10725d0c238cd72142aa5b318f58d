package com.hletong.mob.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.CountDownView;

/**
 * Created by dongdaqing on 2017/8/2.
 * 提供倒计时功能
 */

public abstract class CountDownActivity<P extends IBasePresenter> extends MvpActivity<P> implements CountDownView, CountDownHelper.CountDownListener {
    private CountDownHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new CountDownHelper(this, getTimeShift());
    }

    /**
     * 获取时间偏移
     *
     * @return
     */
    protected long getTimeShift() {
        return 0;
    }

    @Override
    protected void onPause() {
        mHelper.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mHelper.onResume();
        super.onResume();
    }

    @Override
    public void startCounting(int seconds) {
        mHelper.startCountingInSeconds(seconds);
    }

    @Override
    public void startCountingInMillis(long milliseconds) {
        mHelper.startCounting(milliseconds);
    }

    @Override
    public void stopCounting() {
        mHelper.stopCounting();
    }

    @Override
    public void onTicking(long hour, long minute, long second) {
        //倒计时中
    }

    @Override
    public void countFinished() {
        //倒计时结束
    }
}
