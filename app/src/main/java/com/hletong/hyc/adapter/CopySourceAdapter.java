package com.hletong.hyc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CargoSourceItem;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.view.util.ShapeBinder;

/**
 * Created by ddq on 2017/3/6.
 * 复制货源
 */

public class CopySourceAdapter extends HFRecyclerAdapter<CargoSourceItem> {
    private ITransactionView mFragmentDelegate;

    public CopySourceAdapter(Context context, ITransactionView fragmentDelegate) {
        super(context, null);
        this.mFragmentDelegate = fragmentDelegate;
    }

    @Override
    protected BaseHolder<CargoSourceItem> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_source, parent, false), mFragmentDelegate);
    }

    private static class ViewHolder extends SourceListAdapter.QuoteViewHolder<CargoSourceItem> {

        public ViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            itemView.setOnClickListener(null);//不要全体点击事件
            setVisible(R.id.CargoWeight, View.GONE);
            setVisible(R.id.flag, View.GONE);
            setText(R.id.action, R.string.copy);
        }

        public void setData(CargoSourceItem data) {
            super.setData(data);
            setText(R.id.time, data.getUnloadContactWithTel());
            //国联议价货源不能复制
            boolean canCopy = data.canCopy();
            setEnabled(R.id.action, canCopy);
            TextView action = getView(R.id.action);
            if (data.canCopy()) {
                action.setTextColor(getColor(R.color.colorAccent));
                ViewUtil.setBackground(action, ContextCompat.getDrawable(itemView.getContext(), R.drawable.recycler_item_quote_border));
            } else {
                action.setTextColor(getColor(R.color.disabled_text_color));
                ShapeBinder.with(Color.TRANSPARENT).stroke(getColor(R.color.disabled_text_color)).strokeWidth(1).radius(5).drawableTo(action);
            }
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("source", getSource());
            toActivity(CargoForecastActivity.class, bundle);
        }
    }
}
