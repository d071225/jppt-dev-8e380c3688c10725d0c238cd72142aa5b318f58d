package com.hletong.hyc.ui.activity;

import android.view.View;
import android.view.ViewStub;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.Contract;
import com.hletong.hyc.model.validate.CargoContractInfo;
import com.hletong.hyc.presenter.CargoContractPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ddq on 2017/2/24.
 * 签订（查看）货方合同
 */

public class CargoContractActivity extends ContractActivity<Contract.CargoPresenter> {
    @BindView(R.id.password_title)
    View inputAreaTitle;
    @BindView(R.id.ship_psw)
    CommonInputView shipPsw;
    @BindView(R.id.ship_psw_confirm)
    CommonInputView shipPswConfirm;
    @BindView(R.id.receive_psw)
    CommonInputView receivePsw;
    @BindView(R.id.receive_psw_confirm)
    CommonInputView receivePswConfirm;

    @Override
    void toggleInputArea(boolean visible) {
        inputAreaTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
        shipPsw.setVisibility(visible ? View.VISIBLE : View.GONE);
        shipPswConfirm.setVisibility(visible ? View.VISIBLE : View.GONE);
        receivePsw.setVisibility(visible ? View.VISIBLE : View.GONE);
        receivePswConfirm.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    void inflateInputArea() {
        ViewStub part_cargo = (ViewStub) findViewById(R.id.part_cargo);
        part_cargo.inflate();
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sign) {
            getPresenter().submitContract(new CargoContractInfo(
                    shipPsw.getInputValue(),
                    shipPswConfirm.getInputValue(),
                    receivePsw.getInputValue(),
                    receivePswConfirm.getInputValue()
            ));
        }
    }

    @Override
    protected Contract.CargoPresenter createPresenter() {
        return new CargoContractPresenter(this, getIntent().getStringExtra("contractUuid"), getIntent().getStringExtra("cargoUuid"));
    }
}
