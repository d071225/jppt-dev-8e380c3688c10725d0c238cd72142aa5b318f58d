package com.hletong.hyc;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.evernote.android.job.JobManager;
import com.hletong.hyc.contract.LoginContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.enums.Config;
import com.hletong.hyc.http.parse.ItemParse;
import com.hletong.hyc.job.JpptJobCreator;
import com.hletong.hyc.job.UploadLocationJob;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.presenter.LoginPresenter;
import com.hletong.hyc.service.GTDataService;
import com.hletong.hyc.ui.activity.ModifyTextSizeActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.UnicornHelper;
import com.hletong.mob.HLApplication;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.net.EventSessionTimeout;
import com.hletong.mob.net.OkHttpUtil;
import com.hletong.mob.util.AMapLocationManager;
import com.hletong.mob.util.AppManager;
import com.igexin.sdk.PushManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tendcloud.tenddata.TCAgent;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.HttpParser;
import com.xcheng.okhttp.request.OkConfig;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.view.EasyView;
import com.xcheng.view.callback.GlobalLifecycleCallbacks;
import com.xcheng.view.controller.EasyFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;

/**
 * 货运主题
 * Created by cx on 2016/9/21.
 */
public class HyApplication extends HLApplication {
    private boolean isInLogin = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onMainProcessCreate() {
        super.onMainProcessCreate();
        TCAgent.LOG_ON = BuildConfig.DEBUG;
        TCAgent.init(this);
        TCAgent.setReportUncaughtExceptions(true);
        //统一管理生命周期
        globalLifeCycle();
        //设置所有的主题Theme
        BaseActivity.ThemeManager.THEMES = ModifyTextSizeActivity.THEMES;
        Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag("HLET_LOG").methodCount(1).showThreadInfo(false).build()) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
        EasyView.init(this, R.layout.jppt_dialog_loading);
        LocalBroadcastManager.getInstance(this);//初始化广播

        PushManager.getInstance().initialize(this, null);
        PushManager.getInstance().registerPushIntentService(this, GTDataService.class);
        //
        if (!AppTypeConfig.isCargo()) {
            //定时任务初始化
            JobManager.create(this).addJobCreator(new JpptJobCreator());
            UploadLocationJob.scheduleJob();
            UploadLocationJob.timerLocation();
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                startLocation();
            }
        }.start();
        //初始化客服入口
        UnicornHelper.init();
        //微信分享初始化
    }

    @Override
    public void initHttpClient() {
        Config config = Config.getConfig(BuildConfig.config);
        Constant.initHost(config.getHost());
        if (config.getCer() != null) {
            OkHttpUtil.initHttps(20, 10, 30, config.getCer());
        } else {
            OkHttpUtil.initHttp(20, 10, 30);
        }
        //设置Http请求的框架为OKHTTP
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OkHttpUtil.getOkHttpClient()));

        OkConfig okConfig = OkConfig.newBuilder()
                .baseUrl(config.getHost())
                .client(OkHttpUtil.getOkHttpClient())
                .postUiIfCanceled(false)
                .parserFactory(new HttpParser.Factory() {
                    @NonNull
                    @Override
                    public HttpParser<?> parser(OkRequest request) {
                        return ItemParse.INSTANCE;
                    }
                }).build();
        EasyOkHttp.init(okConfig);
    }

    @Override
    public void handleSessionTimeout(EventSessionTimeout event) {
        //防止多个请求同时过期导致多个后台登录，效率问题
        if (isInLogin)
            return;
        isInLogin = true;
        new LoginPresenter(new LoginContract.View() {
            @Override
            public void loginSuccess() {
                isInLogin = false;
                EventSessionTimeout.retryRequest();
            }

            @Override
            public void handleError(@NonNull BaseError error) {
                isInLogin = false;
                EventSessionTimeout.onSessionError();
                LoginInfo.loginDialog();
            }

            @Override
            public void showLoading() {
            }

            @Override
            public void hideLoading() {

            }
        }).login(AppManager.getLoginName(), AppManager.getPassword());
    }


    private static AMapLocation aMapLocation;

    /***全局开启定位***/
    public static void startLocation() {
        AMapLocationManager.getInstance().setLocationChangeListener(new AMapLocationManager.OnLocationChangeListener() {
            @Override
            public void onLocationChanged(@Nullable AMapLocation location) {
                aMapLocation = location;
            }
        });
        AMapLocationManager.getInstance().startLocation();
    }

    /**
     * 获取位置
     *
     * @return 位置信息
     */
    @Nullable
    public static AMapLocation getAMapLocation() {
        if (aMapLocation == null) {
            aMapLocation = AMapLocationManager.getInstance().getLastKnownLocation();
        }
        return aMapLocation;
    }

    private void globalLifeCycle() {
        registerActivityLifecycleCallbacks(new GlobalLifecycleCallbacks() {
            @Override
            public void onActivityStarted(Activity activity) {
                TCAgent.onPageStart(activity, activity.getClass().getSimpleName());//页面埋点
            }

            @Override
            public void onActivityPaused(Activity activity) {
                TCAgent.onPageEnd(activity, activity.getClass().getSimpleName());//页面埋点
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                cancel(activity);
            }

            @Override
            public void onFragmentStarted(FragmentManager fm, Fragment f) {
                TCAgent.onPageStart(f.getContext(), f.getClass().getSimpleName());
            }

            @Override
            public void onFragmentPaused(FragmentManager fm, Fragment f) {
                TCAgent.onPageEnd(f.getContext(), f.getClass().getSimpleName());//页面埋点
            }

            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                if (!(f instanceof EasyFragment) || !((EasyFragment) f).cacheView()) {
                    cancel(f);
                }
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentDestroyed(fm, f);
                cancel(f);
            }

            private void cancel(Object object) {
                if (EventBus.getDefault().isRegistered(object)) {
                    EventBus.getDefault().unregister(object);
                }
                OkHttpUtil.cancel(hashCode());
            }
        });
    }
}