package com.hletong.hyc.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.activity.OtherInfoRegisterActivity;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.validator.Validator;
import com.hletong.mob.validator.result.ResultBasic;
import com.xcheng.view.EasyView;
import com.xcheng.view.util.JumpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册认证其他信息帮助类
 * Created by chengxin on 2017/8/24.
 */
public class OtherInfoHelper {
    private ResultBasic otherInfo;
    private CommonInputView commonInputView;
    private ITransactionView transactionView;
    public static final int REQUEST_CODE = 40;

    public OtherInfoHelper(ITransactionView transactionView, CommonInputView commonInputView) {
        this.commonInputView = commonInputView;
        this.transactionView = transactionView;
    }
    public void setOtherInfo(ResultBasic otherInfo) {
        this.otherInfo = otherInfo;
        if (otherInfo != null) {
            commonInputView.setText("已选择");
        }
    }

    public void toOtherInfoView() {
        Bundle bundle = JumpUtil.getBundle(ResultBasic.class.getName(), otherInfo);
        transactionView.toActivity(OtherInfoRegisterActivity.class, bundle, REQUEST_CODE, null);
    }

    public void receiveData(int requestCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            otherInfo = intent.getParcelableExtra(ResultBasic.class.getName());
            commonInputView.setText("已选择");
        }
    }

    @Nullable
    public ResultBasic getOtherInfo() {
        return otherInfo;
    }

    public static final String key_isOrganization = EasyView.getString(R.string.key_isOrganization);
    public static final String key_soldierInfo = EasyView.getString(R.string.key_soldierInfo);
    public static final String key_isSoldier = EasyView.getString(R.string.key_isSoldier);
    public static final String key_soldierArmy = EasyView.getString(R.string.key_soldierArmy);
    public static final String key_soldierKind = EasyView.getString(R.string.key_soldierKind);
    public static final String key_soldierLevel = EasyView.getString(R.string.key_soldierLevel);

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        if (otherInfo != null) {
            params.put(key_isOrganization, otherInfo.get(key_isOrganization).resultText());
            ResultBasic solderInfo = otherInfo.get(key_soldierInfo);
            if (solderInfo != null) {
                String isSoldier = solderInfo.get(key_isSoldier).resultText();
                params.put(key_isSoldier, isSoldier);
                boolean hasSoldierInfo = "1".equals(isSoldier);
                params.put(key_soldierArmy, hasSoldierInfo ? solderInfo.get(key_soldierArmy).resultText() : Validator.EMPTY);
                params.put(key_soldierKind, hasSoldierInfo ? solderInfo.get(key_soldierKind).resultText() : Validator.EMPTY);
                params.put(key_soldierLevel, hasSoldierInfo ? solderInfo.get(key_soldierLevel).resultText() : Validator.EMPTY);
            }
        }
        return params;
    }
}
