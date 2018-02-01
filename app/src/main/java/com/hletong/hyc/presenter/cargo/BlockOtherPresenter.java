package com.hletong.hyc.presenter.cargo;

import android.support.annotation.NonNull;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;

import java.util.Map;

/**
 * Created by dongdaqing on 2017/8/25.
 * 其他信息
 */

public class BlockOtherPresenter extends BlockBasePresenter<CargoForecastContract.BlockOtherView> implements CargoForecastContract.BlockOtherPresenter {
    private DictionaryItem tradeType;

    public BlockOtherPresenter(@NonNull CargoForecastContract.BlockOtherView baseView) {
        super(baseView);
    }

    @Override
    public void sourceAttached(Source source) {
        getView().showSpecialRequest(source.getSpecialRequest());
    }

    @Override
    protected void handleBillingType(int billingType) {
        super.handleBillingType(billingType);

        if (billingType == 3) {
            //自主交易没有交易类型
            getView().hideView(R.id.tradeType);
        } else {
            getView().showView(R.id.tradeType);
        }
    }

    @Override
    public void dictionarySelected(String key, DictionaryItem item) {
        if ("transTypeEnum".equals(key)){
            tradeType = item;
            getView().updateTradeType(tradeType.getText());
        }
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}
