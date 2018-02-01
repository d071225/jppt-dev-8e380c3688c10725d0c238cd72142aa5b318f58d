package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.UpgradeContract;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.VersionInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.net.JSONUtils;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.SPUtils;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/1/12.
 * 检查更新
 */

public class UpgradePresenter<T extends UpgradeContract.View> extends BasePresenter<T> implements UpgradeContract.Presenter {
    private VersionInfo mVersionInfo;
    private boolean manual;

    public UpgradePresenter(T v) {
        this(v, false);
    }

    public UpgradePresenter(@NonNull T baseView, boolean manual) {
        super(baseView);
        this.manual = manual;
    }

    @Override
    public void checkVersionInfo() {
        OkRequest request = EasyOkHttp.get(Constant.VERSION_UPDATE)
                .param("osType", "1")
                .param("currentVersion", AppManager.versionCode)
                .param("appType", String.valueOf(BuildConfig.app_type))
                .extra(JpptParse.ENTRY, "data")
                .build();

        new ExecutorCall<VersionInfo>(request).enqueue(new AnimCallBack<VersionInfo>(manual ? getView() : null) {
            @Override
            public void onError(OkCall<VersionInfo> okCall, EasyError error) {
                try {
                    if (error instanceof BusiError) {
                        BusiError busiError = (BusiError) error;
                        CommonResult commonResult = JSONUtils.fromJson(busiError.getResponse(), CommonResult.class);
                        getView().handleError(new DataError(commonResult.getErrorInfo()));
                        SPUtils.putBoolean("found_n_v", false);
                    } else {
                        getView().handleError((BaseError) error);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(OkCall<VersionInfo> okCall, VersionInfo response) {
                mVersionInfo = response;
                if (AppManager.versionCode < response.getCurrentVersion()) {
                    SPUtils.putBoolean("found_n_v", true);
                    //有新版本
                    getView().showUpgradeDialog(response);
                } else {
                    //没有新版本
                    SPUtils.putBoolean("found_n_v", true);
                    if (manual)
                        getView().success("已是最新版本");
                }
            }
        });
    }
}
