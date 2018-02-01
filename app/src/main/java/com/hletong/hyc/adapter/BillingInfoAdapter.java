package com.hletong.hyc.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.SourceInvoice;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2017/3/24.
 */

public class BillingInfoAdapter extends HFRecyclerAdapter<SourceInvoice.InvoiceDetail> {

    public BillingInfoAdapter(Context context, List<SourceInvoice.InvoiceDetail> data) {
        super(context, data);
    }

    @Override
    protected BaseHolder<SourceInvoice.InvoiceDetail> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder<>(getInflater().inflate(R.layout.recycler_item_billing_info, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(BaseHolder<SourceInvoice.InvoiceDetail> holder, int position, SourceInvoice.InvoiceDetail invoiceDetail) {
        holder.setText(R.id.time,invoiceDetail.getTime());
        holder.setText(R.id.title,invoiceDetail.getTitle());
    }
}
