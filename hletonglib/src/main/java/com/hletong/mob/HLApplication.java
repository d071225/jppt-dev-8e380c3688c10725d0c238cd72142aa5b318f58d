package com.hletong.mob;

import android.app.Application;
import android.support.annotation.CallSuper;

import com.hletong.mob.net.EventSessionTimeout;
import com.hletong.mob.net.OkHttpUtil;
import com.hletong.mob.util.AppManager;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 全局的Application
 * Created by cc on 2016/9/21.
 */
public abstract class HLApplication extends Application {
    private static long delta = Long.MAX_VALUE;//本地时间与服务器的时间差
    public static boolean JSON_LOG_ON = false;

    @Override
    public final void onCreate() {
        super.onCreate();
        if (AppManager.isOnMainProcess(this)) {
            onMainProcessCreate();
        }
    }

    @CallSuper
    public void onMainProcessCreate() {
        initHttpClient();
        AppManager.init(this);
        //全局接收session过期
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化默认的httpClient,如果子类需要请覆盖
     **/
    public void initHttpClient() {
        OkHttpUtil.initHttp(10, 10, 30);
    }

    /***
     * 接收全局的sessionTimeOut
     **/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public final void receiveSessionTimeout(EventSessionTimeout event) {
        handleSessionTimeout(event);
    }

    public abstract void handleSessionTimeout(EventSessionTimeout event);

    public static void setNewDate(long current, String response) {
        try {
            ////////////////////////////////////////////Tue, 15 Nov 2016 06:39:07 GMT
            SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            Date date = df.parse(response);
            final long delta = date.getTime() - current;//服务端时间 减去 本地时间
            if (Math.abs(delta) < Math.abs(HLApplication.delta)) {
                HLApplication.delta = delta;
            }
        } catch (Exception e) {
            Logger.e(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static long getDelta() {
        return delta;
    }
}