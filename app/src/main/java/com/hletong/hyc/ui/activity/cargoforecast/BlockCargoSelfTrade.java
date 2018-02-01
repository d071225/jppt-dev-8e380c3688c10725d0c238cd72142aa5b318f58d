package com.hletong.hyc.ui.activity.cargoforecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.text.Editable;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.SimpleTextWatcher;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ddq on 2017/3/22.
 * 自主交易货源详情
 */

public class BlockCargoSelfTrade extends BaseBlock {
    @BindView(R.id.cargoName)
    CommonInputView cargoName;
    @BindView(R.id.cargoOwner)
    CommonInputView cargoOwner;
    @BindView(R.id.cargoVolume)
    CommonInputView cargoVolume;
    @BindView(R.id.cargoCount)
    CommonInputView cargoCount;

    private EditText length;
    private EditText width;
    private EditText height;
    private RegisterPhoto mPhotos;

    public BlockCargoSelfTrade(ViewStub cargoInfoSelfTrade, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView switchDelegate) {
        super(cargoInfoSelfTrade, "货物信息", dictItemDialog, switchDelegate);
    }

    @Override
    void viewInflated(View view) {
        mPhotos = new RegisterPhoto(new String[]{"", "", ""}, 0.75f);
        mPhotos.setFullAdd(false);
        mPhotos.setUpLoadUrl(Constant.getUrl(Constant.IMAGE_UPLOAD));

        cargoName.getSuffix().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(RegisterPhoto.class.getSimpleName(), mPhotos);
                startActivityForResult(UploadPhotoActivity.class, 999, bundle);
            }
        });
        cargoCount.getSuffix().setOnClickListener(this);
        cargoCount.getInput().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                notifyItemChanged("self_trade_cargo_ct", s.toString());
            }
        });
        length = (EditText) cargoVolume.findViewById(R.id.length);
        width = (EditText) cargoVolume.findViewById(R.id.width);
        height = (EditText) cargoVolume.findViewById(R.id.height);
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        cargoName.setText(source.getOrgin_cargon_kind_name());
        cargoOwner.setText(source.getCargo_owner_sname());
        length.setText(source.getLength());
        width.setText(source.getWidth());
        height.setText(source.getHeight());
        if (source.getBook_ref_type() == 0) {
            cargoCount.setText(source.getWeight());
        } else {
            cargoCount.setText(source.getUnit_ct());
        }
        onItemSelected(new DictionaryItem(source.getUnits(), source.getUnits_()), -1);
        mPhotos.setFileGroupId(source.getCargoFileId());
//        getBlockAction().downloadImage(mPhotos.getFileGroupId(), this);
    }

    @Override
    public boolean canSaveToDraft() {
        return cargoName.inputHaveValue();
    }

    @Override
    public void onClick(View v) {
        showSelector("unitsEnum", "选择货物单位", -1);
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        if (billingType == 3) {//自主交易
            showBlock(true);
        } else {
            hideBlock();
        }
    }

    @Override
    public void onBlockFieldChanged(BaseBlock block, String dictType, Object object) {
        //运费信息里面的单价发生了变化，这里收到之后，发出价格发生变化的广播，好计算运费信息里面的总价
        if ("self_trade_price".equals(dictType)) {
            notifyItemChanged("self_trade_cargo_ct", cargoCount.getInputValue());
        }
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        mPhotos = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
        getBlockAction().downloadImage(mPhotos.getFileGroupId(), this);
    }

    @Override
    boolean isBlockSatisfied() {
        return cargoName.inputHaveValue()
                && cargoCount.inputHaveValue()
                && cargoCount.getSuffix().getTag() != null;
    }

    @Override
    public String getErrorMessage() {
        if (!cargoName.inputHaveValue()) {
            return "货物品名未填写";
        }

        if (!cargoCount.inputHaveValue()) {
            return "货物数量未填写";
        }

        if (cargoCount.getSuffix().getTag() == null) {
            return "货物单位未选择";
        }
        return null;
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        cargoCount.getSuffix().setTag(item);
        cargoCount.getSuffix().setText(item.getText());
        if ("00".equals(item.getId()) || "02".equals(item.getId())) {
            //吨和立方，可以输入小数
            cargoCount.setInputType(CommonInputView.NUMBER_DECIMAL);
        } else {
            cargoCount.setInputType(CommonInputView.NUMBER);
        }
        notifyItemChanged("unitsEnum", item);
    }

    @Override
    public List<DictionaryItem> prefetchItems() {
        List<DictionaryItem> list = new ArrayList<>();
        if (cargoCount.getSuffix().getTag() == null)
            list.add(new DictionaryItem("-1", "unitsEnum"));
        return list;
    }

    @Override
    public void fillSource(Source source) {
        source.setOrgin_cargon_kind_name(cargoName.getInputValue());
        source.setCargo_owner_sname(cargoOwner.getInputValue());
        source.setLength(getValue(length));
        source.setWidth(getValue(width));
        source.setHeight(getValue(height));
        DictionaryItem di = (DictionaryItem) cargoCount.getSuffix().getTag();
        if (di == null) {
            di = new DictionaryItem("00", "吨");
        }
        source.setUnits(di.getId(), di.getText());
        if ("00".equals(di.getId())) {
            source.setBookRefType(new DictionaryItem("0", "重量"));
            source.setWeight(getValue(cargoCount.getInput()));
        } else {
            source.setBookRefType(new DictionaryItem("1", "数量"));
            source.setUnit_ct(getValue(cargoCount.getInput()));
        }
    }

    @Override
    public void getSelfTradeBlockParams(HashMap<String, String> params) {
        params.put("orginCargonKindName", cargoName.getInputValue());
        params.put("cargoRealOwner", cargoOwner.getInputValue());
        params.put("length", length.getText().toString());
        params.put("width", width.getText().toString());
        params.put("height", height.getText().toString());
        if (mPhotos != null)
            params.put("cargoFileId", mPhotos.getFileGroupId());

        DictionaryItem di = (DictionaryItem) cargoCount.getSuffix().getTag();
        params.put("units", di.getId());
        params.put("units_", di.getValue());
        if ("吨".equals(di.getText())) {
            params.put("bookRefType", "0");
            params.put("weight", cargoCount.getInputValue());
            params.put("unitCt", "0");
        } else {
            params.put("bookRefType", "1");
            params.put("unitCt", cargoCount.getInputValue());
            params.put("weight", "0");
        }
    }

    public void imageDownloadSucceed(List<String> images) {
        Glide.with(getContext()).load(images.get(0)).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                resource.setBounds(0, 0, 44, 44);
                TextViewCompat.setCompoundDrawablesRelative(cargoName.getSuffix(), null, null, resource, null);
            }
        });
    }
}
