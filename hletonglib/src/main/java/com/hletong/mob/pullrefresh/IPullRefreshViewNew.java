package com.hletong.mob.pullrefresh;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;


import java.util.List;

/**
 * T代表列表item的数据类型
 * Created by cx on 2016/10/13.
 */
public interface IPullRefreshViewNew<T> {
    /**
     * UI层调用此方法请求列表数据
     **/
    void requestData(boolean refresh);

    @UiThread
    void refreshView(boolean isRefresh, List<T> data);

    /**
     * called after {@link #refreshView(boolean, List)})
     *
     * @param isRefresh 是否为刷新状态
     * @param state
     */
    @UiThread
    void complete(boolean isRefresh, UIState state);

    /***
     * 获取EmptyView
     **/
    EmptyView getEmptyView();

    /**
     * recyclerView获取列表展示所需的Adapter
     */
    @NonNull
    HFRecyclerAdapter<T> createAdapter();

    /****
     * for recyclerView
     *****/
    RecyclerView.LayoutManager getLayoutManager();

    RecyclerView.ItemDecoration getItemDecoration();

    RecyclerView.ItemAnimator getItemAnimator();
}
