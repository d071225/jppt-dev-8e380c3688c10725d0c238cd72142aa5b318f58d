package com.hletong.hyc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Source;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;

import java.util.List;

/**
 * Created by ddq on 2016/10/27.
 * 竞价列表
 */

public class CBAdapter extends SourceListAdapter {

    public CBAdapter(Context context, List<Source> data, ITransactionView delegate) {
        super(context, data, delegate);
    }

    @Override
    protected BaseHolder<Source> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new ViewHolder(getView(parent), getDelegate());
        return super.onCreateItemViewHolder(parent, viewType);
    }

    private class ViewHolder extends BiddingViewHolder<Source> {

        public ViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            setVisible(R.id.flag, View.GONE);
        }
    }
}
