package com.hletong.hyc.ui.activity.truck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.StringUtil;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hletong.hyc.model.validate.truck.RegisterPersonalTruckInfo.TruckInfo;

public class RegisterTruckInfoActivity extends BaseActivity implements BottomSelectorDialog.OnItemSelectedListener<DictionaryItem> {
    @BindViews({R.id.vehicle_type/*车辆类型*/, R.id.tandemAxle/*拖挂轴数*/, R.id.vehicleAxle/*车辆轴数*/})
    TextView[] tvDicts;
    @BindView(R.id.vehicle_length)
    EditText vehicleLength;
    @BindView(R.id.vehicle_width)
    EditText vehicleWidth;
    @BindView(R.id.breadHeight)
    EditText breadHeight;
    @BindView(R.id.maxQuantity)
    EditText maxQuantity;
    private DictItemDialog mDictItemFragment;
    private TruckInfo mTruckInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_truck_info;
    }

    @Override
    public void initData() {
        super.initData();
        mTruckInfo = getSerializable(TruckInfo.class.getSimpleName());
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
            tvDicts[1].setText(mTruckInfo.getTrailerAxle().getText());
            tvDicts[2].setText(mTruckInfo.getAxleCount().getText());
            vehicleLength.setText(mTruckInfo.getMax_length());
            vehicleWidth.setText(mTruckInfo.getMax_width());
            breadHeight.setText(mTruckInfo.getMax_height());
            maxQuantity.setText(mTruckInfo.getMax_heavy());
        } else {
            mTruckInfo = new TruckInfo();
        }
    }

    @OnClick({R.id.vehicle_type, R.id.tandemAxle, R.id.vehicleAxle, R.id.submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vehicle_type:
                mDictItemFragment.showDict( "truck_type", "请选择车辆类型", 0);
                break;
            case R.id.tandemAxle:
                mDictItemFragment.showDict("trailer_axle", "请选择拖挂轮轴", 1);
                break;
            case R.id.vehicleAxle:
                mDictItemFragment.showDict( "axle_count", "请选择车辆轴数", 2);
                break;
            case R.id.submit:
                if (validate()) {
                    mTruckInfo.setMax_length(getTextValue(vehicleLength));
                    mTruckInfo.setMax_width(getTextValue(vehicleWidth));
                    mTruckInfo.setMax_height(getTextValue(breadHeight));
                    mTruckInfo.setMax_heavy(getTextValue(maxQuantity));
                    Intent intent = new Intent();
                    intent.putExtra(TruckInfo.class.getSimpleName(), mTruckInfo);
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
            mTruckInfo.setTrailerAxle(item);
        } else if (extra == 2) {
            mTruckInfo.setAxleCount(item);
        }
    }

    private boolean validate() {
        boolean isOK = false;
        if (mTruckInfo.getTruck_type() == null) {
            showMessage("请选择车辆类型");
        } else if (mTruckInfo.getTrailerAxle() == null) {
            showMessage("请选择拖挂轮轴");
        } else if (mTruckInfo.getAxleCount() == null) {
            showMessage("请选择车辆轴数");
        } else if (StringUtil.isTrimBlank(getTextValue(vehicleLength))) {
            showMessage("请输入车厢长度");
        } else if (StringUtil.isTrimBlank(getTextValue(vehicleWidth))) {
            showMessage("请输入车厢宽度");
        } else if (StringUtil.isTrimBlank(getTextValue(breadHeight))) {
            showMessage("请输入栏板高度");
        } else if (StringUtil.isTrimBlank(getTextValue(maxQuantity))) {
            showMessage("请输入最大配载量");
        } else {
            isOK = true;
        }
        return isOK;
    }
}
