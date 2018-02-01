package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Receipt;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.ReceiptUploadActivityNew;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2016/11/10.
 */
public class ReceiptAdapter extends HFRecyclerAdapter<Receipt> {
    private ITransactionView delegate;
    private int type;// 1 => 未上传， 2 => 历史列表

    public ReceiptAdapter(Context context, List<Receipt> data, ITransactionView delegate, int type) {
        super(context, data);
        this.type = type;
        this.delegate = delegate;
    }

    @Override
    protected BaseHolder<Receipt> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (type == 1) {
            return new ReceiptViewHolder(getInflater().inflate(R.layout.recycler_item_source, parent, false), delegate);
        } else {
            return new ReceiptViewHolder(new StatusItemUtil().wrapView(getInflater().inflate(R.layout.recycler_item_source, parent, false)), delegate);
        }
    }

    private final class ReceiptViewHolder extends SourceListAdapter.QuoteViewHolder<Receipt> {

        ReceiptViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            itemView.setOnClickListener(null);//不要全体点击事件
            if (type == 1) {
                setText(R.id.action, R.string.upload);
            } else {
                setText(R.id.action, R.string.view);
            }
            setVisible(R.id.flag, View.GONE);
        }

        public void setData(Receipt receipt) {
            super.setData(receipt);
            StatusItemUtil mStatusItemUtil = (StatusItemUtil) itemView.getTag();
            if (mStatusItemUtil != null) {
                switch (receipt.getReceipt_status()) {
                    case 0://待上传
                        mStatusItemUtil.setData("待上传", receipt.getAvailableTime());
                        break;
                    case 1://已上传
                        mStatusItemUtil.setData("已上传", receipt.getAvailableTime());
                        break;
                    case 2://已废弃
                    default:
                        mStatusItemUtil.setData(getColor(R.color.text_hint), "已废弃", receipt.getAvailableTime());
                        break;
                }
            }
        }

        @Override
        protected String getTimeLabelDesc(Source source) {
            return source.getCarrierAndLoadingPeriod();
        }

        @Override
        public String getCargoDesc(Source source) {
            return source.getCargoDescWithUnit();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ReceiptUploadActivityNew.class);
            intent.putExtra("receipt", getItem(getAdapterPosition()));
            intent.putExtra("type", type);
            startActivityForResult(intent);
        }
    }
}
