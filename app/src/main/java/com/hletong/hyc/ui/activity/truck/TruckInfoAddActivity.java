package com.hletong.hyc.ui.activity.truck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.TruckCompleteInfo;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TruckInfoAddActivity extends BaseActivity implements BottomSelectorDialog.OnItemSelectedListener<DictionaryItem> {
    @BindViews({R.id.cv_truckType/*车辆类型*/, R.id.cv_truckColor/*车辆颜色*/, R.id.cv_sexType/*驾驶人性别*/})
    CommonInputView[] tvDicts;
    @BindView(R.id.cv_truckLength)
    CommonInputView et_vehicleLength;
    //车辆载重量
    @BindView(R.id.cv_et_vehicleMass)
    CommonInputView et_vehicleMass;

    //车辆识别代码
    @BindView(R.id.cv_sbdhNumber)
    CommonInputView cv_sbdhNumber;
    //满载车辆重量
    @BindView(R.id.cv_et_loadedVehicleQuality)
    CommonInputView et_loadedVehicleQuality;
    //载重吨位
    @BindView(R.id.cv_et_load_ton)
    CommonInputView cv_et_load_ton;
    //从业资格证号
    @BindView(R.id.cv_et_cyzgzh)
    CommonInputView cv_et_cyzgzh;
    //道路运输字号
    @BindView(R.id.cv_et_dlyszh)
    CommonInputView cv_et_dlyszh;


    private DictItemDialog mDictItemFragment;
    private TruckCompleteInfo mTruckInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_truck_info_add;
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
        mDictItemFragment = new DictItemDialog(this) {
            @Override
            public void showList(List<DictionaryItem> list, boolean refresh) {
                //车辆类型过滤不限
                for (DictionaryItem item : list) {
                    if (item.getText().contains("不限")) {
                        list.remove(item);
                        break;
                    }
                }
                super.showList(list, refresh);
            }
        };
        mDictItemFragment.setOnItemSelected(this);
        if (mTruckInfo != null) {
            if (mTruckInfo.getTruck_type() != null) {
                tvDicts[0].setText(mTruckInfo.getTruck_type().getText());
            }
            if (mTruckInfo.getTruck_color() != null) {
                tvDicts[1].setText(mTruckInfo.getTruck_color().getText());

            }
            if (mTruckInfo.getSexType() != null) {
                tvDicts[2].setText(mTruckInfo.getSexType().getText());
            }
            et_vehicleLength.setText(mTruckInfo.getTruckLength());
            et_vehicleMass.setText(mTruckInfo.getVehicleMass());
            et_loadedVehicleQuality.setText(mTruckInfo.getLoadedVehicleQuality());
            cv_et_load_ton.setText(mTruckInfo.getLoadTon());
            cv_et_cyzgzh.setText(mTruckInfo.getCyzgzNumber());
            cv_et_dlyszh.setText(mTruckInfo.getDlysNumber());
            cv_sbdhNumber.setText(mTruckInfo.getSbdhNumber());
        } else {
            mTruckInfo = new TruckCompleteInfo();
        }

    }

    @OnClick({R.id.cv_truckType, R.id.cv_truckColor, R.id.cv_sexType, R.id.submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_truckType:
                mDictItemFragment.showDict("truck_type", "请选择车辆类型", 0);
                break;
            case R.id.cv_truckColor:
                mDictItemFragment.showDict("plate_color", "请选择车牌颜色", 1);
                break;
            case R.id.cv_sexType:
                mDictItemFragment.showDict("sex", "请选择驾驶员性别", 2);
                break;
            case R.id.submit:
                if (validate()) {
                    mTruckInfo.setTruckLength(et_vehicleLength.getInputValue());
                    mTruckInfo.setLoadedVehicleQuality(et_loadedVehicleQuality.getInputValue());
                    mTruckInfo.setVehicleMass(et_vehicleMass.getInputValue());
                    mTruckInfo.setLoadTon(cv_et_load_ton.getInputValue());
                    mTruckInfo.setCyzgzNumber(cv_et_cyzgzh.getInputValue());
                    mTruckInfo.setDlysNumber(cv_et_dlyszh.getInputValue());
                    mTruckInfo.setSbdhNumber(cv_sbdhNumber.getInputValue());

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
        } else if (StringUtil.isTrimBlank(cv_sbdhNumber.getInputValue())) {
            showMessage("请输入车辆识别代号");
        } else if (StringUtil.isTrimBlank(et_vehicleLength.getInputValue())) {
            showMessage("请输入车厢长度");
        } else if (StringUtil.isTrimBlank(et_vehicleMass.getInputValue())) {
            showMessage("请输入车辆载质量");
        } else if (StringUtil.isTrimBlank(et_loadedVehicleQuality.getInputValue())) {
            showMessage("请输入满载车辆质量");
        } else if (StringUtil.isTrimBlank(cv_et_load_ton.getInputValue())) {
            showMessage("请输入载重吨位");
        } else if (StringUtil.isTrimBlank(cv_et_cyzgzh.getInputValue())) {
            showMessage("请输入从业资格证号");
        } else if (StringUtil.isTrimBlank(cv_et_dlyszh.getInputValue())) {
            showMessage("请输入道路运输字号");
        } else {
            isOK = true;
        }
        return isOK;
    }
}
