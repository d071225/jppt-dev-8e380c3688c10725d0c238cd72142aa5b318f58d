package com.hletong.hyc.presenter;

import android.text.TextUtils;

import com.hletong.hyc.contract.LoginContract;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * 登录的业务处理
 * Created by cc on 2017/1/4.
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(final String loginName, final String password) {
        if (TextUtils.isEmpty(loginName)) {
            handleMessage("用户名不能为空");
            return;
        } else if (TextUtils.isEmpty(password)) {
            handleMessage("密码不能为空");
            return;
        }

        OkRequest request = LoginInfo.request(loginName, password, getView().hashCode());
        new ExecutorCall<LoginInfo>(request).enqueue(new EasyCallback<LoginInfo>(getView()) {
            @Override
            public void onSuccess(OkCall<LoginInfo> okCall, LoginInfo response) {
                LoginInfo.setLoginInfo(response, loginName, password);
                getView().loginSuccess();
            }
        });
    }
}
