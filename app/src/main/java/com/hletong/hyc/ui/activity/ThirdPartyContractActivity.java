package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.ThirdPartyContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TransportContractDetails;
import com.hletong.hyc.presenter.ThirdPartyContractPresenter;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/1/10.
 * 自主交易三方协议
 * 1.摘牌之前查看 => 直接把Source传进来进行展示
 * 2.摘牌之后查看 => 获取货源详情，从中取agrtUuid，然后通过详情接口再去取其他数据
 * 自主开票三方协议
 * 1.在签合同的时候查看
 */

public class ThirdPartyContractActivity extends BaseActivity implements ThirdPartyContract.View {
    @BindView(R.id.contract)
    TextView contract;
    @BindView(R.id.confirm)
    Button confirm;

    private ThirdPartyContract.Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_third_party_contract;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.self_trade_third_party_contract_title);
        TransportContractDetails contractDetails = getParcelable("contract");
        Source source = getIntent().getParcelableExtra("source");
        if (contractDetails != null) {
            setCustomTitle(R.string.transport_third_party_contract_title);
            mPresenter = new ThirdPartyContractPresenter(this, contractDetails);
        } else if (source != null) {//这种情况来自货源预报，查看自主交易合同或者货物运输三方合同
            //在货源预报界面设置运费单价是放到orign_unit_amt字段，而在ThirdPartyContractPresenter里面生成合同用的是transport_unit_amt字段
            source.setTransport_unit_amt(source.getOrign_unit_amt());
            if (source.getBilling_type() == 2)//自主开票
                setCustomTitle(R.string.transport_third_party_contract_title);
            mPresenter = new ThirdPartyContractPresenter(this, source);
        } else
            mPresenter = new ThirdPartyContractPresenter(this, getIntent().getIntExtra("bookRefType", -1));

        mPresenter.start();

        if (getIntent().getBooleanExtra("showConfirmButton", false)) {
            confirm.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.confirm)
    @Override
    public void onClick(View v) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setupContractRequest() {
        final String contractUuid = getIntent().getStringExtra("contractUuid");
        final String cargoUuid = getIntent().getStringExtra("cargoUuid");
        if (contractUuid != null) {
            mPresenter.loadContractByContractID(contractUuid);
        } else if (cargoUuid != null) {
            mPresenter.loadContractByCargoID(cargoUuid, getIntent().getDoubleExtra("price", 0), getIntent().getDoubleExtra("cargo", 0));
        }
    }

    @Override
    public void showContract(Spanned mSpanned) {
        contract.setText(mSpanned);
    }
}
