package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.RegisterContract;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.presenter.RegisterPresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.SpanFormat;
import com.hletong.mob.annotation.LoginAction;
import com.hletong.mob.base.CountDownActivity;
import com.hletong.mob.util.StringUtil;
import com.xcheng.view.util.JumpUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class PhoneVerifyCodeActivity extends CountDownActivity<RegisterContract.IRegisterPresenter> implements RegisterContract.IRegisterView{
    @BindView(R.id.tv_getVerificationCode)
    TextView tvGetVerificationCode;
    @BindView(R.id.et_phoneNumber)
    EditText mPhoneNumberEdit;
    @BindView(R.id.et_verifyCode)
    EditText mVerifyCodeEdit;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.checkboxAgree)
    CheckBox checkboxAgree;
    @BindView(R.id.ll_deleteGroup1)
    LinearLayout ll_deleteGroup1;
    @BindView(R.id.et_deleteNum1)
    EditText etFirstTel;

    private RegisterInfo registerInfo;
    private static final int CONTRACT_CODE = 1;

    @Override
    public void onTicking(long hour, long minute, long second) {
        tvGetVerificationCode.setText(String.format(Locale.CHINA, "%d秒后重试", second));
    }

    @Override
    public void countFinished() {
        tvGetVerificationCode.setEnabled(true);
        tvGetVerificationCode.setText(R.string.get_verificationCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_verifycode;
    }

    @Override
    public void initData() {
        super.initData();
        registerInfo = getSerializable(RegisterInfo.class.getSimpleName());
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle(R.string.phoneVerificationCode);
        ButterKnife.bind(this);
        btnRegister.setEnabled(checkboxAgree.isChecked());
        SpanFormat.formatSpanEnd(checkboxAgree, R.string.hlet_user_protocol, 10, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户协议
                toActivity(RegisterContractActivity.class, null, CONTRACT_CODE, null);
            }
        });
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == CONTRACT_CODE) {
            checkboxAgree.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CallPhoneDialog.getInstance().show(getSupportFragmentManager());
        return true;
    }

    @OnClick({R.id.btn_register, R.id.tv_getVerificationCode, R.id.iv_deleteNum1, R.id.iv_addNum})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                getPresenter().register();
                break;
            case R.id.tv_getVerificationCode:
                getPresenter().getVerifyCode();
                break;
            case R.id.iv_addNum:
                if (ll_deleteGroup1.getVisibility() == View.VISIBLE) {
                    showMessage("最多添加一个备用号码");
                    return;
                }
                ll_deleteGroup1.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_deleteNum1:
                ll_deleteGroup1.setVisibility(View.GONE);
                break;
        }
    }

    @OnCheckedChanged(R.id.checkboxAgree)
    public void agree(CompoundButton view, boolean isChecked) {
        btnRegister.setEnabled(view.isChecked());
        btnRegister.setTextColor(view.isChecked() ? Color.parseColor("#ffffff") : Color.parseColor("#33ffffff"));
    }

    @Override
    public void onVerifyCode(String message) {
        showMessage(message);
        tvGetVerificationCode.setEnabled(false);
        //开始倒计时
        startCounting(59);
    }

    @Override
    public void onSuccess(RegisterInfo registerInfo, String message) {
        showMessage(message);
        Bundle bundle = createLoginBundle(LoginAction.REGISTER);
        bundle.putSerializable(RegisterInfo.class.getSimpleName(), registerInfo);
        JumpUtil.toActivityClearTopWithState(this, LoginActivity.class, bundle);
    }

    @NonNull
    @Override
    public RegisterInfo getRegisterInfo() {
        String phoneNumber = getTextValue(mPhoneNumberEdit);
        String verifyCode = getTextValue(mVerifyCodeEdit);
        String firstTel = getTextValue(etFirstTel);
        registerInfo.setMember_tel(phoneNumber);
        registerInfo.setMessagecheck(verifyCode);
        if (!StringUtil.isTrimBlank(firstTel)) {
            registerInfo.setFirst_tel(firstTel);
        }
        return registerInfo;
    }

    @Override
    protected RegisterContract.IRegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }
}
