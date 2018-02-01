package com.hletong.hyc.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.HistoryOrder;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.OrderDetailsFragment;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2016/11/24.
 * 车船方 => 承运历史
 */
public class HistoryOrderAdapter extends HFRecyclerAdapter<HistoryOrder> {

    public HistoryOrderAdapter(Context context) {
        super(context, null);
    }

    @Override
    protected BaseHolder<HistoryOrder> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new HistoryOrderViewHolder(getInflater().inflate(R.layout.recycler_item_source, parent, false), null);
    }

    private class HistoryOrderViewHolder extends SourceListAdapter.BaseViewHolder<HistoryOrder> {
        private String tradeUuid;
        private String name;

        public HistoryOrderViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            setVisible(R.id.flag, View.GONE);
            setVisible(R.id.action, View.GONE);
            itemView.setOnClickListener(this);
        }

        public void setData(HistoryOrder order) {
            super.setData(order);
            name = order.getOrgin_cargon_kind_name();
            tradeUuid = order.getTrade_uuid();
        }

        @Override
        protected String getTimeLabelDesc(Source source) {
            HistoryOrder ho = (HistoryOrder) source;
            return ho.getCarrier_no() + " 装货日期：" + source.getLoadingPeriod();
        }

        @Override
        protected String getCargoDesc(Source source) {
            return super.getCargoDesc(source);
        }

        @Override
        public void onClick(View v) {
            Bundle fa = new Bundle();
            fa.putString("tradeUuid", tradeUuid);
            toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(name, OrderDetailsFragment.class, fa));
        }
    }
}
