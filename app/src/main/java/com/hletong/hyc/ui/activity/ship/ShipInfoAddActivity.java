package com.hletong.hyc.ui.activity.ship;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.ShipInfo;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆信息补全
 * Created by chengxin on 2017/6/12.
 */
public class ShipInfoAddActivity extends BaseActivity implements BottomSelectorDialog.OnItemSelectedListener<DictionaryItem> {

    @BindView(R.id.cv_shipType)
    CommonInputView cv_shipType;
    //车牌号
    @BindView(R.id.cv_shipLength)
    CommonInputView cv_shipLength;
    @BindView(R.id.cv_loadedWater)
    CommonInputView cv_loadedWater;
    @BindView(R.id.cv_newTonnage)
    CommonInputView cv_newTonnage;

    @BindView(R.id.cv_loadTon)
    CommonInputView cv_loadTon;

    @BindView(R.id.cv_gjzsh)
    CommonInputView cv_gjzsh;

    private ShipInfo mShipInfo;

    private DictItemDialog mDictItemFragment;


    @Override
    public int getLayoutId() {
        return R.layout.activity_ship_info_add;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
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
        mShipInfo = getSerializable(ShipInfo.class.getSimpleName());
        if (mShipInfo != null) {
            if (mShipInfo.getShip_type() != null) {
                cv_shipType.setText(mShipInfo.getShip_type().getText());
            }
            cv_shipLength.setText(mShipInfo.getShipLength());
            cv_loadedWater.setText(mShipInfo.getLoadedWater());
            cv_loadTon.setText(mShipInfo.getLoadTon());
            cv_gjzsh.setText(mShipInfo.getGjzsNumber());
            cv_newTonnage.setText(mShipInfo.getNewTonnage());
        } else {
            mShipInfo = new ShipInfo();
        }
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        cv_shipType.setText(item.getText());
        mShipInfo.setShip_type(item);
    }

    @OnClick({R.id.cv_shipType, R.id.submit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_shipType) {
            mDictItemFragment.showDict("ship_type", "船舶类型", 1);
        } else if (v.getId() == R.id.submit) {
            if (validate()) {
                mShipInfo.setShipLength(cv_shipLength.getInputValue());
                mShipInfo.setLoadedWater(cv_loadedWater.getInputValue());
                mShipInfo.setLoadTon(cv_loadTon.getInputValue());
                mShipInfo.setGjzsNumber(cv_gjzsh.getInputValue());
                mShipInfo.setNewTonnage(cv_newTonnage.getInputValue());
                Intent intent = new Intent();
                intent.putExtra(ShipInfo.class.getSimpleName(), mShipInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private boolean validate() {
        boolean isOK = false;
        if (mShipInfo.getShip_type() == null) {
            showMessage("请选择船舶类型");
        }
//        else if (StringUtil.isTrimBlank(cv_shipLength.getInputValue())) {
//            showMessage("请输入船长");
//        }
        else if (StringUtil.isTrimBlank(cv_loadedWater.getInputValue())) {
            showMessage("请输入满载吃水");
        }
//        else if (StringUtil.isTrimBlank(cv_newTonnage.getInputValue())) {
//            showMessage("请输入净吨位");
//        }
        else if (StringUtil.isTrimBlank(cv_loadTon.getInputValue())) {
            showMessage("请输入载重吨位");
        } else if (StringUtil.isTrimBlank(cv_gjzsh.getInputValue())) {
            showMessage("请输入国籍证书号");
        } else {
            isOK = true;
        }
        return isOK;
    }
}
