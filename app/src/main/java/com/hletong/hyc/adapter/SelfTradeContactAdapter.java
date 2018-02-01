package com.hletong.hyc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CargoContact;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by dongdaqing on 2017/5/22.
 */

public class SelfTradeContactAdapter extends HFRecyclerAdapter<CargoContact> {
    public SelfTradeContactAdapter(Context context) {
        super(context, null);
    }

    @Override
    protected BaseHolder<CargoContact> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_selft_trade_contact, parent, false));
    }

    private class ViewHolder extends BaseHolder<CargoContact> implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(CargoContact data) {
            setText(R.id.title, data.getContactName());
            setText(R.id.second, data.getContactTel());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("name", getItem(getAdapterPosition()).getContactName());
            intent.putExtra("tel", getItem(getAdapterPosition()).getContactTel());
            Activity activity = (Activity) v.getContext();
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }
}
