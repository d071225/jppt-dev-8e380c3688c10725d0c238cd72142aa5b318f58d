package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.SettingContract;
import com.hletong.hyc.presenter.ExitMemberPresenter;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyExitActivity extends BaseActivity implements SettingContract.IExitMemberView {
    @BindView(R.id.et_exitReason)
    EditText etExitReason;
    @BindView(R.id.submit)
    Button submit;
    private SettingContract.IExitMemberPresenter exitMemberPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_exit;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle(R.string.apply_exit);
        ButterKnife.bind(this);
        exitMemberPresenter = new ExitMemberPresenter(this);
    }

    @OnClick(R.id.submit)
    @Override
    public void onClick(View view) {
        if (exitMemberPresenter.validate()) {
            DialogFactory.showAlertWithNegativeButton(this, null, getString(R.string.exitMemberWarm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    exitMemberPresenter.exitMember();
                }
            });
        }
    }

    @Override
    public void exitMemberSuccess() {
        DialogFactory.showAlert(this, R.string.exiMemberSuccess, R.string.exiMemberTip, false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginActivity.toRootLogin(ApplyExitActivity.this);
            }
        });
    }

    @Override
    public String getExitReason() {
        return getTextValue(etExitReason);
    }

    @Override
    public String getUserType() {
        return LoginInfo.getLoginInfo().getUser_type();
    }
}
