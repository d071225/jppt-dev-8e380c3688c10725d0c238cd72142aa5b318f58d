package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoRefusedReasonContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.presenter.CargoRefusedReasonPresenter;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/31.
 * 货源审核失败的原因
 */

public class CargoRefusedReasonActivity extends BaseActivity implements CargoRefusedReasonContract.View {
    @BindView(R.id.reason)
    TextView mReason;
    @BindView(R.id.dark_Btn)
    TextView mButton;

    @Override
    public int getLayoutId() {
        return R.layout.activtity_refused_reason;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle("审核未通过原因");
        mButton.setText("重新发布货源");
        CargoRefusedReasonContract.Presenter presenter = new CargoRefusedReasonPresenter(this, getIntent().getStringExtra("cargoUuid"));
        presenter.start();
    }

    @OnClick(R.id.dark_Btn)
    public void onClick() {
        Bundle bundle = new Bundle();
        Source source = new Source();
        source.setCargo_uuid(getIntent().getStringExtra("cargoUuid"));
        bundle.putParcelable("source", source);
        bundle.putBoolean("full_copy", true);
        toActivity(CargoForecastActivity.class, bundle, null);
    }

    @Override
    public void showReason(String message) {
        mReason.setText(message);
    }
}
