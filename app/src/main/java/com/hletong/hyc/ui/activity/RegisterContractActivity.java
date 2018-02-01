package com.hletong.hyc.ui.activity;

import android.view.View;

import com.hletong.hyc.R;
import com.hletong.mob.base.BaseActivity;

public class RegisterContractActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_register_contract;
    }

    @Override
    public void setListener() {
        super.setListener();
        findViewById(R.id.btn_registerEnsure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
