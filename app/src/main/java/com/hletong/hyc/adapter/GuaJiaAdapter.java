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
 * Created by ddq on 2016/11/7.
 * 挂价摘牌列表
 */
public class GuaJiaAdapter extends SourceListAdapter {

    public GuaJiaAdapter(Context context, List<Source> items, ITransactionView delegate) {
        super(context, items, delegate);
    }

    @Override
    protected BaseHolder<Source> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2 || viewType == 3 || viewType == 4) {
            return new ViewHolder(getInflater().inflate(R.layout.recycler_item_source, parent, false), getDelegate());
        }
        return super.onCreateItemViewHolder(parent, viewType);
    }

    static class ViewHolder extends BookViewHolder<Source> {

        public ViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            setVisible(R.id.flag, View.GONE);
        }
    }
}
