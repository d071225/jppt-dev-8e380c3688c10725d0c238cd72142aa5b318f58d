package com.hletong.mob.net;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * session过期消息
 * Created by cx on 2016/11/15.
 */
public class EventSessionTimeout {
    private String message;
    /**
     * 记录session过期的请求
     * 仿照LocalBroadcastManager，PrintHelperKitkat部分代码锁定
     **/
    private static final List<RetryCall> sRetryCalls = new ArrayList<>(0);

    public static EventSessionTimeout getInstance(String message, Call call, Callback callback) {
        synchronized (sRetryCalls) {
            if (call != null && callback != null && !isContain(call)) {
                sRetryCalls.add(new RetryCall(call, callback));
            }
        }
        return new EventSessionTimeout(message);
    }

    private EventSessionTimeout(String message) {
        this.message = message;
    }

    private static boolean isContain(Call call) {
        HttpUrl url = call.request().url();
        for (RetryCall temp : sRetryCalls) {
            if (temp.call.request().url().equals(url)) {
                return true;
            }
        }
        return false;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /***
     * 重新发起之前session过期的请求
     **/
    public static void retryRequest() {
        synchronized (sRetryCalls) {
            if (sRetryCalls.size() == 0)
                return;
            for (int index = 0; index < sRetryCalls.size(); index++) {
                RetryCall retryCall = sRetryCalls.get(index);
                if (!retryCall.call.isCanceled()) {
                    Request request = retryCall.call.request();
                    OkHttpUtil.getOkHttpClient().newCall(request).enqueue(retryCall.callback);
                }
            }
            cancelAll();
        }
    }

    /***
     * 重新登录失败之后继续处理错误
     **/
    public static void onSessionError() {
        synchronized (sRetryCalls) {
            if (sRetryCalls.size() == 0)
                return;
            for (int index = 0; index < sRetryCalls.size(); index++) {
                RetryCall retryCall = sRetryCalls.get(index);
                if (!retryCall.call.isCanceled() && retryCall.callback instanceof ISessionTimeoutError) {
                    ISessionTimeoutError sessionTimeoutError = (ISessionTimeoutError) retryCall.callback;
                    sessionTimeoutError.onSessionError();
                }
            }
        }
    }

    /**
     * 取消超时的请求
     **/
    public static void cancel(Object tag) {
        synchronized (sRetryCalls) {
            if (sRetryCalls.size() == 0)
                return;
            for (int index = 0; index < sRetryCalls.size(); index++) {
                Call call = sRetryCalls.get(index).call;
                if (tag.equals(call.request().tag())) {
                    sRetryCalls.remove(index);
                    index--;
                }
            }
        }

    }

    /**
     * 取消所有请求
     */
    public static void cancelAll() {
        synchronized (sRetryCalls) {
            sRetryCalls.clear();
        }
    }

    private static class RetryCall {
        private Call call;
        private Callback callback;

        public RetryCall(Call call, Callback callback) {
            this.call = call;
            this.callback = callback;
        }
    }
}
