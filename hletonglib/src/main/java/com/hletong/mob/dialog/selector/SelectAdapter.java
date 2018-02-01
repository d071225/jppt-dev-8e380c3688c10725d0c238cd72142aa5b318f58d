package com.hletong.mob.dialog.selector;

import android.content.Context;
import android.support.annotation.IntRange;

import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by dongdaqing on 2017/6/2.
 */

public abstract class SelectAdapter<T> extends HFRecyclerAdapter<T> {
    private int selected = -1;

    public SelectAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public SelectAdapter(Context context, List<T> data, @IntRange(from = 1) int length) {
        super(context, data, length);
    }

    public final boolean isSelected(int position){
        return selected == position;
    }

    public final void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }
}
