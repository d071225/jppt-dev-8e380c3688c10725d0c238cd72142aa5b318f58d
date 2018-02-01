package com.hletong.hyc.ui.activity.cargoforecast;

import android.view.View;
import android.view.ViewStub;

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
 * 运输信息，自主交易的承运信息（运输方式，车（船）型等）是在这里面选择的，
 * 而非自主交易（平台开票，自主开票）的承运信息是在选择货源的时候就选好了，这里不做选择
 */

public class BlockTransport extends BaseBlock {

    @BindView(R.id.multiTransportFlag)
    CommonInputView multiTransportFlag;
    @BindView(R.id.transportDays)
    CommonInputView transportDays;
    @BindView(R.id.load_days)
    CommonInputView loadDays;
    @BindView(R.id.transportType)
    CommonInputView transportType;
    @BindView(R.id.carrierModel)
    CommonInputView carrierModel;
    @BindView(R.id.carrierLength)
    CommonInputView carrierLength;
    @BindView(R.id.unit_price)
    CommonInputView unitPrice;

    private int transport_type = -1;//运输方式

    public BlockTransport(ViewStub viewStub, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView switchDelegate) {
        super(viewStub, "运输信息", dictItemDialog, switchDelegate);
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {

        unitPrice.setText(source.getOrign_unit_amt());

        {
            DictionaryItem di = source.getTransportTypeAsDI();
            if (di != null)
                onItemSelected(di, R.id.transportType);
        }
        {
            DictionaryItem di = source.getMultiTransportFlagAsDI();
            if (di != null)
                onItemSelected(di, R.id.multiTransportFlag);
        }
        {
            DictionaryItem di = source.getCarrierModelAsDI();
            if (di != null)
                onItemSelected(di, R.id.carrierModel);
        }
        {
            DictionaryItem di = source.getCarrierLengthAsDI();
            if (di != null)
                onItemSelected(di, R.id.carrierLength);
        }
        transportDays.setText(source.getTransport_days());
        loadDays.setText(source.getStevedorage_days());
    }

    @OnClick({R.id.multiTransportFlag, R.id.transportType, R.id.carrierModel, R.id.carrierLength})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.multiTransportFlag:
                if (transport_type == -1) {
                    showMessage("请先添加货物");
                    return;
                }
                showSelector("multiTransportFlag", getMultiTransportTitle(), view.getId());
                break;
            case R.id.transportType:
                showSelector("transportTypeEnum", "运输方式", view.getId());
                break;
            case R.id.carrierModel:
                showSelector(getModelKey(), getModelTitle(), view.getId());
                break;
            case R.id.carrierLength:
                showSelector("truckCarrierLengthTypeEnum", "车长要求", view.getId());
                break;
        }
    }

    @Override
    public List<DictionaryItem> prefetchItems() {
        List<DictionaryItem> list = new ArrayList<>();
        if (getBillingType() != 3) {//非自主交易
            if (multiTransportFlag.getTag() == null)
                list.add(new DictionaryItem(String.valueOf(R.id.multiTransportFlag), "multiTransportFlag"));
        } else {//自主交易
            if (transportType.getTag() == null)
                list.add(new DictionaryItem(String.valueOf(R.id.transportType), "transportTypeEnum"));
            if (carrierModel.getTag() == null)
                list.add(new DictionaryItem(String.valueOf(R.id.carrierModel), getModelKey()));
            if (carrierLength.getTag() == null)
                list.add(new DictionaryItem(String.valueOf(R.id.carrierLength), "truckCarrierLengthTypeEnum"));
        }
        return list;
    }

    @Override
    boolean isBlockSatisfied() {
        boolean r = multiTransportFlag.getTag() != null;
        if (r) {
            if (getBillingType() == 3) {//自主交易
                r = transportType.getTag() != null && carrierModel.getTag() != null;
                if (transport_type == 1)
                    r &= carrierLength.getTag() != null;
            } else {//非自主交易
                r = transportDays.inputHaveValue();
            }
        }
        return r;
    }

    @Override
    public String getErrorMessage() {
        if (multiTransportFlag.getTag() == null)
            return "请选择是否整车(船)运输";
        if (getBillingType() == 3) {
            if (transportType.getTag() == null)
                return "请选择运输方式";
            if (carrierModel.getTag() == null)
                return "请选择车辆/船舶类型";
            if (transport_type == 1 && carrierLength.getTag() == null)
                return "请选择车辆长度";
        } else {
            if (!transportDays.inputHaveValue())
                return "请填写运输期限";
        }
        return null;
    }

    @Override
    public void getBlockParams(HashMap<String, String> params) {
        params.put("multi_transport_flag", getTag(multiTransportFlag).getId());
        params.put("transport_days", transportDays.getInputValue());//运输天数
        params.put("stevedorage_days", loadDays.getInputValue());//装卸货天数（船舶）
        params.put("orign_unit_amt",unitPrice.getInputValue());//运费单价
    }

    @Override
    public void getSelfTradeBlockParams(HashMap<String, String> params) {
        params.put("multiTransportFlag", getTag(multiTransportFlag).getId());
        params.put("transportType", getTag(transportType).getId());
        params.put("carrierModelType", getTag(carrierModel).getId());
        DictionaryItem di = getTag(carrierLength);
        if (di != null)
            params.put("carrierLengthType", di.getId());
    }

    @Override
    public void fillSource(Source source) {
        source.setMultiTransportFlag((DictionaryItem) multiTransportFlag.getTag());
        if (getBillingType() != 3){
            source.setOrign_unit_amt(unitPrice.inputHaveValue() ? Double.parseDouble(unitPrice.getInputValue()) : 0);
            source.setTransport_days(transportDays.getInputValue());
            source.setStevedorage_days(getIntValue(loadDays));
        }else {
            source.setTransportType((DictionaryItem) transportType.getTag());
            source.setCarrierModel((DictionaryItem) carrierModel.getTag());
            source.setCarrierLength((DictionaryItem) carrierLength.getTag());
        }
    }

    @Override
    void billingTypeChangedInternal(int billingType) {//开票方式发生了变化
        super.billingTypeChangedInternal(billingType);
        loadDays.setVisibility(View.GONE);
        if (billingType != 3) {//非自主交易
            //清空数据，隐藏View，清空数据是很有必要的
            clear(transportType);
            clear(carrierModel);
            clear(carrierLength);

            transportDays.setVisibility(View.VISIBLE);
            transportType.setVisibility(View.GONE);
            carrierModel.setVisibility(View.GONE);
            carrierLength.setVisibility(View.GONE);
            unitPrice.setVisibility(View.VISIBLE);
        } else {
            clear(transportDays);
            transportDays.setVisibility(View.GONE);
            transportType.setVisibility(View.VISIBLE);
            carrierModel.setVisibility(View.VISIBLE);
            carrierLength.setVisibility(View.VISIBLE);
            unitPrice.setVisibility(View.GONE);
        }
        childViewVisibilityChanged();
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        CommonInputView civ = (CommonInputView) findViewById(extra);
        civ.setTag(item);
        civ.setText(item.getText());
        //运输方式发生了变化
        if (extra == R.id.transportType) {
            transport_type = item.getIdAsInt();
            notifyItemChanged("transportTypeEnum", item);//发出通知
            //只有自主交易才会在这里选择运输方式
            //为什么不在onBlockDictItemChanged函数里面去做处理？
            //在广播的过程中把自己给去掉了，见上面的dictChanged函数
            if (getBillingType() == 3) {
                if ("2".equals(item.getId())) {//船舶没有长度选择，而车辆有
                    clear(carrierLength);
                    setViewVisibility(carrierLength, View.GONE);
                } else {
                    setViewVisibility(carrierLength, View.VISIBLE);
                }
            }
            carrierModel.setLabel(getModelTitle());
            carrierModel.setTag(null);
            carrierModel.setText(null);
        }
    }

    //这里接收到的是其他Block发出的运输方式发生了变化（非自主交易模式，选择货物，货物里面有运输方式），
    // 不是这个block里面选择运输方式发出的通知
    @Override
    public void onBlockFieldChanged(BaseBlock block, String dictType, Object object) {
        if ("transportTypeEnum".equals(dictType)) {//运输方式发生了变化
            DictionaryItem di = (DictionaryItem) object;
            transport_type = di.getIdAsInt();
            multiTransportFlag.setLabel(getMultiTransportTitle());
            if ("2".equals(di.getId())) {//非自主交易的船舶有装卸货天数
                setViewVisibility(loadDays, View.VISIBLE);
            } else {
                setViewVisibility(loadDays, View.GONE);
            }
        }else if ("cargo_unit".equals(dictType)){
            unitPrice.setSuffixText((String) object);
        }
    }

    private String getMultiTransportTitle() {
        if (transport_type == 1) {
            return "整车运输";
        } else if (transport_type == 2) {
            return "整船运输";
        }
        return "";
    }

    private String getModelKey() {
        if (transport_type == 2) {
            return "shipCarrierModelTypeEnum";
        }
        return "truckCarrierModelTypeEnum";
    }

    private String getModelTitle() {
        if (transport_type == 2) {
            return "船型要求";
        }
        return "车型要求";
    }
}
