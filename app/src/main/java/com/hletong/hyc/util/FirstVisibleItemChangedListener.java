package com.hletong.hyc.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ddq on 2017/3/6.
 */

public class FirstVisibleItemChangedListener extends RecyclerView.OnScrollListener {
    private int firstVisibleItem = -1;
    private VisibleItemChanged vic;

    public FirstVisibleItemChangedListener(VisibleItemChanged vic) {
        this.vic = vic;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
            if (this.firstVisibleItem != firstVisibleItem) {
                int tmpOld = this.firstVisibleItem;
                this.firstVisibleItem = firstVisibleItem;
                vic.onItemChanged(tmpOld, this.firstVisibleItem);
            }
        }
    }

    public interface VisibleItemChanged {
        void onItemChanged(int oldFirstItem, int newFirstItem);
    }
}
