package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.UnSignedContract;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2016/11/8.
 * 未签合同列表
 */
public class UnsignedContractAdapter extends HFRecyclerAdapter<UnSignedContract> {
    private ITransactionView delegate;

    public UnsignedContractAdapter(Context context, List<UnSignedContract> data, ITransactionView delegate) {
        super(context, data);
        this.delegate = delegate;
    }

    @Override
    protected BaseHolder<UnSignedContract> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new USContractViewHolder(getInflater().inflate(R.layout.recycler_item_source, parent, false), delegate);
    }

    private class USContractViewHolder extends SourceListAdapter.QuoteViewHolder<UnSignedContract> {

        public USContractViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            itemView.setOnClickListener(null);
            setVisible(R.id.flag, View.GONE);
            setText(R.id.action, "签约");
        }

        private String bookUuid;//车船签约合同之前获取合同信息使用的字段
        private String cargoUuid;//货方签约合同之前获取合同信息使用的字段

        public void setData(UnSignedContract contractItem) {
            this.bookUuid = contractItem.getBook_uuid();
            this.cargoUuid = contractItem.getCargo_uuid();
            super.setData(contractItem);
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
            Intent intent = new Intent(v.getContext(), AppTypeConfig.getContractActivity());
            intent.putExtra("title", getString(R.string.signContract));
            intent.putExtra("cargoUuid", cargoUuid);
            if (AppTypeConfig.isTransporter()) {
                intent.putExtra("bookUuid", bookUuid);
            }
            startActivityForResult(intent);
        }
    }
}
