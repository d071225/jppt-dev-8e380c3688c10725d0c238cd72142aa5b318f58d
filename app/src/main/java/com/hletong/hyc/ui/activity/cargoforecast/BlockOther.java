package com.hletong.hyc.ui.activity.cargoforecast;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/22.
 * 其他信息
 */

public class BlockOther extends BaseBlock {
    @BindView(R.id.tradeType)
    CommonInputView tradeType;
    @BindView(R.id.specialRequestTitle)
    CommonInputView specialRequestTitle;
    @BindView(R.id.specialRequest)
    EditText specialRequest;

    private boolean bookOnly = false;//是否仅允许挂价
    /**
     * 非自主交易货源上传时需要这个字段，但是这个字段跟这个模块没有任何关系
     * 为什么要放在这里获取呢？
     * 这个就不说了！！！！！！
     */
    private String taxRate;

    public BlockOther(ViewStub VSOther, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView switchDelegate) {
        super(VSOther, "其他信息", dictItemDialog, switchDelegate);
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        DictionaryItem di = source.getTransTypeAsDI();
        if (di != null) onItemSelected(di, -1);
        specialRequest.setText(source.getOSpecialReq());
    }

    @OnClick({R.id.tradeType})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tradeType:
                if (bookOnly) {
                    showMessage("兜底运输时交易方式只能是挂价");
                    return;
                }
                showSelector("transTypeEnum", "交易类型", 0);
                break;
        }
    }

    @Override
    public List<DictionaryItem> prefetchItems() {
        List<DictionaryItem> list = new ArrayList<>();
        if (tradeType.getTag() == null)
            list.add(new DictionaryItem("0", "transTypeEnum"));
        if (TextUtils.isEmpty(taxRate)) {
            list.add(new DictionaryItem("1", "taxRate"));
        }
        return list;
    }

    @Override
    public boolean dataRetrieved(List<DictionaryItem> data, int extra) {
        if (extra == 0) {
            //默认选择挂价
            for (DictionaryItem di : data)
                if ("挂价".equals(di.getText())) {
                    onItemSelected(di, extra);
                    return true;
                }
        }
        return super.dataRetrieved(data, extra);
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        if (extra == 0) {
            tradeType.setText(item.getText());
            tradeType.setTag(item);
        } else if (extra == 1) {
            taxRate = item.getText();
        }
    }

    @Override
    public void onBlockFieldChanged(BaseBlock block, String dictType, Object object) {
        //选择了兜底（平台开票才有兜底，自主开票那叫指定运输），只能是挂价
        if (getBillingType() == 1 && "appointTransportFlagEnum".equals(dictType)) {
            DictionaryItem di = (DictionaryItem) object;
            bookOnly = "是".equals(di.getText());
            if (bookOnly) {
                onItemSelected(new DictionaryItem("2", "挂价"), -1);
            }
        }
    }

    @Override
    public String getErrorMessage() {
        if (getBillingType() != 3){
            if (tradeType.getTag() == null)
                return "请选择交易方式";
            if (TextUtils.isEmpty(taxRate))
                return "程序内部出错";
        }
        return null;
    }

    @Override
    public void getBlockParams(HashMap<String, String> params) {
        params.put("trans_type", getTag(tradeType).getId());
        params.put("special_req", specialRequest.getText().toString().trim());
        params.put("transport_tax_rt", taxRate);
    }

    @Override
    public void getSelfTradeBlockParams(HashMap<String, String> params) {
        params.put("specialReq", specialRequest.getText().toString().trim());
    }

    @Override
    boolean isBlockSatisfied() {
        //非自主交易，要选择交易类型
        return getBillingType() == 3 || (tradeType.getTag() != null && !TextUtils.isEmpty(taxRate));
    }

    @Override
    public void fillSource(Source source) {
        source.setRoundTransType((DictionaryItem) tradeType.getTag());
        source.setSpecialReq(specialRequest.getText().toString().trim());
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        super.billingTypeChangedInternal(billingType);
        clear(tradeType);
        if (billingType == 3) {
            setViewVisibility(tradeType, View.GONE);
        } else {
            setViewVisibility(tradeType, View.VISIBLE);
        }
    }
}