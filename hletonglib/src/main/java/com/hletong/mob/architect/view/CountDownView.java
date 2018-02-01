package com.hletong.mob.architect.view;

/**
 * Created by dongdaqing on 2017/8/2.
 */

public interface CountDownView extends IBaseView {
    /**
     * 开始倒计时操作
     *
     * @param seconds 倒计时多少秒
     */
    void startCounting(int seconds);

    /**
     * 开始倒计时操作
     * @param milliseconds 倒计时多少毫秒
     */
    void startCountingInMillis(long milliseconds);

    /**
     * 停止倒计时
     */
    void stopCounting();
}
