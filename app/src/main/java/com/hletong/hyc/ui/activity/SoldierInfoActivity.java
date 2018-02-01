package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;

import com.hletong.hyc.R;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.validator.SimpleValidatorListener;
import com.hletong.mob.validator.Validator;
import com.hletong.mob.validator.result.ResultBasic;
import com.hletong.mob.validator.result.ResultType;
import com.xcheng.view.EasyView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoldierInfoActivity extends BaseActivity {
    @BindView(R.id.layoutIs)
    View layoutIs;
    @BindView(R.id.isSoldier)
    CheckBox isSoldier;

    @BindView(R.id.layoutNot)
    View layoutNot;
    @BindView(R.id.notSoldier)
    CheckBox notSoldier;

    @BindView(R.id.layoutInput)
    View layoutInput;
    private Validator validator;
    public static final String key_isSoldier = EasyView.getString(R.string.key_isSoldier);
    public static final String key_soldierKind = EasyView.getString(R.string.key_soldierKind);
    public static final String key_soldierArmy = EasyView.getString(R.string.key_soldierArmy);
    public static final String key_soldierLevel = EasyView.getString(R.string.key_soldierLevel);

    @Override
    public int getLayoutId() {
        return R.layout.activity_soldier_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        validator = Validator.create(this);
        validator.setOnValidatorListener(new SimpleValidatorListener() {
            @Override
            public void onFormSuccess(Map<String, ResultBasic> formMap, Validator validator) {
                boolean isSolder = isSoldier.isChecked();
                formMap.put(key_isSoldier, ResultBasic.textOf(ResultType.TEXT, isSolder ? "1" : "2"));
                Intent intent = new Intent();
                intent.putExtra(ResultBasic.class.getName(), ResultBasic.mapOf(formMap, isSolder ? "是" : "否"));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ResultBasic resultBasic = getParcelable(ResultBasic.class.getName());
        if (resultBasic != null) {
            validator.bindMap(resultBasic.basicMap());
            ResultBasic item = resultBasic.get(key_isSoldier);
            if (item != null) {
                boolean isSolider = item.resultText().equals("1");
                setSoldier(isSolider);
            }
        } else {
            setSoldier(false);
            notSoldier.setChecked(false);
        }
    }

    private void setSoldier(boolean isSoldier) {
        validator.findViewByKey(key_soldierKind).setValidate(isSoldier);
        validator.findViewByKey(key_soldierArmy).setValidate(isSoldier);
        validator.findViewByKey(key_soldierLevel).setValidate(isSoldier);
        layoutInput.setVisibility(isSoldier ? View.VISIBLE : View.GONE);
        this.isSoldier.setChecked(isSoldier);
        this.notSoldier.setChecked(!isSoldier);
    }

    @OnClick({R.id.layoutIs, R.id.layoutNot, R.id.submit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.layoutIs:
                setSoldier(true);
                break;
            case R.id.layoutNot:
                setSoldier(false);
                break;
            case R.id.submit:
                if (!isSoldier.isChecked() && !notSoldier.isChecked()) {
                    showMessage("请选择是否为退役军人");
                    return;
                }
                validator.validate();
                break;
        }
    }
}
