package com.hletong.hyc.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class TimeLineItem extends RecyclerView.ItemDecoration {

    private int mItemSize = 1;
    private Paint mPaint;


    public TimeLineItem(Context context) {
        mItemSize = (int) TypedValue.applyDimension(mItemSize, TypedValue.COMPLEX_UNIT_DIP, context.getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#dae1e5"));
         /*设置填充*/
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View child = parent.getChildAt(i);
            int left = child.getPaddingLeft();
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            //画时间轴
            int top = child.getTop();
            int height = child.getHeight();
            int paddingTop = child.getPaddingTop();
            int buttom = top + mItemSize;
            c.drawCircle((float) (left / 2.0), buttom + paddingTop + 15, 8, mPaint);
            c.drawLine((float) (left / 2.0), buttom + paddingTop + 23, (float) (left / 2.0), buttom + height+paddingTop+8 , mPaint);

            //画下划线
            int top1 = child.getBottom() + params.bottomMargin;
            int bottom = top1 + mItemSize;
            c.drawLine(left, top1, right, bottom,mPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int left = child.getPaddingLeft();

            int top = child.getTop();
            int paddingTop = child.getPaddingTop();
            int buttom = top + mItemSize;

            c.drawCircle((float) (left / 2.0), buttom + paddingTop + 15, 8, mPaint);
            mPaint.setColor(Color.parseColor("#dae1e5"));
        }
    }
    //    偏移量设置
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

    }
}
