package com.hletong.mob.pullrefresh;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int color;
    private Paint mPaint;

    public DividerItemDecoration(int mColor) {
        color = mColor;
        init();
    }

    public DividerItemDecoration() {
        color = Color.parseColor("#dae1e5");
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int count = parent.getChildCount();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (i < count - 1)
                c.drawLine(0, view.getBottom() - 1, parent.getRight(), view.getBottom() - 1, mPaint);
        }
    }
}