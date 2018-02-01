package com.hletong.mob.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hletong.mob.R;
import com.hletong.mob.util.BorderHelper;

/**
 * Created by dongdaqing  changed by cx on 16/10/13.
 */
public class BorderLinearLayout extends LinearLayout {
    private BorderHelper mBorderHelper;

    public BorderLinearLayout(Context context) {
        this(context, null);
    }

    public BorderLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BorderLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mBorderHelper = new BorderHelper(this);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BorderLinearLayout);
        int linePosition = array.getInt(R.styleable.BorderLinearLayout_linePosition, 0);

        int lineTopColor = array.getColor(R.styleable.BorderLinearLayout_lineTopColor, mBorderHelper.getLineTopColor());
        int lineBottomColor = array.getColor(R.styleable.BorderLinearLayout_lineBottomColor, mBorderHelper.getLineBottomColor());
        float lineWidth = array.getDimension(R.styleable.BorderLinearLayout_lineWidth, mBorderHelper.getLineWidth());

        int lineTopLeft = array.getDimensionPixelSize(R.styleable.BorderLinearLayout_lineTopLeft, 0);
        int lineTopRight = array.getDimensionPixelSize(R.styleable.BorderLinearLayout_lineTopRight, 0);
        int lineBottomLeft = array.getDimensionPixelSize(R.styleable.BorderLinearLayout_lineBottomLeft, 0);
        int lineBottomRight = array.getDimensionPixelSize(R.styleable.BorderLinearLayout_lineBottomRight, 0);
        array.recycle();

        mBorderHelper.setLineStatus(linePosition, lineWidth, lineTopColor, lineBottomColor);
        mBorderHelper.setLinePosition(lineTopLeft, lineTopRight, lineBottomLeft, lineBottomRight);
    }

    /***
     * 保证能够展示，渲染在child上面
     **/
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mBorderHelper.drawTop(canvas);
        mBorderHelper.drawBottom(canvas);
        mBorderHelper.drawLeft(canvas);
        mBorderHelper.drawRight(canvas);
    }

    public BorderHelper getBorderHelper() {
        return mBorderHelper;
    }
}
