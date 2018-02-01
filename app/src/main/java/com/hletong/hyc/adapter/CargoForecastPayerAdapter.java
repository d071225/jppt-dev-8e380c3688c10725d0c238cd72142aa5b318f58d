package com.hletong.hyc.adapter;

import android.content.Context;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Payer;
import com.hletong.mob.pullrefresh.BaseHolder;

/**
 * Created by ddq on 2017/3/15.
 * 付款方
 */

public class CargoForecastPayerAdapter extends BaseEditAdapter<Payer> {

    public CargoForecastPayerAdapter(Context context, boolean modeEdit, int selected) {
        super(context, null, modeEdit, R.layout.recycler_item_payer, selected);
    }

    @Override
    protected void bindData(Payer payer, BaseHolder<Payer> holder) {
        holder.setText(R.id.title, payer.getAccountName());
        holder.setText(R.id.bank, "建设银行 " + payer.getBankName());
//        holder.setText(R.id.account, "开户名：" + payer.getAccountName());
        holder.setText(R.id.cardNo, "卡号：" + payer.getCardNo());
    }
}
