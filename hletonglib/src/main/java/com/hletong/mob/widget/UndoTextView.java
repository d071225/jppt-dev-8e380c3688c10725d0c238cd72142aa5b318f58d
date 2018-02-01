package com.hletong.mob.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.hletong.mob.R;
import com.hletong.mob.util.BitmapUtils;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by cc on 2016/12/27.
 */
public class UndoTextView extends AppCompatTextView {
    private float radius;
    /***
     * 相对于DrawableTop右上角的位置
     */
    private float toRightTopX;
    private float toRightTopY;
    private int undoMessage;
    private Bitmap bubbleBitmap;
    private Paint textPaint;

    public UndoTextView(Context context) {
        this(context, null);
    }

    public UndoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public UndoTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UndoTextView);
        radius = LocalDisplay.dp2px(10);//array.getDimension(R.styleable.UndoTextView_radius, LocalDisplay.dp2px(5));
        toRightTopX = array.getDimension(R.styleable.UndoTextView_toRightTopX, 0);
        toRightTopY = array.getDimension(R.styleable.UndoTextView_toRightTopY, 0);
        Drawable bubbleDrawable = array.getDrawable(R.styleable.UndoTextView_bubbleDrawableId);
        if (bubbleDrawable != null) {
            bubbleBitmap = BitmapUtils.convert(bubbleDrawable);
        } else {
            bubbleBitmap = BitmapUtils.convert(ContextCompat.getDrawable(context, R.drawable.icon_bubble));
        }
        array.recycle();
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT); // 设置字体
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (undoMessage > 0) {
            int bubbleWidth = bubbleBitmap.getWidth();
            int bubbleHeight = bubbleBitmap.getHeight();
            float left = getDrawableTopX() - bubbleWidth / 2 + toRightTopX;
            float top = getPaddingTop() - bubbleHeight / 2 + toRightTopY;
            textPaint.setColor(Color.RED);
            // canvas.drawCircle(cx, cy, radius, textPaint);
            canvas.drawBitmap(bubbleBitmap, left, top, null);
            textPaint.setColor(Color.WHITE);
            String undoText;
            if (undoMessage < 10) {
                undoText = String.valueOf(undoMessage);
                textPaint.setTextSize(LocalDisplay.dp2px(14));
            } else if (undoMessage < 100) {
                undoText = String.valueOf(undoMessage);
                textPaint.setTextSize(LocalDisplay.dp2px(12));
            } else {
                undoText = "99+";
                textPaint.setTextSize(LocalDisplay.dp2px(10));
            }
            canvas.drawText(undoText,
                    left + (bubbleWidth - textPaint.measureText(undoText)) / 2.0f,
                    top + (bubbleHeight-bubbleHeight/10 - (textPaint.descent() + textPaint.ascent())) / 2.0f,
                    textPaint);
        }
    }

    public int getDrawableTopX() {
        Drawable topDrawable = getCompoundDrawables()[1];
        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        return getPaddingLeft() + contentWidth / 2 + topDrawable.getIntrinsicWidth() / 2;
    }

    public void setUndoMessage(int count) {
        this.undoMessage = count;
        invalidate();
    }
}
