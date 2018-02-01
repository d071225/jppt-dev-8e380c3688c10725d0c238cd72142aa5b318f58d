package com.hletong.hyc.ui.activity.cargoforecast2;

import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.cargo.BlockUnloadPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockUnload extends BlockBase<CargoForecastContract.BlockUnloadPresenter> implements CargoForecastContract.BlockUnloadView {
    @BindView(R.id.unloadAddress)
    CommonInputView unloadAddress;
    @BindView(R.id.unloadContact)
    CommonInputView unloadContact;
    @BindView(R.id.unloadContactTel)
    CommonInputView unloadContactTel;
    @BindView(R.id.routePlan)
    TextView routePlan;

    public BlockUnload(ViewStub viewStub) {
        super(viewStub);
    }

    @Override
    public void showAddress(String address) {
        unloadAddress.setText(address);
    }

    @Override
    public void showContact(String contact, String tel) {
        unloadContact.setText(contact);
        unloadContactTel.setText(tel);
    }

    @Override
    CargoForecastContract.BlockUnloadPresenter createPresenter() {
        return new BlockUnloadPresenter(this);
    }

    @Override
    protected String getTitle() {
        return "卸货信息";
    }

    @OnClick({R.id.unloadAddress, R.id.routePlan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.unloadAddress:
                getPresenter().chooseContact();
                break;
            case R.id.routePlan:
                getPresenter().viewTransportRoute();
                break;
        }
    }
}
