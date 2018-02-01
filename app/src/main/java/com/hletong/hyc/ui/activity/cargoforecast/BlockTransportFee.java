package com.hletong.hyc.ui.activity.cargoforecast;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;

import com.hletong.hyc.R;
import com.hletong.hyc.model.AuthInfo;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.SimpleTextWatcher;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/24.
 * 自主交易-运费信息
 */

public class BlockTransportFee extends BaseBlock {
    @BindView(R.id.bargain)
    CommonInputView bargain;
    @BindView(R.id.bargain_hint)
    View bargainHint;
    @BindView(R.id.freightPrice)
    CommonInputView freightPrice;
    @BindView(R.id.freightTotalPrice)
    CommonInputView freightTotalPrice;

    private String cargoCount;//货物数量
    private boolean onlyBargain = false;

    public BlockTransportFee(ViewStub viewStub, CargoForecastActivity.BlockAction blockAction, ITransactionView switchDelegate) {
        super(viewStub, "运费信息", blockAction, switchDelegate);
    }

    @Override
    void viewInflated(View view) {
        freightPrice.getInput().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(cargoCount))
                    notifyItemChanged("self_trade_price", s.toString());
                else
                    calculateTotalPrice();
            }
        });
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        if (source.getTrans_type() == 300 && source.getBilling_type() == 3) {
            onItemSelected(new DictionaryItem("1", "是"), -1);
        } else {
            onItemSelected(new DictionaryItem("0", "否"), -1);
            freightPrice.setText(source.getOrign_unit_amt());
            freightPrice.getSuffix().setText(source.getUnitForFee());
        }
    }

    @OnClick(R.id.bargain)
    @Override
    public void onClick(View v) {
//        showSelector("appCustomQuoteFlagEnum", "运费议价", -1);
        if (onlyBargain) {
            DictionaryItem di = (DictionaryItem) bargain.getTag();
            if (di == null || "否".equals(di.getText())){
                showSelector("appCustomQuoteFlagEnum", "运费议价", -1);
            }else {
                showMessage("您当前的会员状态只能发布议价货源");
            }
        } else
            showSelector("appCustomQuoteFlagEnum", "运费议价", -1);
    }

    @Override
    boolean isBlockSatisfied() {
        DictionaryItem di = (DictionaryItem) bargain.getTag();
        return di != null && ("是".equals(di.getText()) || freightPrice.inputHaveValue());
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        if (billingType == 3) {
            LoginInfo loginInfo = LoginInfo.getLoginInfo();
            AuthInfo authInfo = loginInfo.getAuthInfo(3);
            onlyBargain = !authInfo.isAuthorized();
            showBlock(false);
        } else
            hideBlock();
    }

    @Override
    public boolean dataRetrieved(List<DictionaryItem> data, int extra) {
        if (onlyBargain) {
            onItemSelected(new DictionaryItem("1", "是"), -1);
            return true;
        } else
            return super.dataRetrieved(data, extra);
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        bargain.setText(item.getText());
        bargain.setTag(item);
        if ("否".equals(item.getText())) {
            bargainHint.setVisibility(View.GONE);
            freightPrice.setVisibility(View.VISIBLE);
            freightTotalPrice.setVisibility(View.VISIBLE);
        } else {
            bargainHint.setVisibility(View.VISIBLE);
            freightPrice.setVisibility(View.GONE);
            freightTotalPrice.setVisibility(View.GONE);
        }
        childViewVisibilityChanged();
    }

    @Override
    public void onBlockFieldChanged(BaseBlock block, String dictType, Object object) {
        if ("unitsEnum".equals(dictType)) {
            DictionaryItem di = (DictionaryItem) object;
            freightPrice.getSuffix().setText("元/" + di.getText());
        } else if ("self_trade_cargo_ct".equals(dictType)) {//货物数量发生了变化
            cargoCount = (String) object;
            calculateTotalPrice();
        }
    }

    @Override
    public List<DictionaryItem> prefetchItems() {
        ArrayList<DictionaryItem> list = new ArrayList<>();
        if (bargain.getTag() == null)
            list.add(new DictionaryItem("-1", "appCustomQuoteFlagEnum"));
        return list;
    }

    @Override
    public String getErrorMessage() {
        DictionaryItem di = (DictionaryItem) bargain.getTag();
        if (di == null)
            return "请选择是否议价";

//        if (onlyBargain && "否".equals(di.getText())){
//            return "您当前的会员状态只能发布议价货源";
//        }

        if ("否".equals(di.getText())) {
            if (!freightPrice.inputHaveValue())
                return "货物单价未填写";

            try {
                double value = Double.parseDouble(freightPrice.getInputValue());
                if (value <= 0) {
                    return "运费单价必须大于0";
                }
            } catch (NumberFormatException e) {
                return "无法解析运费单价";
            }
        }

        return null;
    }

    @Override
    public void fillSource(Source source) {
        source.setOrign_unit_amt(getValue(freightPrice.getInput()));
    }

    @Override
    public void getSelfTradeBlockParams(HashMap<String, String> params) {
        DictionaryItem di = (DictionaryItem) bargain.getTag();
        if ("否".equals(di.getText())) {
            params.put("orignUnitAmt", freightPrice.getInputValue());
        } else {
            params.put("orignUnitAmt", "0");
            params.put("transType", "300");
        }
    }

    private void calculateTotalPrice() {
        final String s = freightPrice.getInputValue();
        if (TextUtils.isEmpty(cargoCount) || TextUtils.isEmpty(s))
            freightTotalPrice.setText("0");

        try {
            double b1 = Double.parseDouble(cargoCount);
            double b2 = Double.parseDouble(s);
            freightTotalPrice.setText(b1 * b2);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
}
