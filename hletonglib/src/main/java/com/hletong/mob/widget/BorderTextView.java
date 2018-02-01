package com.hletong.mob.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hletong.mob.R;
import com.hletong.mob.util.BorderHelper;

/**
 * Created by dongdaqing  changed by cx on 16/10/13.
 */
public class BorderTextView extends TextView {
    private BorderHelper mBorderHelper;

    public BorderTextView(Context context) {
        this(context, null);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mBorderHelper = new BorderHelper(this);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BorderTextView);
        int linePosition = array.getInt(R.styleable.BorderTextView_linePosition, 0);

        int lineTopColor = array.getColor(R.styleable.BorderTextView_lineTopColor, mBorderHelper.getLineTopColor());
        int lineBottomColor = array.getColor(R.styleable.BorderTextView_lineBottomColor, mBorderHelper.getLineBottomColor());
        float lineWidth = array.getDimension(R.styleable.BorderTextView_lineWidth, mBorderHelper.getLineWidth());

        int lineTopLeft = array.getDimensionPixelSize(R.styleable.BorderTextView_lineTopLeft, 0);
        int lineTopRight = array.getDimensionPixelSize(R.styleable.BorderTextView_lineTopRight, 0);
        int lineBottomLeft = array.getDimensionPixelSize(R.styleable.BorderTextView_lineBottomLeft, 0);
        int lineBottomRight = array.getDimensionPixelSize(R.styleable.BorderTextView_lineBottomRight, 0);

        array.recycle();

        mBorderHelper.setLineStatus(linePosition, lineWidth, lineTopColor, lineBottomColor);
        mBorderHelper.setLinePosition(lineTopLeft, lineTopRight, lineBottomLeft, lineBottomRight);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mBorderHelper.drawTop(canvas);
        mBorderHelper.drawBottom(canvas);
    }

    public BorderHelper getBorderHelper() {
        return mBorderHelper;
    }
}
