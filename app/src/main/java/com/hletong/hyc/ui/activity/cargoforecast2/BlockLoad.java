package com.hletong.hyc.ui.activity.cargoforecast2;

import android.view.ViewStub;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.CargoAddressListFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.DatePickerView;
import com.hletong.hyc.util.DatePickerUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 2017/8/25.
 * 装货
 */

public class BlockLoad extends BlockBase<CargoForecastContract.BlockLoadPresenter> implements CargoForecastContract.BlockLoadView {
    @BindView(R.id.loadDT)
    DatePickerView loadDT;
    @BindView(R.id.loadTM)
    DatePickerView loadTM;
    @BindView(R.id.loadTM_)
    CommonInputView loadTM_;
    @BindView(R.id.loadAddress)
    CommonInputView loadAddress;
    @BindView(R.id.loadContact)
    CommonInputView loadContact;
    @BindView(R.id.loadContactTel)
    CommonInputView loadContactTel;

    private DatePickerUtil mPicker;

    public BlockLoad(ViewStub viewStub) {
        super(viewStub);
    }

    @Override
    CargoForecastContract.BlockLoadPresenter createPresenter() {
        return new BlockLoadPresenter(this);
    }

    @Override
    protected String getTitle() {
        return "装货信息";
    }

    @Override
    public void showDate(String from, String to) {
        loadDT.setStart(from);
        loadDT.setEnd(to);
    }

    @Override
    public void showTime(String from, String to) {
        loadTM.setStart(from);
        loadTM.setEnd(to);
    }

    @Override
    public void showAddress(String address) {
        loadAddress.setText(address);
    }

    @Override
    public void showContact(String contact, String tel) {
        loadContact.setText(contact);
        loadContactTel.setText(tel);
    }

    @OnClick({R.id.loadAddress})
    public void onClick() {
        getPresenter().chooseContact();
    }
}
