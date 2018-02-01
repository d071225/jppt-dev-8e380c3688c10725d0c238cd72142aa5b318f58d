package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.SettingContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.presenter.ExitPresenter;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/4/13.
 * 工具箱
 */

public class ToolboxActivity extends BaseActivity implements SettingContract.IExitView {
    private SettingContract.IExitPresenter exitPresenter;
    @BindView(R.id.tv_PathPlanning)
    View mPathPlanningView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_toolbox;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.toolbox);
        mPathPlanningView.setVisibility(AppTypeConfig.isTruck() ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.tv_PathPlanning, R.id.tv_textSizeSet, R.id.tv_changeLoginPassword, R.id.tv_shenqingtuihui, R.id.tv_signout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_PathPlanning:
                toActivity(AddressChooserForRoutePlanActivity.class, null, null);
                break;
            case R.id.tv_textSizeSet:
                toActivity(ModifyTextSizeActivity.class, null, null);
                break;
            case R.id.tv_changeLoginPassword:
                toActivity(ModifyLoginPswActivity.class, null, null);
                break;

            case R.id.tv_shenqingtuihui:
                toActivity(ApplyExitActivity.class, null, null);
                break;
            case R.id.tv_signout:
                DialogFactory.showAlertWithNegativeButton(getContext(), true, "确认退出吗？", null, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (exitPresenter == null) {
                            exitPresenter = new ExitPresenter(ToolboxActivity.this);
                        }
                        exitPresenter.exit();
                    }
                }, "取消", null);
                break;
        }
    }

    @Override
    public void success(String message) {
        showMessage(message);
        LoginActivity.toRootLogin(this);

    }
}
