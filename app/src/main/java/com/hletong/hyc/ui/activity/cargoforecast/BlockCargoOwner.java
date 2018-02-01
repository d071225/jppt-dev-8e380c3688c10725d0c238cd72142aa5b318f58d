package com.hletong.hyc.ui.activity.cargoforecast;

import android.content.Intent;
import android.view.View;
import android.view.ViewStub;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.SelfTradeContactFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.util.StringUtil;

import java.util.HashMap;

/**
 * Created by dongdaqing on 2017/5/22.
 * 自主交易货方联系人
 */

public class BlockCargoOwner extends BaseBlock {
    private CommonInputView contact;
    private CommonInputView tel;

    public BlockCargoOwner(ViewStub viewStub, CargoForecastActivity.BlockAction blockAction, ITransactionView switchDelegate) {
        super(viewStub, "货方联系人", blockAction, switchDelegate);
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        if (billingType == 3) {
            showBlock(false);
        } else {
            hideBlock();
        }
    }

    @Override
    void viewInflated(View view) {
        contact = (CommonInputView) findViewById(R.id.contact);
        tel = (CommonInputView) findViewById(R.id.tel);
        contact.getSuffix().setOnClickListener(this);
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        contact.setText(source.getLoadingContact());
        tel.setText(source.getLoading_contacts_tel());
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(CommonWrapFragmentActivity.class, 199, CommonWrapFragmentActivity.getBundle(
                "货方联系人", SelfTradeContactFragment.class, null
        ));
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        contact.setText(data.getStringExtra("name"));
        tel.setText(data.getStringExtra("tel"));
    }

    @Override
    public void fillSource(Source source) {
        source.setLoading_contacts(contact.getInputValue());
        source.setLoading_contacts_tel(tel.getInputValue());
    }

    @Override
    public void getSelfTradeBlockParams(HashMap<String, String> params) {
        params.put("contactName", contact.getInputValue());
        params.put("contactTel", tel.getInputValue());
    }

    @Override
    boolean isBlockSatisfied() {
        return contact.inputHaveValue() && tel.inputHaveValue();
    }

    @Override
    public String getErrorMessage() {
        if (!contact.inputHaveValue())
            return "请填写联系人姓名";

        if (!StringUtil.isMobileNumber(tel.getInputValue()))
            return "请填写正确的电话";
        return null;
    }
}
