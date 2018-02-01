package com.hletong.hyc.util;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Agreement;
import com.hletong.hyc.ui.activity.WebActivity;
import com.hletong.hyc.ui.widget.SmoothClickCheckBox;
import com.hletong.mob.architect.view.ITransactionView;

import static com.hletong.hyc.ui.widget.SmoothClickCheckBox.*;

/**
 * Created by dongdaqing on 2017/7/13.
 */

public class ContractPack implements OnSpanClickListener, CompoundButton.OnCheckedChangeListener {
    private boolean company;
    private SmoothClickCheckBox mCheckBox;
    private View mAssociatedButton;
    private ITransactionView mTransactionView;
    private Agreement mAgreement;

    public ContractPack(ITransactionView transactionView, boolean company, SmoothClickCheckBox checkBox, View associatedButton, Agreement agreement) {
        this.company = company;
        mTransactionView = transactionView;
        mCheckBox = checkBox;
        mAssociatedButton = associatedButton;
        mAgreement = agreement;

        init();
    }

    private void init() {
        mCheckBox.setSpans(AppTypeConfig.getRegisterContract());
        mCheckBox.setListener(this);
        mCheckBox.setCheckedChangeListener(this);
    }

    @Override
    public void onSpanClick(int index) {
        Bundle bundle = new Bundle();
        if (onSpanClick != null) {
            onSpanClick.onSpanClick(index);
        }
        switch (index) {
            case 0://车船货入会协议书
                bundle.putString("url", mAgreement.getUrlWithParams());
                if (AppTypeConfig.isCargo()) {
                    bundle.putString("title", "货方会员入会协议");
                } else if (AppTypeConfig.isShip()) {
                    bundle.putString("title", "船舶会员入会协议");
                } else if (AppTypeConfig.isTruck()) {
                    bundle.putString("title", "车辆会员入会协议");
                }
                break;
            case 1://会员守则
                bundle.putString("url", Pages.USER_CODE);
                bundle.putString("title", "会员守则");
                break;
            case 2://违规违约处理细则
                bundle.putString("url", Pages.BREACH_DEALING);
                bundle.putString("title", "违规违约处理细则");
                break;
            case 3://结算细则
                bundle.putString("url", Pages.SETTLE_DETAILS);
                bundle.putString("title", "结算细则");
                break;
        }
        mTransactionView.toActivity(WebActivity.class, bundle, null);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mAssociatedButton != null)
            mAssociatedButton.setEnabled(isChecked);
    }

    public boolean isCompany() {
        return company;
    }

    private OnSpanClickListener onSpanClick;

    public void setOnSpanClick(OnSpanClickListener onSpanClick) {
        this.onSpanClick = onSpanClick;
    }
}
