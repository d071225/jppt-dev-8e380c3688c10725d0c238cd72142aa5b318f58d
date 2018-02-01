package com.hletong.hyc.util;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.ui.activity.LoginActivity;
import com.hletong.mob.annotation.LoginAction;
import com.xcheng.view.util.ToastLess;

/**
 * 解决过滤未登录操作
 * Created by chengxin on 2017/3/29.
 */
public class LoginFilterDelegate {
    private View mFilterActionView;
    private Activity mActivity;

    public LoginFilterDelegate(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 是否要过滤View的操作，如果不是View的点击,可以模拟，如new TextView().setOnClickListener(),
     * 在 onClick() 处理成功后的操作然后将View传进来,参考 willFilter(View.OnClickListener listener)，
     *
     * @param actionView 需要过滤的View
     * @return 是否过滤
     */
    public boolean willFilter(@NonNull View actionView) {
        if (isFilter()) {
            mFilterActionView = actionView;
            handle();
            return true;
        }
        return false;
    }

    public boolean willFilter(View.OnClickListener listener) {
        if (isFilter()) {
            mFilterActionView = new View(mActivity);
            mFilterActionView.setOnClickListener(listener);
            handle();
            return true;
        }
        return false;
    }

    /**
     * 处理登陆之后返回页面继续之前的用户操作
     */
    public void handleResume() {
        if (mFilterActionView != null && !isFilter()) {
            mFilterActionView.performClick();
        }
        mFilterActionView = null;
    }


    private boolean isFilter() {
        return LoginInfo.getLoginInfo() == null;
    }

    private void handle() {
        ToastLess.showToast("请先登录");
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.putExtra(LoginAction.KEY_LOGIN_ACTION, LoginAction.NO_LOGIN);
        mActivity.startActivity(intent);
    }
}
