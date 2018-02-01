package com.hletong.hyc.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.ForgetPsw;
import com.hletong.hyc.presenter.ForgetPsdPresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgetPswActivity extends BaseActivity implements ForgetPsw.View {
    @BindView(R.id.tv_getVerificationCode)
    TextView tvGetVerificationCode;
    @BindView(R.id.tv_memberName)
    EditText tvMemberName;
    @BindView(R.id.tv_phoneNumber)
    EditText tvPhoneNumber;
    @BindView(R.id.tv_verifyCode)
    EditText tvVerifyCode;

    private ForgetPsdPresenter mPresenter;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        private int count = 60;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count--;
            if (count == 0) {
                count = 60;
                tvGetVerificationCode.setText("获取验证码");
                tvGetVerificationCode.setEnabled(true);
            } else {
                tvGetVerificationCode.setText(count + "秒后重试");
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_psw;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mPresenter = new ForgetPsdPresenter(this);
    }

    @OnClick({R.id.ev_id_submit, R.id.tv_error, R.id.tv_getVerificationCode})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_getVerificationCode) {
            mPresenter.getVerifyCode(tvMemberName.getText().toString(), tvPhoneNumber.getText().toString());
        } else if (v.getId() == R.id.ev_id_submit) {
            mPresenter.resetPsd(tvMemberName.getText().toString(), tvPhoneNumber.getText().toString(), tvVerifyCode.getText().toString());
        } else if (v.getId() == R.id.tv_error) {
            CallPhoneDialog.getInstance().show(getSupportFragmentManager());
        }
    }

    @Override
    public void httpVerifyCode() {
        tvGetVerificationCode.setEnabled(false);
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void httpResetSuccess() {
        DialogFactory.showAlert(this, "密码重置成功", "请查看手机短信并妥善保存密码以便下次登录", "去登录", false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}
