package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.mob.base.BaseActivity;

/**
 * Created by ddq on 2017/2/15.
 */

public class ApplyDepositCardActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_deposit_card;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle(R.string.hl_deposit_card_introduce);
        View view = findViewById(R.id.mm_member_unit);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.margin_15dp);
        view.setLayoutParams(layoutParams);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.hl_deposit_card_introduce_contact_area_title);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        LoginInfo loginInfo = LoginInfo.getLoginInfo();
        TextView mm_name = (TextView) findViewById(R.id.mm_name);
        mm_name.setText(loginInfo.getMm_unit_name());

        TextView contact = (TextView) findViewById(R.id.contact);
        contact.setText(loginInfo.getMm_biz_contact());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_tel:
                CallPhoneDialog.getInstance().show(getSupportFragmentManager(), "联系会员管理单位", (String) v.getTag(), (String) v.getTag());
                break;
        }
    }
}
