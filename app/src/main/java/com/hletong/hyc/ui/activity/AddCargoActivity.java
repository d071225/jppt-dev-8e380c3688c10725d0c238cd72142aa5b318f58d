package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoContract;
import com.hletong.hyc.model.Cargo;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.validate.CargoInfo;
import com.hletong.hyc.presenter.CargoPresenter;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.dialog.selector.SelectorPrefetchListener;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.xcheng.view.processbtn.ProcessButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/10.
 * 货源预报-添加货物信息
 */

public class AddCargoActivity extends ImageSelectorActivityNew<CargoContract.Presenter> implements CargoContract.View {
    @BindView(R.id.cargoName)
    CommonInputView mCargoName;
    @BindView(R.id.measureType)
    CommonInputView mMeasureType;
    @BindView(R.id.unit)
    CommonInputView mUnit;
    @BindView(R.id.length)
    CommonInputView mLength;
    @BindView(R.id.width)
    CommonInputView mWidth;
    @BindView(R.id.height)
    CommonInputView mHeight;
    @BindView(R.id.primary)
    PickerRecyclerView mPrimary;
    @BindView(R.id.secondary)
    View mSecondary;
    @BindView(R.id.picker)
    PickerRecyclerView mPicker;

    @BindView(R.id.waste_rt)
    CommonInputView mWasteRt;
    @BindView(R.id.transporter_type)
    CommonInputView mTransporterType;
    @BindView(R.id.transporter_model)
    CommonInputView mTransporterModel;
    @BindView(R.id.transporter_length)
    CommonInputView mTransporterLength;
    @BindView(R.id.submit)
    ProcessButton processSubmit;

    private DictItemDialog mDictItemDialog;

    private BottomSelectorDialog.OnItemSelectedListener<DictionaryItem> mSelectedListener = new SelectorPrefetchListener<DictionaryItem>() {
        @Override
        public void onItemSelected(DictionaryItem item, int extra) {
            CommonInputView civ = (CommonInputView) findViewById(extra);
            civ.setText(item.getText());
            getPresenter().itemChanged(item, extra);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_cargo;
    }

    public ProcessButton getProcessSubmit() {
        return processSubmit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mDictItemDialog = new DictItemDialog(this, Constant.getUrl(Constant.CARGO_FORECAST_DICTIONARY));
        mDictItemDialog.setOnItemSelected(mSelectedListener);
        getPresenter().start();
    }

    @OnClick({R.id.measureType, R.id.unit, R.id.transporter_type, R.id.transporter_length, R.id.transporter_model, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.measureType:
                showSelector("measureTypeEnum", view.getId());
                break;
            case R.id.unit:
                showSelector("unitsEnum", view.getId());
                break;
            case R.id.transporter_type:
                showSelector("transportTypeEnum", view.getId());
                break;
            case R.id.transporter_length:
                showSelector("truckCarrierLengthTypeEnum", view.getId());
                break;
            case R.id.transporter_model:
                getPresenter().selectTransportModel();
                break;
            case R.id.submit:
                getPresenter().submit(
                        new CargoInfo(
                                mCargoName.getInputValue(),
                                mLength.getInputValue(),
                                mWidth.getInputValue(),
                                mHeight.getInputValue(),
                                mWasteRt.getInputValue()),
                        mPicker.getVisibility() == View.VISIBLE ? mPicker.getPhotos() : mPrimary.getPhotos());
                break;
        }
    }


    public void showSelector(String fieldName, int id) {
        mDictItemDialog.showDict(fieldName, null, id);
    }

    @Override
    public void prefetch(List<DictionaryItem> list) {
        mDictItemDialog.prefetch(list, null);
    }

    @Override
    protected CargoContract.Presenter createPresenter() {
        return new CargoPresenter(this, (Cargo) getParcelable("cargo"));
    }

    @Override
    public void showTitle(String title) {
        setCustomTitle(title);
    }

    @Override
    public void showCargo(String cargoName, String measureType, String unit, String length, String width, String height, String loss, String transportType) {
        mCargoName.setText(cargoName);
        mMeasureType.setText(measureType);
        mUnit.setText(unit);
        mLength.setText(length);
        mWidth.setText(width);
        mHeight.setText(height);
        mWasteRt.setText(loss);
        mTransporterType.setText(transportType);
    }

    @Override
    public void showImages(List<String> list) {
        mPrimary.setVisibility(View.VISIBLE);
        new PickerRecyclerView.Builder(this).selectedPhotos((ArrayList<String>) list).action(PreViewBuilder.PREVIEW).build(mPrimary);
    }

    @Override
    public void showPrimaryPicker() {
        mPrimary.setVisibility(View.VISIBLE);
        new PickerRecyclerView.Builder(this).maxCount(5).action(PreViewBuilder.SELECT).build(mPrimary);
        mPrimary.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector(mPrimary);
            }
        });
    }

    @Override
    public void showSecondPicker() {
        mSecondary.setVisibility(View.VISIBLE);
        mPicker.setVisibility(View.VISIBLE);
        new PickerRecyclerView.Builder(this).maxCount(5).action(PreViewBuilder.SELECT).build(mPicker);
        mPicker.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector(mPicker);
            }
        });
    }

    @Override
    public void showSubmitButton(String text) {
        processSubmit.setVisibility(View.VISIBLE);
        processSubmit.setText(text);
    }

    @Override
    public void showCarrierLength(String length) {
        mTransporterLength.setVisibility(View.VISIBLE);
        mTransporterLength.setText(length);
    }

    @Override
    public void hideCarrierLength() {
        mTransporterLength.setVisibility(View.GONE);
        mTransporterLength.setText(null);
    }

    @Override
    public void showCarrierModel(String model, String label) {
        mTransporterModel.setText(model);
        mTransporterModel.setLabel(label);
    }
}
