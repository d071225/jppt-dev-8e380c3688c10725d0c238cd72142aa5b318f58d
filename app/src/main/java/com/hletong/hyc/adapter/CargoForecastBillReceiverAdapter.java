package com.hletong.hyc.adapter;

import android.content.Context;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Drawee;
import com.hletong.mob.pullrefresh.BaseHolder;

/**
 * Created by ddq on 2017/3/15.
 * 受票方
 */

public class CargoForecastBillReceiverAdapter extends BaseEditAdapter<Drawee> {

    public CargoForecastBillReceiverAdapter(Context context, boolean modeEdit, int selected) {
        super(context, null, modeEdit, R.layout.recycler_item_bill_receiver, selected);
    }

    @Override
    protected void bindData(Drawee billReceiver, BaseHolder<Drawee> holder) {
        holder.setText(R.id.title, billReceiver.getInvoice_name());
        holder.setText(R.id.tax, "纳税号：" + billReceiver.getInvoice_taxpayer());
        holder.setText(R.id.cardNo, "卡号：" + billReceiver.getInvoice_bank_code());
        holder.setText(R.id.address, "地址：" + billReceiver.getInvoice_address());
    }
}
