package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.SettingContract;
import com.hletong.hyc.presenter.ModifyPsdPresenter;
import com.hletong.mob.base.BaseActivity;
import com.xcheng.view.widget.CommonView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLoginPswActivity extends BaseActivity implements SettingContract.IModifyPsdView {
    @BindView(R.id.cv_oldPsw)
    CommonView mCvOldPsw;
    @BindView(R.id.cv_newPsw)
    CommonView mCvNewPsw;
    @BindView(R.id.cv_confirmNewPsw)
    CommonView mCvConfirmNewPsw;

    private ModifyPsdPresenter modifyPsdPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_revise_login_psw;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ev_id_submit)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.ev_id_submit) {
            if (modifyPsdPresenter == null) {
                modifyPsdPresenter = new ModifyPsdPresenter(this);
            }
            modifyPsdPresenter.changePassword(mCvOldPsw.getText(), mCvNewPsw.getText(), mCvConfirmNewPsw.getText());
        }
    }

    @Override
    public void success(String s) {
        showMessage(s);
        LoginActivity.toRootLogin(this);
    }
}
