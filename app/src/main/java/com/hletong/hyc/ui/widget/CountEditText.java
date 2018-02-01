package com.hletong.hyc.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Created by dongdaqing on 2017/5/17.
 */

public class CountEditText extends AppCompatEditText {
    private final int RES[] = {android.R.attr.maxLength};
    private Paint mPaint;
    private int extra;
    private int maxLength;

    public CountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray tv = getContext().obtainStyledAttributes(attrs, RES);
        maxLength = tv.getInt(0, 0);
        tv.recycle();

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#c2c9cc"));
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        extra = (int) (fm.descent - fm.ascent + 16);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() + extra, MeasureSpec.EXACTLY)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final String value = String.valueOf(maxLength - getText().length());
        canvas.drawText(
                value,
                getWidth() - mPaint.measureText(value) - getPaddingRight(),
                getHeight() - getPaddingBottom() - mPaint.getFontMetrics().descent,
                mPaint);
    }
}
