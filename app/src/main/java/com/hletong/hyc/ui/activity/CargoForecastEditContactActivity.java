package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoContactContract;
import com.hletong.hyc.model.BusinessContact;
import com.hletong.hyc.model.validate.ContactInfo;
import com.hletong.hyc.presenter.CargoContactPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.base.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/15.
 * 编辑收发货人
 */

public class CargoForecastEditContactActivity extends MvpActivity<CargoContactContract.Presenter> implements CargoContactContract.View {
    @BindView(R.id.name)
    CommonInputView name;
    @BindView(R.id.taxCode)
    CommonInputView taxCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_contact;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setCustomTitle("新增联系人");
        ButterKnife.bind(this);
        getPresenter().start();
    }

    @OnClick(R.id.submit)
    public void onClick() {
        getPresenter().submit(new ContactInfo(name.getInputValue(), taxCode.getInputValue()));
    }

    @Override
    protected CargoContactContract.Presenter createPresenter() {
        return new CargoContactPresenter(this, (BusinessContact) getParcelable("mContact"));
    }

    @Override
    public void showTitle(String title) {
        setCustomTitle(title);
    }

    @Override
    public void showName(String name) {
        this.name.setText(name);
    }

    @Override
    public void showTaxCode(String taxCode) {
        this.taxCode.setText(taxCode);
    }
}
