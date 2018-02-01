package com.hletong.mob.base;

import android.os.Handler;
import android.os.SystemClock;

import com.hletong.mob.util.SimpleDate;
import com.orhanobut.logger.Logger;

/**
 * Created by ddq on 2016/11/8.
 * 倒计时工具类：
 * 1.可以以某一个时刻为终点进行倒计时，这种情况时，要对终点所在的时间环境（时区和时刻）与本地的时间环境进行校准
 * 2.可以指定从现在开始多少秒之后倒计时结束，这种情况下{@link #timeShift}为0
 * 在Activity或者Fragment里面使用的时候要在onResume里面调用{@link #onResume()}和onPause方法里面调用{@link #onPause()}，如果Activity或者Fragment里面
 * 存在了Handler对象，可以通过构造函数传入共享使用
 * 在View里面使用的时候可以传入View中的Handler，同时在onAttachToWindow方法里面调用{@link #onResume()}，在onDetachFromWindow里面
 * 调用{@link #onPause()}方法
 */
public class CountDownHelper {
    private Handler mHandler;//可以从外部对象传入
    private CountDownListener cd;
    private Counting mCounting;

    private long hour;//剩余小时
    private long minute;//剩余分
    private long second;//剩余秒
    private long timeShift;//时间偏移，远程服务端的时间 减去 本地同一时刻的时间，顺序不能反了
    private long pausedTime;//暂停的时刻
    private boolean everPaused = false;

    public CountDownHelper(Handler handler, CountDownListener cd, long timeShift) {
        if (handler == null)
            this.mHandler = new Handler();
        else
            this.mHandler = handler;
        this.cd = cd;
        this.timeShift = timeShift;
        mCounting = new Counting();
    }

    public CountDownHelper(CountDownListener cd, long timeShift) {
        this(null, cd, timeShift);
    }

    public CountDownHelper(CountDownListener cd) {
        this(cd, 0);
    }

    //计算剩余时间
    private void calculateTimeLeft(long deadline) {
        long delta = deadline - (System.currentTimeMillis() + timeShift);
        if (delta < 0) {
            hour = 0;
            minute = 0;
            second = 0;
        } else {
            hour = delta / SimpleDate.HOUR_MILLISECONDS;
            delta -= hour * SimpleDate.HOUR_MILLISECONDS;
            minute = delta / SimpleDate.MINUTE_MILLISECONDS;
            delta -= minute * SimpleDate.MINUTE_MILLISECONDS;
            second = delta / SimpleDate.SECOND_MILLISECONDS;
        }
    }

    public void startCounting(long deadline) {
        everPaused = false;
        calculateTimeLeft(deadline);
        mHandler.removeCallbacks(mCounting);
        mCounting.reset();
        if (hour + second + minute > 0) {
            dispatchCounting();
            mHandler.postDelayed(mCounting, 1000);
        } else {
            stopCounting();
        }
    }

    public void startCountingInSeconds(int seconds) {
        startCounting(System.currentTimeMillis() + seconds * SimpleDate.SECOND_MILLISECONDS);
    }

    public void onPause() {
        pausedTime = SystemClock.elapsedRealtime();
        mHandler.removeCallbacks(mCounting);
        everPaused = true;
    }

    public void onResume() {
        if (!everPaused)
            return;
        long pausedInterval = SystemClock.elapsedRealtime() - pausedTime;
        everPaused = false;
        startCounting(System.currentTimeMillis() - pausedInterval + hour * SimpleDate.HOUR_MILLISECONDS + minute * SimpleDate.MINUTE_MILLISECONDS + second * SimpleDate.SECOND_MILLISECONDS);
    }

    public void stopCounting() {
        everPaused = false;
        mHandler.removeCallbacks(mCounting);

        hour = 0;
        minute = 0;
        second = 0;

        dispatchFinish();
    }

    private void dispatchCounting() {
        if (cd != null)
            cd.onTicking(hour, minute, second);
    }

    private void dispatchFinish() {
        if (cd != null)
            cd.countFinished();
    }

    public interface CountDownListener {
        void onTicking(long hour, long minute, long second);

        void countFinished();//倒计时结束
    }

    private class Counting implements Runnable {
        private long lastRecordTime = 0;//上一次执行到postDelay的时刻，用于测算消息调度时间，以便对消息延时做更准确的计算
        private long delay = 998;//根据实测结果，消息调度的平均时间应在2ms左右，故这里初始延时设定为998ms

        @Override
        public void run() {
            if (second <= 0) {
                second = 59;
                if (minute <= 0) {
                    --hour;
                    minute = 59;
                } else {
                    --minute;
                }
            } else {
                --second;
            }
            if (hour + second + minute <= 0) {
                stopCounting();
            } else {
                dispatchCounting();
                long current = SystemClock.elapsedRealtime();
                if (lastRecordTime == 0)
                    lastRecordTime = current  - delay;//第一次的时候，虚拟一个时间
                long timeInterval = current - lastRecordTime - delay;//上次消息调度时间
                delay = 1000 - timeInterval;//本次的消息延时使用上一次消息调度的时间作为依据
                Logger.d("timeInterval =>" + timeInterval + " ,delay => " + delay);
                lastRecordTime = current;
                mHandler.postDelayed(this, delay);
            }
        }

        void reset() {
            lastRecordTime = 0;
            delay = 998;
        }
    }

    public static String build(long hour, long minute, long second) {
        StringBuilder builder = new StringBuilder("秒");
        builder.insert(0, second);
        if (minute > 0) {
            builder.insert(0, "分");
            builder.insert(0, minute);
        }

        if (hour > 0) {
            builder.insert(0, "时");
            builder.insert(0, hour);
        }
        return builder.toString();
    }
}
