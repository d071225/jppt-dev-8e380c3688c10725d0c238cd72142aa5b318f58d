package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.AddressContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.CargoContact;
import com.hletong.hyc.model.validate.AddressInfo;
import com.hletong.hyc.presenter.AddressPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.base.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/14.
 * 编辑收发货地
 */

public class AddAddressActivity extends MvpActivity<AddressContract.Presenter> implements AddressContract.View {
    @BindView(R.id.select)
    CommonInputView mAddress;
    @BindView(R.id.details)
    EditText mDetails;
    @BindView(R.id.contact)
    CommonInputView mContact;
    @BindView(R.id.contact_tel)
    CommonInputView mContactTel;
    @BindView(R.id.harbor)
    CommonInputView mHarbor;
    @BindView(R.id.submit)
    Button submit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle("新增常用地址");
        getPresenter().start();
    }

    @OnClick({R.id.select, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select:
                toActivity(AddressSelectorActivity.class, null, 200, null);
                break;
            case R.id.submit:
                getPresenter().submit(new AddressInfo(
                        mDetails.getText().toString().trim(),
                        mContact.getInputValue(),
                        mContactTel.getInputValue(),
                        mHarbor.getInputValue()));
                break;
        }
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        Address address = Address.getAddress(data);
        mAddress.setText(address.buildAddress());
        getPresenter().addressChanged(address);
    }

    @Override
    protected AddressContract.Presenter createPresenter() {
        return new AddressPresenter(this, (CargoContact) getParcelable("address"));
    }

    @Override
    public void showTitle(String title) {
        setCustomTitle(title);
    }

    @Override
    public void showAddress(String address, String detail) {
        mAddress.setText(address);
        mDetails.setText(detail);
    }

    @Override
    public void showContact(String contact, String tel) {
        mContact.setText(contact);
        mContactTel.setText(tel);
    }

    @Override
    public void showDeep(String deep) {
        mHarbor.setText(deep);
    }
}
