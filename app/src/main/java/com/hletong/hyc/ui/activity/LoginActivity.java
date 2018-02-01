package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.LoginContract;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.presenter.LoginPresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.mob.annotation.LoginAction;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.net.EventSessionTimeout;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.FinishOptions;
import com.hletong.mob.util.PopInputSelect;
import com.igexin.sdk.PushManager;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.widget.CommonView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    @BindView(R.id.cv_loginName)
    CommonView mCVLoginName;
    @BindView(R.id.cv_password)
    CommonView mCVPassword;
    @BindView(R.id.iv_eye)
    View mEyePwdCtrlView;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    private LoginPresenter mLoginPresenter;
    //显示用户名选择
    private PopInputSelect popInputSelect;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.iv_popList, R.id.tv_register, R.id.btn_login, R.id.iv_eye, R.id.tv_forgetpassword, R.id.official_website, R.id.relative_service})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_popList:
                popInputSelect.showPopWindow(0, 0);
                break;
            case R.id.tv_register:
                toActivity(RegisterActivity.class, null, null);
                break;
            case R.id.btn_login:
                if (mLoginPresenter == null) {
                    mLoginPresenter = new LoginPresenter(this);
                }
                mLoginPresenter.login(mCVLoginName.getText(), mCVPassword.getText());
                break;
            case R.id.iv_eye:
                boolean isSelect = mEyePwdCtrlView.isSelected();
                mEyePwdCtrlView.setSelected(!isSelect);
                mCVPassword.getInputView().setInputType(!isSelect ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case R.id.tv_forgetpassword:
                toActivity(ForgetPswActivity.class, null, null);
                break;
            case R.id.official_website:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.hletong.com"));
                startActivity(intent);
                break;
            case R.id.relative_service:
                CallPhoneDialog.getInstance().show(getSupportFragmentManager());
                break;
        }
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setNavigationIcon(null);
        ButterKnife.bind(this);
        //退出账号重新登录
        if (getLoginAction(getIntent()) == LoginAction.RE_LOGIN) {
            LoginInfo.removeLoginInfo();
        }
        String loginName = AppManager.getLoginName();
        if (loginName != null) {
            mCVLoginName.setText(loginName);
        }
        if (BuildConfig.DEBUG && loginName == null) {
            switch (BuildConfig.app_type) {
                case 1://货
                    mCVLoginName.setText("会员5566");
                    break;
                case 2://车
                    mCVLoginName.setText("浙A55236");
                    break;
                case 3://船
                    mCVLoginName.setText("船舶会员89");
                    break;
            }
            mCVPassword.setText("999999");
        }
        mCVLoginName.getInputView().setSelection(mCVLoginName.getText().length());
        popInputSelect = new PopInputSelect(mCVLoginName.getInputView(), "loginNameSet", -LocalDisplay.dp2px(5));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getLoginAction(intent) == LoginAction.REGISTER) {
            RegisterInfo info = (RegisterInfo) intent.getSerializableExtra(RegisterInfo.class.getSimpleName());
            mCVLoginName.setText(info.getLoginName());
            mCVPassword.setText(info.getOriPassWord());
            findViewById(R.id.btn_login).performClick();
        }
    }

    public static void toRootLogin(BaseActivity baseActivity) {
        //清除所有通知
        NotificationManagerCompat manager = NotificationManagerCompat.from(baseActivity);
        manager.cancelAll();
        //关闭推送
        PushManager.getInstance().turnOffPush(baseActivity);
//        JumpUtil.toActivityBeRoot(baseActivity, LoginActivity.class, baseActivity.createLoginBundle(LoginAction.RE_LOGIN));
    }

    @Override
    public void loginSuccess() {
        showMessage("登录成功");
        PushManager.getInstance().turnOnPush(getContext());
        popInputSelect.saveInput();
        if (getLoginAction(getIntent()) == LoginAction.NO_LOGIN) {
            finish();
        } else if (getLoginAction(getIntent()) == LoginAction.SESSION_TIMEOUT) {
            EventSessionTimeout.retryRequest();
            //结束返回之前页
            finish();
        } else {
            toActivity(MainActivity.class, null, FinishOptions.FINISH_ONLY());
        }
    }

    private int getLoginAction(@NonNull Intent intent) {
        return intent.getIntExtra(LoginAction.KEY_LOGIN_ACTION, LoginAction.NORMAL);
    }
}
