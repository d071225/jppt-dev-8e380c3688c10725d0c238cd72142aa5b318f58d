package com.hletong.hyc.ui.activity.truck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.TruckCompleteInfo;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.util.StringUtil;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTruckInfoActivity extends BaseActivity implements BottomSelectorDialog.OnItemSelectedListener<DictionaryItem> {
    @BindViews({R.id.vehicle_type/*车辆类型*/, R.id.color_type/*车辆颜色*/, R.id.sex_type/*驾驶人性别*/})
    TextView[] tvDicts;
    @BindView(R.id.vehicle_length)
    EditText et_vehicleLength;
    @BindView(R.id.et_vehicleMass)
    EditText et_vehicleMass;
    @BindView(R.id.et_loadedVehicleQuality)
    EditText et_loadedVehicleQuality;
    private DictItemDialog mDictItemFragment;
    private TruckCompleteInfo mTruckInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_truck_info;
    }

    @Override
    public void initData() {
        super.initData();
        mTruckInfo = getSerializable(TruckCompleteInfo.class.getSimpleName());
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.vehicle_info);
        mDictItemFragment = new DictItemDialog(this);
        mDictItemFragment.setOnItemSelected(this);
        if (mTruckInfo != null) {
            tvDicts[0].setText(mTruckInfo.getTruck_type().getText());
            tvDicts[1].setText(mTruckInfo.getTruck_color().getText());
            tvDicts[2].setText(mTruckInfo.getSexType().getText());
            et_vehicleLength.setText(mTruckInfo.getTruckLength());
            et_vehicleMass.setText(mTruckInfo.getVehicleMass());
            et_loadedVehicleQuality.setText(mTruckInfo.getLoadedVehicleQuality());
        } else {
            mTruckInfo = new TruckCompleteInfo();
        }
    }

    @OnClick({R.id.vehicle_type, R.id.color_type, R.id.sex_type, R.id.submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vehicle_type:
                mDictItemFragment.showDict("truck_type", "请选择车辆类型", 0);
                break;
            case R.id.color_type:
                mDictItemFragment.showDict("plate_color", "请选择车牌颜色", 1);
                break;
            case R.id.sex_type:
                mDictItemFragment.showDict("sex", "请选择驾驶员性别", 2);
                break;
            case R.id.submit:
                if (validate()) {
                    mTruckInfo.setTruckLength(getTextValue(et_vehicleLength));
                    mTruckInfo.setLoadedVehicleQuality(getTextValue(et_loadedVehicleQuality));
                    mTruckInfo.setVehicleMass(getTextValue(et_vehicleMass));
                    Intent intent = new Intent();
                    intent.putExtra(TruckCompleteInfo.class.getSimpleName(), mTruckInfo);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        tvDicts[extra].setText(item.getValue());
        if (extra == 0) {
            mTruckInfo.setTruck_type(item);
        } else if (extra == 1) {
            mTruckInfo.setTruck_color(item);
        } else if (extra == 2) {
            mTruckInfo.setSexType(item);
        }
    }

    private boolean validate() {
        boolean isOK = false;
        if (mTruckInfo.getTruck_type() == null) {
            showMessage("请选择车辆类型");
        } else if (mTruckInfo.getTruck_color() == null) {
            showMessage("请选择车牌颜色");
        } else if (mTruckInfo.getSexType() == null) {
            showMessage("请选择驾驶员性别");
        } else if (StringUtil.isTrimBlank(getTextValue(et_vehicleLength))) {
            showMessage("请输入车厢长度");
        } else if (StringUtil.isTrimBlank(getTextValue(et_vehicleMass))) {
            showMessage("请输入车辆载质量");
        } else if (StringUtil.isTrimBlank(getTextValue(et_loadedVehicleQuality))) {
            showMessage("请输入满载车辆质量");
        } else {
            isOK = true;
        }
        return isOK;
    }
}
