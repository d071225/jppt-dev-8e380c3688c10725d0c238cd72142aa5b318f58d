package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.SettingContract;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.util.AppManager;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

/**
 * 登录的业务处理
 * Created by cc on 2017/1/4.
 */
public class ExitPresenter extends BasePresenter<SettingContract.IExitView> implements SettingContract.IExitPresenter {

    public ExitPresenter(SettingContract.IExitView view) {
        super(view);
    }

    @Override
    public void exit() {
        OkRequest request = EasyOkHttp.get(Constant.LOGOUT)
                .extra(JpptParse.SESSION_NOT_CHECKED, true)
                .param("deviceType", 2)
                .param("clientId", AppManager.PUSH_ID)
                .tag(tag())
                .build();
        easySubmit(request);
    }

    @Override
    protected void easySubmitFailed(BaseError error) {
        getView().success("退出成功");
    }

    @Override
    protected void easySubmitSucceed(CommonResult cr) {
        getView().success("退出成功");
    }
}
