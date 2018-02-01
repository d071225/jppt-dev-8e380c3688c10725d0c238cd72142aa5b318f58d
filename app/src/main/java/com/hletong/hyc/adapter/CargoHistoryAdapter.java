package com.hletong.hyc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CargoSourceItem;
import com.hletong.hyc.model.HistoryOrder;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.CargoOrderHistoryDetailsFragment;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2017/4/13.
 * 货方-历史订单
 */

public class CargoHistoryAdapter extends HFRecyclerAdapter<CargoSourceItem> {

    public CargoHistoryAdapter(Context context) {
        super(context, null);
    }

    @Override
    protected BaseHolder<CargoSourceItem> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_source, parent, false), null);
    }


    private class ViewHolder extends SourceListAdapter.BaseViewHolder<CargoSourceItem> {
        private String tradeUuid;
        private String name;

        public ViewHolder(View itemView, ITransactionView delegate) {
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
            return "装货日期：" + source.getLoadingPeriod();
        }

        @Override
        public void onClick(View v) {
            toActivity(CommonWrapFragmentActivity.class,
                    CommonWrapFragmentActivity.getBundle("历史货源详情",
                            CargoOrderHistoryDetailsFragment.class,
                            CargoOrderHistoryDetailsFragment.getParams(getItem(getAdapterPosition()))));
        }
    }
}
