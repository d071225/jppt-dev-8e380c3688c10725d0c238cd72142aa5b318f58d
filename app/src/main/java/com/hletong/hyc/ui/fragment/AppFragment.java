package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ViewTreeObserver;

import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.contract.AppContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.http.parse.ListParse;
import com.hletong.hyc.model.AdInfo;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.presenter.UnTodoPresenter;
import com.hletong.hyc.ui.activity.AdShowActivity;
import com.hletong.hyc.ui.activity.AppGuideHelper;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.LoginFilterDelegate;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.base.BaseFragment;
import com.hletong.mob.util.SPUtils;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.HttpParser;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 首页各模块入口
 * Created by cc on 2016/10/13.
 */
public abstract class AppFragment extends BaseFragment implements AppContract.View {

    private AppContract.Presenter mUnTodoPresenter;
    private LoginFilterDelegate mLoginFilterDelegate;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this, getView());
        mUnTodoPresenter = new UnTodoPresenter(this);
        if (!SPUtils.getBoolean(Constant.SP_KEY.KEY_FIRST_ENTER_GUIDE, true)) {
            autoAdFirstOpen();
            getAdInfo(false);
        } else {
            getAdInfo(true);
            getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    AppGuideHelper guideHelper = new AppGuideHelper(getActivity());
                    appGuideFirstOpen(guideHelper);
                    getView().getViewTreeObserver().removeOnPreDrawListener(this);
                    SPUtils.putBoolean(Constant.SP_KEY.KEY_FIRST_ENTER_GUIDE, false);
                    return true;
                }
            });
        }
    }

    private void getAdInfo(boolean justLoadData) {
        List<AdInfo> infos = SPUtils.getBean(Constant.SP_KEY.KEY_ADINFO_LIST, new TypeToken<List<AdInfo>>() {
        });
        if (infos != null && !justLoadData) {
            Intent intent = new Intent(getContext(), AdShowActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }
        final OkRequest request = EasyOkHttp.get(Constant.AD)
                .param("os_type", AppTypeConfig.getOsType())
                .parserFactory(new HttpParser.Factory() {
                    @NonNull
                    @Override
                    public HttpParser<?> parser(OkRequest request) {
                        return ListParse.INSTANCE;
                    }
                })
                .extra(JpptParse.ENTRY, "data")
                .build();
        new ExecutorCall<List<AdInfo>>(request).enqueue(new UICallback<List<AdInfo>>() {
            @Override
            public void onError(OkCall<List<AdInfo>> okCall, EasyError error) {
                if (error instanceof DataError) {
                    CommonResult result = CommonResult.parseResult(((DataError) error).getResponse());
                    if (result != null && result.isSuccess()) {
                        SPUtils.remove(Constant.SP_KEY.KEY_ADINFO_LIST);
                    }
                }
            }

            @Override
            public void onSuccess(OkCall<List<AdInfo>> okCall, List<AdInfo> response) {
                SPUtils.putBean(Constant.SP_KEY.KEY_ADINFO_LIST, response, new TypeToken<List<AdInfo>>() {
                });
            }
        });
    }

    /**
     * 每次进入app需要弹出 的广告模块
     */
    protected abstract void autoAdFirstOpen();

    /**
     * 第一次打开时功能引导
     *
     * @param guideHelper
     */
    protected abstract void appGuideFirstOpen(AppGuideHelper guideHelper);

    private void filterResume() {
        if (LoginInfo.getLoginInfo() == null && mLoginFilterDelegate == null) {
            mLoginFilterDelegate = new LoginFilterDelegate(getActivity());
        }
        if (mLoginFilterDelegate != null) {
            mLoginFilterDelegate.handleResume();
        }
    }

    protected boolean willFilter(android.view.View view) {
        return mLoginFilterDelegate != null && mLoginFilterDelegate.willFilter(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        mUnTodoPresenter.getUnTodoInfo();
        filterResume();

    }
}
