package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.SplashContract;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.util.SPUtils;

/**
 * Created by ddq on 2017/2/8.
 */

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    public SplashPresenter(@NonNull SplashContract.View baseView) {
        super(baseView);
    }

    @Override
    public void start() {
        //第一次启动app加载引导页面
        boolean startGuide = SPUtils.getBoolean(Constant.SP_KEY.KEY_FIRST_START_GUIDE, true);
        if (startGuide){
            getView().showGuideView();
        }else {
            getView().startCounting(getCountSeconds());
        }
    }

    @Override
    public void guideFinished() {
        //引导页面加载完毕
        SPUtils.putBoolean(Constant.SP_KEY.KEY_FIRST_START_GUIDE, false);
        //展示广告页面
        getView().startCounting(getCountSeconds());
    }

    /**
     * 广告页倒计时时长
     * @return
     */
    private int getCountSeconds(){
        return BuildConfig.DEBUG ? 0 : 3;
    }
}
