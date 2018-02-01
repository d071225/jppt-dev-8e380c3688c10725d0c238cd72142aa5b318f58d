package com.hletong.mob.pullrefresh;

import android.view.View;

import com.xcheng.view.adapter.EasyHolder;

/**
 * Created by cc on 2016/10/8.
 */
public class BaseHolder<Z> extends EasyHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    /**
     * {@link HFRecyclerAdapter#onBindItemViewHolder(BaseHolder, int, Z)}
     *
     * @param data 被绑定的数据
     */
    public void setData(Z data) {
    }

    public void toActivity(Class cls) {
        toActivity(cls, null);
    }
}
