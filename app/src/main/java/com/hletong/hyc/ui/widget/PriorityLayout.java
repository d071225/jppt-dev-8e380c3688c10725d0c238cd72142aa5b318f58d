package com.hletong.hyc.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dongdaqing on 2017/5/9.
 * 优先级布局，样式为单行或者单列，childView通过priority属性指定优先级
 * 优先级高的child会优先占用空间，优先级低的后考虑
 *
 * 适用的情况，A,B,C三个TextView从左到右排列，其中A的内容会变化，b,c内容也会变化，
 * 为了保证无论A的内容怎么变化b和C都能够显示所有内容，设定B，C优先级高于A即可
 *
 * 关于为什么不使用LinearLayout的weight属性，确实用LinearLayout能实现想要的效果，
 * 但是这种做法在RecyclerView里面Item复用会有BUG，Item不能够重新计算TextView的大小，而是使用了缓存的大小
 */

public class PriorityLayout extends ViewGroup {
    private Orientation mOrientation;
    private ArrayList<Priority> priorities;
    private static final Comparator<Priority> comparator = new Comparator<Priority>() {
        @Override
        public int compare(Priority o1, Priority o2) {
            return o1.priority - o2.priority;
        }
    };

    public PriorityLayout(Context context) {
        this(context, null);
    }

    public PriorityLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PriorityLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public PriorityLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PriorityLayout);
        if (ta.getInt(R.styleable.PriorityLayout_orientation, 0) == 1)
            mOrientation = Orientation.HORIZONTAL;
        ta.recycle();

        priorities = new ArrayList<>();
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mOrientation == Orientation.VERTICAL)
            layoutVertical(l, t, r, b);
        else {
            layoutHorizontal(l, t, r, b);
        }
    }

    private void layoutVertical(int l, int t, int r, int b) {
        int top = getPaddingTop();
        int left = getPaddingLeft();
        int width = r - l - getPaddingLeft() - getPaddingRight();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == GONE)
                continue;

            LayoutParams params = (LayoutParams) view.getLayoutParams();
            int vt = top + params.topMargin;
            int vl;
            if (params.isCenterInParent()) {
                vl = left + (width - view.getMeasuredWidth()) / 2;
            } else {
                vl = left + params.leftMargin;
            }
            view.layout(vl, vt, vl + view.getMeasuredWidth(), vt + view.getMeasuredHeight());
            top = vt + view.getMeasuredHeight() + params.bottomMargin;
        }
    }

    private void layoutHorizontal(int l, int t, int r, int b) {
        int top = getPaddingTop();
        int left = getPaddingLeft();
        int height = b - t - getPaddingTop() - getPaddingBottom();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == GONE)
                continue;
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            int vt;
            int vl = left + params.leftMargin;
            if (params.isCenterInParent()) {
                vt = (height - view.getMeasuredHeight()) / 2 + top;
            } else {
                vt = top + params.topMargin;
            }
            view.layout(vl, vt, vl + view.getMeasuredWidth(), vt + view.getMeasuredHeight());
            left = vl + view.getMeasuredWidth() + params.rightMargin;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cc = getChildCount();
        priorities.clear();
        for (int i = 0; i < cc; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            priorities.add(params.getPriority(i));
        }
        Collections.sort(priorities, comparator);

        if (mOrientation == Orientation.VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }

        int width = getPaddingLeft() + getPaddingRight();
        int height = getPaddingTop() + getPaddingBottom();

        for (int i = 0; i < cc; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != GONE) {
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                if (mOrientation == Orientation.VERTICAL) {
                    width = Math.max(width, view.getMeasuredWidth() + params.leftMargin + params.rightMargin);
                    height += view.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                } else {
                    width += view.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                    height = Math.max(height, view.getMeasuredHeight() + params.topMargin + params.bottomMargin);
                }
            }
        }

        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        if (widthSpec == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = Math.max(width, getSuggestedMinimumWidth());
        }
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        if (heightSpec == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = Math.max(height, getSuggestedMinimumHeight());
        }
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int widthUsed = getPaddingLeft() + getPaddingRight();
        for (int i = priorities.size() - 1; i >= 0; i--) {
            View view = getChildAt(priorities.get(i).index);
            if (view.getVisibility() != GONE) {
                measureChildWithMargins(view, widthMeasureSpec, widthUsed, heightMeasureSpec, 0);
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                widthUsed += view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
        }
    }

    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int heightUsed = getPaddingTop() + getPaddingBottom();
        for (int i = priorities.size() - 1; i >= 0; i--) {
            View view = getChildAt(priorities.get(i).index);
            if (view.getVisibility() != GONE) {
                measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                heightUsed += view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }
        }
    }

    private enum Orientation {
        VERTICAL, HORIZONTAL
    }

    private static class Priority {
        int index;
        int priority;

        public Priority(int index, int priority) {
            this.index = index;
            this.priority = priority;
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        private int priority;
        private boolean centerInParent;
        private Priority p;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.PriorityLayout_Layout);
            priority = ta.getInt(R.styleable.PriorityLayout_Layout_priority, 0);
            centerInParent = ta.getBoolean(R.styleable.PriorityLayout_Layout_centerInParent, true);
            ta.recycle();
        }

        public LayoutParams(@Px int width, @Px int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public Priority getPriority(int index) {
            if (p == null) {
                p = new Priority(index, priority);
            }
            p.index = index;
            return p;
        }

        public boolean isCenterInParent() {
            return centerInParent;
        }
    }
}
