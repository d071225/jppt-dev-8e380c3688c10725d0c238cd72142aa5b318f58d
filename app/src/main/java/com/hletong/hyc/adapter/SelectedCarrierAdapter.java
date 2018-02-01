package com.hletong.hyc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.AppointCarrier;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2017/3/20.
 */

public class SelectedCarrierAdapter extends HFRecyclerAdapter<AppointCarrier> {
    private int transporterType;

    public SelectedCarrierAdapter(Context context, List<AppointCarrier> data,int transporterType) {
        super(context, data);
        this.transporterType = transporterType;
    }

    @Override
    protected BaseHolder<AppointCarrier> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_selected_carrier, parent, false));
    }

    private class ViewHolder extends BaseHolder<AppointCarrier> implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(AppointCarrier appointCarrier) {
            setText(R.id.text, appointCarrier.getCarrier(transporterType));
        }

        @Override
        public void onClick(View v) {
            remove(getItem(getAdapterPosition()));
        }
    }
}
