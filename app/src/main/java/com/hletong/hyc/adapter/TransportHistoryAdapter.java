package com.hletong.hyc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.HistoryOrderDetail;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2016/11/24.
 */
public class TransportHistoryAdapter extends HFRecyclerAdapter<HistoryOrderDetail> {

    public TransportHistoryAdapter(Context context, List<HistoryOrderDetail> data) {
        super(context, data, Integer.MAX_VALUE);
    }

    @Override
    protected BaseHolder<HistoryOrderDetail> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new TViewHolder(getInflater().inflate(R.layout.recycler_item_transport_history, parent, false));
    }

    private class TViewHolder extends BaseHolder<HistoryOrderDetail> {

        private TViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(HistoryOrderDetail hod) {
            TextView des = getView(R.id.des);
            des.setText(hod.getContent());
            TextView time = getView(R.id.time);
            time.setText(hod.getCreateTime());
        }
    }
}
