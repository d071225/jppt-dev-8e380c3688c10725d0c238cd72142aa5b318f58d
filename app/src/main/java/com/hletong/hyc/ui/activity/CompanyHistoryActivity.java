package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.mob.base.BaseActivity;

public class CompanyHistoryActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_hletong_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TextView textView = findViewById(R.id.detail);
        textView.setText(Html.fromHtml(getDetail()));
        setCustomTitle(R.string.company_history);
    }

    private String getDetail() {
        return
                getString(R.string.company_history_detail_p_1) +
                        getString(R.string.company_history_detail_p_2) +
                        getString(R.string.company_history_detail_p_3) +
                        getString(R.string.company_history_detail_p_4) +
                        getString(R.string.company_history_detail_p_5) +
                        getString(R.string.company_history_detail_p_6) +
                        getString(R.string.company_history_detail_p_7);
    }
}
