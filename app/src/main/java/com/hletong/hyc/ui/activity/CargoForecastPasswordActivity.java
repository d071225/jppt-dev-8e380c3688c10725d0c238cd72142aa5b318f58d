package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastPasswordContract;
import com.hletong.hyc.model.CargoNoContract;
import com.hletong.hyc.presenter.CargoForecastPasswordPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.base.BaseActivity;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 2017/6/8.
 * 长期协议价密码
 */

public class CargoForecastPasswordActivity extends BaseActivity implements CargoForecastPasswordContract.View {
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.deliver_psw)
    CommonInputView mDeliverPsw;
    @BindView(R.id.deliver_psw_c)
    CommonInputView mDeliverPswC;
    @BindView(R.id.receive_psw)
    CommonInputView mReceivePsw;
    @BindView(R.id.receive_psw_c)
    CommonInputView mReceivePswC;

    private CargoForecastPasswordContract.Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_forecast_password;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitHint();
            }
        });

        mPresenter = new CargoForecastPasswordPresenter(this, (CargoNoContract) getIntent().getParcelableExtra("cargo"));
        mPresenter.start();
    }

    @Override
    public void onBackPressed() {
        exitHint();
    }

    private void exitHint() {
        DialogFactory.showAlertWithNegativeButton(this, "提示", "退出后长期协议价货源将变为普通货源，确定要退出吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    @OnClick(R.id.submit)
    public void onClick() {
        try {
            mPresenter.submit(
                    mDeliverPsw.getInputValue(),
                    mDeliverPswC.getInputValue(),
                    mReceivePsw.getInputValue(),
                    mReceivePswC.getInputValue()
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }

    @Override
    public void showHint(String text) {
        mText.setText(text);
    }
}
