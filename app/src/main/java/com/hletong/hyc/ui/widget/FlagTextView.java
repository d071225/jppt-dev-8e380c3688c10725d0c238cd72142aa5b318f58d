package com.hletong.hyc.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by ddq on 2017/3/3.
 * 保证宽度不小于高度
 */

public class FlagTextView extends AppCompatTextView {

    public FlagTextView(Context context) {
        super(context);
    }

    public FlagTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() > getMeasuredWidth()) {
            super.measure(
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(),MeasureSpec.EXACTLY),
                    getMeasuredHeightAndState()
            );
        }
    }
}
