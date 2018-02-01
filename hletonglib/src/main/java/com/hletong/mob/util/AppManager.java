package com.hletong.mob.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * 必须在Application启动时就调用,简化Context传入，做初始化操作
 */
public final class AppManager {

    private static Context sAppContext;

    //保留一份同步的Cookie 防止每次都从sp中获取做IO操作
    private static String sCookie;

    public static String appId;
    public static String imei;
    public static String PUSH_ID;//个推推送ID

    //app版本信息
    public static int versionCode;
    public static String versionName;
    public static String appName;

    /**
     * *********************************************************************************************
     * Global ApplicationContext
     * *********************************************************************************************
     */
    //调用初始化数据
    public static void init(Context context) {
        sAppContext = context.getApplicationContext();
        imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        appId = context.getPackageName();
        appName = (String) context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 重置Cookie
     **/
    public static void setCookie(@NonNull String cookie) {
        sCookie = cookie;
        SPUtils.putString("HLCookie", cookie);
    }

    /***
     * 删除Cookie
     **/
    public static void removeCookie() {
        sCookie = null;
        SPUtils.remove("HLCookie");
    }

    public static String getCookie() {
        if (sCookie == null) {
            sCookie = SPUtils.getString("HLCookie", null);
        }
        return sCookie;
    }

    /**
     * 不要用此context做 View 初始化，inflate等操作，否则会使用系统默认的主题样式，如果你自定义了某些样式可能不会被使用。
     *
     * @return applicationContext 对象
     */
    public static Context getContext() {
        return sAppContext;
    }

    /**
     * 获取文件共享认证的Authority
     *
     * @return 认证authority
     */
    public static String getAuthority() {
        return appId + ".FileProvider";
    }

    /**
     * 全局保存用户名密码
     *
     * @param loginName 登录名
     * @param password  密码
     */
    public static void setLoginNameAndPassword(@NonNull String loginName, @NonNull String password) {
        SPUtils.putString("HLLoginName", loginName);
        SPUtils.putString("HLPassWord", password);
    }

    public static String getLoginName() {
        return SPUtils.getString("HLLoginName", null);
    }

    public static String getPassword() {
        return SPUtils.getString("HLPassWord", null);
    }

    public static void removePassword() {
        SPUtils.remove("HLPassWord");
    }

    /**
     * 当前进程是否在App主进程
     *
     * @param context 上下文
     * @return 返回布尔值
     */
    public static boolean isOnMainProcess(@NonNull Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager.RunningAppProcessInfo myProcess = null;
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = mActivityManager.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : runningApps) {
                if (appProcess.pid == pid) {
                    myProcess = appProcess;
                    break;
                }
            }
        }
        return myProcess != null && context.getPackageName().equals(myProcess.processName);
    }
}
