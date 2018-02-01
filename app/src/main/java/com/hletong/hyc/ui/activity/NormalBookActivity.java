package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TransporterBase;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.util.ContractSpan;
import com.xcheng.view.controller.dialog.BottomOptionDialog;
import com.xcheng.view.controller.dialog.SimpleSelectListener;

/**
 * Created by ddq on 2017/1/5.
 * 挂价摘牌基类
 */

public abstract class NormalBookActivity<S extends TransporterBase, P extends BookContract.NormalPresenter> extends BookActivityBase<S, P> implements BookContract.NormalView {
    private BottomOptionDialog mApplyDialog;

    @Override
    public void showAlertForBorrowInsuranceMoney() {
        if (mApplyDialog == null) {
            mApplyDialog = new BottomOptionDialog.Builder(this)
                    .optionTexts("会员管理单位垫付", "办理惠龙卡")
                    .onSelectListener(new SimpleSelectListener() {
                        @Override
                        public void onOptionSelect(View view, int position) {
                            if (position == 0) {
                                getPresenter().bookWithAdvance();
                            } else if (position == 1) {
                                toActivity(ApplyDepositCardActivity.class, null, null);
                            }
                        }
                    }).create();
        }
        mApplyDialog.show();
    }

    @Override
    public String getCarrier() {
        return getSelectedItem().getValue();
    }

    @Override
    public String getCargo() {
        return input.getInputValue();
    }

    @Override
    public void onClick(View v) {
        getPresenter().submit(getSubmitParam(input.getInputValue(), getDeductTaxRt()));
    }

    protected abstract PayMode getSubmitParam(String cargo, Source.DeductRt deductTaxRt);

    @Override
    public void showContractCheckbox() {
        //展示三方合同的checkbox
        mCheckBox.setVisibility(View.VISIBLE);
        submit.setEnabled(false);

        ContractSpan.setContractHint(R.string.hint_self_trade_contract, mCheckBox).setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().viewContract(101, input.getInputValue());
            }
        });

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                submit.setEnabled(isChecked);
            }
        });
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        //用户点击了三方合同界面的"阅读并确认"按钮
        mCheckBox.setChecked(true);
    }
}
