package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Receipt;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.view.controller.EasyActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 审核不通过原因
 */
public class ReceiptRefuseActivity extends EasyActivity {
    @BindView(R.id.tv_refuse)
    TextView tvRefuse;
    @BindView(R.id.tv_date)
    TextView tvDate;

    @Override
    public int getLayoutId() {
        return R.layout.activity_receipt_refuse;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        Receipt receipt = getParcelable("receipt");
        tvRefuse.setText(receipt != null ? receipt.getMemo() : "");
        if (receipt.getAccept_dttm() != null) {
            Date date = SimpleDate.parseStrDate(receipt.getAccept_dttm(), SimpleDate.formatterHlet);
            if (date != null) {
                tvDate.setText(SimpleDate.formatterToMin.format(date));
            }
        }
    }
}
