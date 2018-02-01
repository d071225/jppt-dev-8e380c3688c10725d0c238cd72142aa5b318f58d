package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.validator.SimpleValidatorListener;
import com.hletong.mob.validator.Validator;
import com.hletong.mob.validator.result.ResultBasic;
import com.orhanobut.logger.Logger;
import com.xcheng.view.util.JumpUtil;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherInfoRegisterActivity extends BaseActivity {
    private DictItemDialog dictItemDialog;
    private ArrayList<DictionaryItem> dictionaryItems;
    private Validator validator;
    @Override
    public int getLayoutId() {
        return R.layout.activity_otherinfo_register;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        validator = Validator.create(this);
        ResultBasic resultBasic = getParcelable(ResultBasic.class.getName());
        if (resultBasic != null) {
            validator.bindMap(resultBasic.basicMap());
        }
        validator.setOnValidatorListener(new SimpleValidatorListener() {
            @Override
            public void onFormSuccess(Map<String, ResultBasic> formMap, Validator validator) {
                Intent intent = new Intent();
                intent.putExtra(ResultBasic.class.getName(), ResultBasic.mapOf(formMap, "已选择"));
                Logger.d(formMap);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dictItemDialog = new DictItemDialog(this);
        dictItemDialog.setOnItemSelected(new BottomSelectorDialog.OnItemSelectedListener<DictionaryItem>() {
            @Override
            public void onItemSelected(DictionaryItem item, int extra) {
                validator.bind(R.string.key_isOrganization, item.getId(), item.getText());
            }
        });
        dictionaryItems = new ArrayList<>();
        dictionaryItems.add(new DictionaryItem("1", "是"));
        dictionaryItems.add(new DictionaryItem("2", "否"));
    }

    @OnClick({R.id.val_soldier, R.id.val_organization, R.id.submit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.val_soldier:
                toActivity(SoldierInfoActivity.class, JumpUtil.getBundle(ResultBasic.class.getName(), validator.findBasicByKey(R.string.key_soldierInfo)), 10, null);
                break;
            case R.id.val_organization:
                dictItemDialog.showDict("cv_soldier", "请选择", dictionaryItems, 0);
                break;
            case R.id.submit:
                validator.validate();
                break;
        }
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == 10) {
            validator.bind(R.string.key_soldierInfo, (ResultBasic) getParcelable(data, ResultBasic.class.getName()));
        }
    }
}
