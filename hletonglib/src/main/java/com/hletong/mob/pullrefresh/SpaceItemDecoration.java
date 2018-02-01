package com.hletong.mob.pullrefresh;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ddq on 2016/12/19.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean showEndSpace;

    public SpaceItemDecoration(int space) {
        this(space, false);
    }

    public SpaceItemDecoration(int space, boolean showEndSpace) {
        this.space = space;
        this.showEndSpace = showEndSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (showEndSpace || position != parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, space);
        }
    }
}
