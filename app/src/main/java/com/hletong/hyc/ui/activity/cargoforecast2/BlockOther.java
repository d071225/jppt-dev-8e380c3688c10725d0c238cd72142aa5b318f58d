package com.hletong.hyc.ui.activity.cargoforecast2;

import android.view.ViewStub;
import android.widget.EditText;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.cargo.BlockOtherPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;

import butterknife.BindView;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockOther extends BlockBase implements CargoForecastContract.BlockOtherView {
    @BindView(R.id.tradeType)
    CommonInputView tradeType;
    @BindView(R.id.specialRequest)
    EditText specialRequest;

    public BlockOther(ViewStub viewStub) {
        super(viewStub);
    }

    @Override
    public void updateTradeType(String name) {
        tradeType.setText(name);
    }

    @Override
    public void showSpecialRequest(String request) {
        specialRequest.setText(request);
    }

    @Override
    public String getSpecialRequest() {
        return specialRequest.getText().toString().trim();
    }

    @Override
    protected String getTitle() {
        return "其他信息";
    }

    @Override
    CargoForecastContract.BlockPresenter createPresenter() {
        return new BlockOtherPresenter(this);
    }
}
