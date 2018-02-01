package com.hletong.hyc.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by dongdaqing on 2017/6/30.
 */

public class RecyclerAdapterObserver extends RecyclerView.AdapterDataObserver {
    @Override
    public void onChanged() {

    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        onChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        onChanged();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        onChanged();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        onChanged();
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        onChanged();
    }
}
