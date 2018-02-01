package com.hletong.hyc.ui.activity.cargoforecast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.CargoForecastBaseSelectFragment;
import com.hletong.hyc.ui.fragment.SelectCargoFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/22.
 * 非自主交易货源详情
 */

public class BlockCargoNoneSelfTrade extends BaseBlock {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.measure_type)
    TextView measureType;
    @BindView(R.id.transport_loss)
    TextView transportLoss;
    @BindView(R.id.volume)
    TextView volume;
    @BindView(R.id.cargoTotalValue)
    CommonInputView cargoTotalValue;
    @BindView(R.id.bookRefType)
    CommonInputView bookRefType;
    @BindView(R.id.cargoNumber)
    CommonInputView cargoNumber;
    @BindView(R.id.cargoWeight)
    CommonInputView cargoWeight;
    @BindView(R.id.gallery)
    PickerRecyclerView mRecyclerView;
    @BindView(R.id.unit)
    TextView unit;
    @BindView(R.id.carrier_model)
    TextView carrierModel;
    @BindView(R.id.carrier_length)
    TextView carrierLength;
    @BindView(R.id.hint)
    TextView hint;

    private Source mCargo;

    public BlockCargoNoneSelfTrade(ViewStub cargoInfoNoneSelfTrade, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView delegate) {
        super(cargoInfoNoneSelfTrade, "货物信息", dictItemDialog, delegate);
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        if (source.getCargo_price() != 0)
            cargoTotalValue.setText(source.getCargo_price() / 10000);//单位是万元
        if (source.getUnit_ct() != 0)
            cargoNumber.setText(source.getUnit_ct());
        if (source.getWeight() != 0)
            cargoWeight.setText(source.getWeight());

        DictionaryItem brt = source.getBookRefTypeAsDictionaryItem();

        if (brt != null) {
            onItemSelected(brt, -1);
        }
        //模拟用户选择货源返回结果
        Intent intent = new Intent();
        intent.putExtra("data", source);
        onActivityResult(188, intent);
    }

    @Override
    public boolean canSaveToDraft() {
        return mCargo != null;
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        if (billingType == 3) {
            hideBlock();
        } else {
            showBlock(true);
        }
    }

    @OnClick({R.id.cargo, R.id.bookRefType})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cargo:
                startActivityForResult(CommonWrapFragmentActivity.class, 188,
                        CommonWrapFragmentActivity.getBundle("选择货物",
                                SelectCargoFragment.class, CargoForecastBaseSelectFragment.getDefaultBundle(false, -1, "添加货物", "管理货物")));
                break;
            case R.id.bookRefType:
                showSelector("bookRefTypeEnum", "摘牌依据", 2);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == 188) {
            hint.setVisibility(View.GONE);//隐藏提示框
            mCargo = data.getParcelableExtra("data");
            cargoNumber.getSuffix().setText(mCargo.getCargoUnit());
            if ("00".equals(mCargo.getUnits()) || "02".equals(mCargo.getUnits())) {
                //吨和立方，可以输入小数
                cargoNumber.setInputType(CommonInputView.NUMBER_DECIMAL);
            } else {
                cargoNumber.setInputType(CommonInputView.NUMBER);
            }
            title.setText(mCargo.getOrgin_cargon_kind_name());
            measureType.setText("计量方式：" + mCargo.getMeasure_type());
            transportLoss.setText("运输损耗：" + mCargo.getMaxWastageRtWithUnit());
            volume.setText("长/宽/高：" + mCargo.getVolume("/"));
            unit.setText("单位：" + mCargo.getCargoUnit());
            carrierLength.setVisibility(View.INVISIBLE);
            if (1 == mCargo.getTransport_type()) {
                carrierLength.setVisibility(View.VISIBLE);
                carrierModel.setText("车型要求：" + mCargo.getCarrier_model_type_());
                carrierLength.setText("车长要求：" + (mCargo.getCarrier_length_type_() == null ? "无" : mCargo.getCarrier_length_type_()));
            } else if (2 == mCargo.getTransport_type()) {
                carrierModel.setText("船型要求：" + mCargo.getCarrier_model_type_());
            }
            //发出通知，承运信息发生了变化
            notifyItemChanged("transportTypeEnum", new DictionaryItem(String.valueOf(mCargo.getTransport_type()), mCargo.getTransportType()));
            setViewVisibility(mRecyclerView, View.GONE);
            if (mCargo.getCargoFileId() != null) {
                getBlockAction().downloadImage(mCargo.getCargoFileId(), this);
            }
            //通知单位发生了变化
            notifyUnitChanged();
        }
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        bookRefType.setTag(item);
        bookRefType.setText(item.getText());
        //通知外部单位发生了改变
        notifyUnitChanged();
    }

    //通知外部单位发生了改变
    private void notifyUnitChanged() {
        if (bookRefType.getTag() != null) {
            DictionaryItem di = (DictionaryItem) bookRefType.getTag();
            if (di.getText().equals("重量")) {
                notifyItemChanged("cargo_unit", "元/吨");
            }else if (mCargo != null)
                notifyItemChanged("cargo_unit", "元/" + mCargo.getUnits_());
        }
    }

    @Override
    boolean isBlockSatisfied() {
        if (mCargo == null)
            return false;

        boolean r = cargoTotalValue != null && cargoTotalValue.inputHaveValue();
        if (mCargo.getBook_ref_type() == 0) {//以重量计费
            r &= cargoWeight.inputHaveValue();
        } else {//以数量计费
            r &= cargoNumber.inputHaveValue() && cargoNumber.getSuffix().getTag() != null;
        }
        return r;
    }

    @Override
    public String getErrorMessage() {
        if (mCargo == null)
            return "货物未选择";
        DictionaryItem brt = (DictionaryItem) bookRefType.getTag();
        if (brt == null)
            return "摘牌依据未选择";
        if (!cargoTotalValue.inputHaveValue())
            return "请填写货物总价值";
        if ("重量".equals(brt.getText())) {
            if (!cargoWeight.inputHaveValue())
                return "请填写货物重量";
        } else {
            if (!cargoNumber.inputHaveValue())
                return "请填写货物数量";
        }
        return null;
    }

    @Override
    public void getBlockParams(HashMap<String, String> params) {
        params.put("orgin_cargon_kind_name", mCargo.getOrgin_cargon_kind_name());
        params.put("measure_type", mCargo.getMeasureType());
        params.put("units", mCargo.getUnits());
        params.put("units_", mCargo.getUnits_());
//        params.put("orign_unit_amt", String.valueOf(mCargo.getOrign_unit_amt()));
        params.put("length", mCargo.getLength());
        params.put("width", mCargo.getWidth());
        params.put("height", mCargo.getHeight());
        params.put("transport_type", String.valueOf(mCargo.getTransport_type()));
        params.put("carrier_model_type", mCargo.getCarrier_model_type());
        params.put("carrier_length_type", mCargo.getCarrier_length_type());
        params.put("max_wastage_rt", String.valueOf(mCargo.getMax_wastage_rt()));
        params.put("cargo_file_id", mCargo.getCargoFileId());
        params.put("book_ref_type", getTag(bookRefType).getId());
        params.put("cargo_price", String.valueOf(getValue(cargoTotalValue) * 10000));//单位是万元
        params.put("unit_ct", cargoNumber.getInputValue());
        params.put("weight", cargoWeight.getInputValue());
    }

    @Override
    public void fillSource(Source source) {
        if (mCargo != null) {
            source.setOrgin_cargon_kind_name(mCargo.getOrgin_cargon_kind_name());
            source.setMeasureType(new DictionaryItem(mCargo.getMeasureType(), mCargo.getMeasure_type()));
            source.setUnits(mCargo.getUnits(), mCargo.getUnits_());
            source.setVolumn(mCargo);
            source.setTransportType(new DictionaryItem(String.valueOf(mCargo.getTransport_type()), mCargo.getTransportType()));
            source.setCarrierModel(new DictionaryItem(mCargo.getCarrier_model_type(), mCargo.getCarrier_model_type_()));
            source.setCarrierLength(new DictionaryItem(mCargo.getCarrier_length_type(), mCargo.getCarrier_length_type_()));
            source.setMax_wastage_rt(mCargo.getMax_wastage_rt());
            source.setCargo_file_id(mCargo.getCargoFileId());
        }
        source.setBookRefType((DictionaryItem) bookRefType.getTag());
        source.setCargo_price(getValue(cargoTotalValue) * 10000);
        source.setUnit_ct(getValue(cargoNumber));
        source.setWeight(getValue(cargoWeight));
    }

    @Override
    public List<DictionaryItem> prefetchItems() {
        List<DictionaryItem> list = new ArrayList<>();
        //没有数据时才需要预加载
        if (bookRefType.getTag() == null)
            list.add(new DictionaryItem("1", "bookRefTypeEnum"));
        return list;
    }

    public void imageDownloadSucceed(List<String> images) {
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int oldHeight = oldBottom - oldTop;
                int newHeight = bottom - top;
                if (oldHeight != newHeight) {
                    childViewVisibilityChanged();
                    mRecyclerView.removeOnLayoutChangeListener(this);
                }
                Logger.d("oldHeight => " + oldHeight + ", newHeight => " + newHeight);
            }
        });
        new PickerRecyclerView.Builder((Activity) getContext()).selectedPhotos(new ArrayList<>(images)).action(PreViewBuilder.PREVIEW).build(mRecyclerView);
        setViewVisibility(mRecyclerView, View.VISIBLE);
    }
}
