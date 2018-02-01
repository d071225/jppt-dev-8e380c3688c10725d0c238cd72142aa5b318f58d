package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Invoice;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.InvoiceUploadActivityNew;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2016/11/9.
 */
public class InvoiceAdapter extends HFRecyclerAdapter<Invoice> {
    private ITransactionView delegate;
    private int type;//未上传列表 => 1，历史列表 => 2

    public InvoiceAdapter(Context context, List<Invoice> data, ITransactionView delegate, int type) {
        super(context, data);
        this.delegate = delegate;
        this.type = type;
    }

    @Override
    protected BaseHolder<Invoice> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (type == 1) {
            return new InvoiceViewHolder(getInflater().inflate(R.layout.recycler_item_source, parent, false), delegate);
        } else {
            return new InvoiceViewHolder(new StatusItemUtil().wrapView(getInflater().inflate(R.layout.recycler_item_source, parent, false)), delegate);
        }
    }

    private class InvoiceViewHolder extends SourceListAdapter.QuoteViewHolder<Invoice> {

        public InvoiceViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            itemView.setOnClickListener(null);//不要全体点击事件
            if (type == 1){
                setText(R.id.action, R.string.upload);
            }else {
                setText(R.id.action, R.string.view);
            }
            setVisible(R.id.flag, View.GONE);
        }

        public void setData(Invoice invoice) {
            super.setData(invoice);
            StatusItemUtil mStatusItemUtil = (StatusItemUtil) itemView.getTag();
            if (mStatusItemUtil != null) {
                switch (invoice.getInvoice_status()) {
                    case 0://待上传
                        mStatusItemUtil.setData("待上传", invoice.getAvailableTime());
                        break;
                    case 1://已上传
                        mStatusItemUtil.setData("已上传", invoice.getAvailableTime());
                        break;
                    case 2://已废弃
                    default:
                        mStatusItemUtil.setData(getColor(R.color.text_hint), "已废弃", invoice.getAvailableTime());
                        break;
                }
            }
        }

        @Override
        protected String getTimeLabelDesc(Source source) {
            return source.getCarrierAndLoadingPeriod();
        }

        @Override
        protected String getCargoDesc(Source source) {
            return source.getCargoDescWithUnit();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), InvoiceUploadActivityNew.class);
            intent.putExtra("invoice", getItem(getAdapterPosition()));
            startActivityForResult(intent);
        }
    }
}
