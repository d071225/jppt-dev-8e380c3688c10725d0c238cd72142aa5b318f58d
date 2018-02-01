package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hletong.hyc.R;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_password)
    EditText mPasswordEdit;
    @BindView(R.id.et_confirmPassword)
    EditText mConfirmPasswordEdit;
    @BindView(R.id.btn_next)
    Button btnNext;
    private RegisterInfo registerInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_password;
    }

    @Override
    public void initData() {
        super.initData();
        registerInfo = getSerializable(RegisterInfo.class.getSimpleName());
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle(R.string.set_password);
        ButterKnife.bind(this);
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

    @OnClick({R.id.btn_next})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btnNext) {
            RegisterInfo registerInfo = getRegisterInfo();
            if (registerInfo.validatePassword(this)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(RegisterInfo.class.getSimpleName(), registerInfo);
                toActivity(PhoneVerifyCodeActivity.class, bundle, null);
            }
        }
    }

    private RegisterInfo getRegisterInfo() {
        String password = mPasswordEdit.getText().toString();
        String confirmPassword = mConfirmPasswordEdit.getText().toString();
        registerInfo.setPassWord(password);
        registerInfo.setConfirmPassword(confirmPassword);
        return registerInfo;
    }

    private boolean isFirstPasswordOK = false;

    @OnTextChanged(R.id.et_password)
    void passwordChanged(Editable s) {
        if (s.length() >= 6 && !isFirstPasswordOK) {
            isFirstPasswordOK = true;
        }
        if (isFirstPasswordOK) {
            if (!checkPassword(s.toString())) {
                mPasswordEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_input_error, 0);
            } else {
                mPasswordEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_input_ok, 0);
            }
        }
    }

    @OnTextChanged(R.id.et_confirmPassword)
    void confirmPasswordChanged(Editable s) {
        if (mPasswordEdit.getText().toString().equals(s.toString()) && checkPassword(s.toString())) {
            mConfirmPasswordEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_input_ok, 0);
        } else {
            mConfirmPasswordEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_input_error, 0);
        }
    }

    public boolean checkPassword(String oriPassWord) {
        if (StringUtil.isTrimBlank(oriPassWord)) {
            return false;
        } else if (!RegexUtil.isPassword(oriPassWord)) {
            return false;
        }
        return true;
    }

}
