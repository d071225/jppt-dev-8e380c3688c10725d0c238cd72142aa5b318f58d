package com.hletong.hyc.ui.activity.ship;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hletong.hyc.model.validate.ship.RegisterPersonalShipInfo.ShipInfo;

public class RegisterShipInfoActivity extends BaseActivity implements BottomSelectorDialog.OnItemSelectedListener<DictionaryItem> {
    //船舶类型
    @BindView(R.id.tvShipType)
    TextView tvShipType;
    @BindView(R.id.ship_length)
    EditText etShipLength;
    @BindView(R.id.ship_width)
    EditText etShipWidth;
    @BindView(R.id.ship_deep)
    EditText etShipDeep;
    //满载吃水
    @BindView(R.id.ship_full_draft)
    EditText etShipFull;
    //载重吨位
    @BindView(R.id.ship_load_ton)
    EditText etShipLoadton;
    private DictItemDialog mDictItemFragment;
    private ShipInfo mShipInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_ship_info;
    }

    @Override
    public void initData() {
        super.initData();
        mShipInfo = getSerializable(ShipInfo.class.getSimpleName());
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.ship_info);
        mDictItemFragment = new DictItemDialog(this);
        mDictItemFragment.setOnItemSelected(this);
        if (mShipInfo != null) {
            tvShipType.setText(mShipInfo.getShip_type().getText());
            etShipLength.setText(mShipInfo.getShip_length());
            etShipWidth.setText(mShipInfo.getShip_width());
            etShipDeep.setText(mShipInfo.getDeep());
            etShipFull.setText(mShipInfo.getFull_draft());
            etShipLoadton.setText(mShipInfo.getLoad_ton());
        } else {
            mShipInfo = new ShipInfo();
        }
    }

    @OnClick({R.id.tvShipType, R.id.submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvShipType:
                mDictItemFragment.showDict( "ship_type", "请选择船舶类型", 0);
                break;
            case R.id.submit:
                if (validate()) {
                    mShipInfo.setShip_length(getTextValue(etShipLength));
                    mShipInfo.setShip_width(getTextValue(etShipWidth));
                    mShipInfo.setDeep(getTextValue(etShipDeep));
                    mShipInfo.setFull_draft(getTextValue(etShipFull));
                    mShipInfo.setLoad_ton(getTextValue(etShipLoadton));
                    Intent intent = new Intent();
                    intent.putExtra(ShipInfo.class.getSimpleName(), mShipInfo);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        tvShipType.setText(item.getText());
        mShipInfo.setShip_type(item);
    }

    private boolean validate() {
        boolean isOK = false;
        if (mShipInfo.getShip_type() == null) {
            showMessage("请选择船舶类型");
        } else if (StringUtil.isTrimBlank(getTextValue(etShipLength))) {
            showMessage("请输入船长");
        } else if (StringUtil.isTrimBlank(getTextValue(etShipWidth))) {
            showMessage("请输入型宽");
        } else if (StringUtil.isTrimBlank(getTextValue(etShipDeep))) {
            showMessage("请输入型深");
        } else if (StringUtil.isTrimBlank(getTextValue(etShipFull))) {
            showMessage("请输入满载吃水");
        } else if (StringUtil.isTrimBlank(getTextValue(etShipLoadton))) {
            showMessage("请输入载重吨位");
        } else {
            isOK = true;
        }
        return isOK;
    }
}
