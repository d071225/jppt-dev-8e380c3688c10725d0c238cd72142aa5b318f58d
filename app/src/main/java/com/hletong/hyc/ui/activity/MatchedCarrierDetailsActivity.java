package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.MatchedCarrierContract;
import com.hletong.hyc.model.ForecastTransporterInfo;
import com.hletong.hyc.model.MatchedCarrier;
import com.hletong.hyc.presenter.MatchedCarrierPresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/27.
 * 车船详情
 */

public class MatchedCarrierDetailsActivity extends BaseActivity implements MatchedCarrierContract.View {
    @BindView(R.id.vf)
    ViewStub vf;
    @BindView(R.id.vm)
    ViewStub vm;

    private MatchedCarrierContract.Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_matched_carrier_details;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle("车船详情");
        mPresenter = new MatchedCarrierPresenter((MatchedCarrier) getParcelable("fti"), this);
        mPresenter.start();
    }

    @OnClick(R.id.call)
    public void onClick() {
        mPresenter.call(getSupportFragmentManager());
    }

    @Override
    public void initForecast(MatchedCarrier fti) {
        View view = vf.inflate();
        TextView title = (TextView) view.findViewById(R.id.carrierNo);
        title.setText(fti.getCarrierNo());
        TextView from = (TextView) view.findViewById(R.id.from);
        from.setText(fti.getOriginAddress().buildAddress());
        TextView to = (TextView) view.findViewById(R.id.to);
        to.setText(fti.getDestAddress().buildAddress());
        TextView capacity = (TextView) view.findViewById(R.id.capacity);
        capacity.setText("可配载量：" + fti.getCapacityWithUnit());
        TextView availableDt = (TextView) view.findViewById(R.id.availableDt);
        availableDt.setText(fti.getTimeDes("-"));
    }

    @Override
    public void initMatched(MatchedCarrier fti) {
        View view = vm.inflate();
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(fti.getCarrierNo());
        TextView address = (TextView) view.findViewById(R.id.address);
        address.setText(fti.getForecastUuid() == null ? fti.getNowAddress() : fti.getOriginAddress().buildAddress());
    }
}
