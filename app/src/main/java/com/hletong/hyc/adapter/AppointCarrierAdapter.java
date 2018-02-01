package com.hletong.hyc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.AppointCarrier;
import com.hletong.hyc.ui.widget.RatingBar;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by ddq on 2017/3/20.
 * 指定车船
 */

public class AppointCarrierAdapter extends HFRecyclerAdapter<AppointCarrier> {
    private int transporterType;
    private LinkedHashSet<AppointCarrier> selected;
    private ItemChangedListener<AppointCarrier> mItemChangedListener;

    public AppointCarrierAdapter(Context context, int transporterType, ArrayList<AppointCarrier> carriers, ItemChangedListener<AppointCarrier> listener) {
        super(context, null);
        this.transporterType = transporterType;
        this.mItemChangedListener = listener;
        if (!ParamUtil.isEmpty(carriers)) {
            this.selected = new LinkedHashSet<>(carriers);
        } else {
            selected = new LinkedHashSet<>();
        }
        //hideFooter();
    }

    @Override
    protected BaseHolder<AppointCarrier> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_appoint_carrier, parent, false));
    }

    public ArrayList<AppointCarrier> getSelectedItems() {
        return new ArrayList<>(selected);
    }

    private class ViewHolder extends BaseHolder<AppointCarrier> implements View.OnClickListener {
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);
        }

        public void setData(AppointCarrier appointCarrier) {
            if (selected.contains(appointCarrier)) {
                mImageView.setSelected(true);
            } else {
                mImageView.setSelected(false);
            }
            setText(R.id.carrier, appointCarrier.getCarrier(transporterType));
            setText(R.id.member, appointCarrier.getMember_name());
            setText(R.id.address, appointCarrier.getAddress());
            setText(R.id.favor_rate, appointCarrier.getMemberGrade());
            setText(R.id.trade_ct, appointCarrier.getTradeCount());
            RatingBar ratingBar = getView(R.id.rate);
            ratingBar.setProgress(appointCarrier.getMember_grade());
        }

        @Override
        public void onClick(View v) {
            AppointCarrier ac = getItem(getAdapterPosition());
            if (mImageView.isSelected()) {
                mItemChangedListener.itemUnSelected(ac);
                selected.remove(ac);
            } else {
                mItemChangedListener.itemSelected(ac);
                selected.add(ac);
            }
            mImageView.setSelected(!mImageView.isSelected());
        }
    }

    public interface ItemChangedListener<T> {
        void itemSelected(T item);

        void itemUnSelected(T item);
    }
}
