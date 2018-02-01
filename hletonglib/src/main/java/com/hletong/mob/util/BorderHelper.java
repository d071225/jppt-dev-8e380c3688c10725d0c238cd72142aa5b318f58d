package com.hletong.mob.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ddq on 2016/12/19.
 */
public class BorderHelper {
    private Paint paintForBorder;

    public static final int DRAW_LEFT = 0x1;
    public static final int DRAW_TOP = 0x2;//是否绘制上面
    public static final int DRAW_RIGHT = 0x4;//
    public static final int DRAW_BOTTOM = 0x8;//是否绘制下面

    private int linePosition = 0;
    private int lineTopLeft = 0;
    private int lineTopRight = 0;
    private int lineBottomLeft = 0;
    private int lineBottomRight = 0;

    private View mView;

    private float lineWidth = 1;

    private int lineTopColor = Color.parseColor("#dae1e5");
    private int lineBottomColor = Color.parseColor("#dae1e5");

    public BorderHelper(View mView) {
        if (mView instanceof ViewGroup) {
            ViewGroup mViewGroup = (ViewGroup) mView;
            mViewGroup.setWillNotDraw(false);
        }
        this.mView = mView;
        paintForBorder = new Paint();
        paintForBorder.setAntiAlias(true);
    }

    public BorderHelper(int mLinePosition, View mView) {
        this(mView);
        linePosition = mLinePosition;
    }

    public void drawTop(Canvas canvas) {
        if ((linePosition & DRAW_TOP) == DRAW_TOP) {
            paintForBorder.setColor(lineTopColor);
            paintForBorder.setStrokeWidth(lineWidth);
            canvas.drawLine(lineTopLeft, lineWidth / 2, mView.getWidth() - lineTopRight, lineWidth / 2, paintForBorder);
        }
    }

    public void drawBottom(Canvas canvas) {
        if ((linePosition & DRAW_BOTTOM) == DRAW_BOTTOM) {
            paintForBorder.setColor(lineBottomColor);
            paintForBorder.setStrokeWidth(lineWidth);
            canvas.drawLine(lineBottomLeft, mView.getHeight() - lineWidth / 2, mView.getWidth() - lineBottomRight, mView.getHeight() - lineWidth / 2, paintForBorder);
        }
    }

    public void drawLeft(Canvas canvas) {
        if ((linePosition & DRAW_LEFT) == DRAW_LEFT) {
            paintForBorder.setColor(lineBottomColor);
            paintForBorder.setStrokeWidth(lineWidth);
            canvas.drawLine(0, lineTopLeft, 0, mView.getHeight() - lineBottomLeft, paintForBorder);
        }
    }

    public void drawRight(Canvas canvas) {
        if ((linePosition & DRAW_RIGHT) == DRAW_RIGHT) {
            paintForBorder.setColor(lineBottomColor);
            paintForBorder.setStrokeWidth(lineWidth);
            final float x = mView.getWidth() - paintForBorder.getStrokeWidth();
            canvas.drawLine(x, lineTopRight, x, mView.getHeight() - lineBottomRight, paintForBorder);
        }
    }

    public void setLineStatus(int mLinePosition, float mLineWidth, int mLineTopColor, int mLineBottomColor) {
        this.linePosition = mLinePosition;
        this.lineWidth = mLineWidth;
        this.lineTopColor = mLineTopColor;
        this.lineBottomColor = mLineBottomColor;
    }

    public void setLinePosition(int mLineTopLeft, int mLineTopRight, int mLineBottomLeft, int mLineBottomRight) {
        setLineTopLeft(mLineTopLeft);
        setLineTopRight(mLineTopRight);
        setLineBottomLeft(mLineBottomLeft);
        setLineBottomRight(mLineBottomRight);
    }

    public void setLineTopLeft(int mLineTopLeft) {
        lineTopLeft = mLineTopLeft;
        mView.invalidate();
    }

    public void setLineTopRight(int mLineTopRight) {
        lineTopRight = mLineTopRight;
        mView.invalidate();
    }

    public void setLineBottomLeft(int mLineBottomLeft) {
        lineBottomLeft = mLineBottomLeft;
        mView.invalidate();
    }

    public void setLineBottomRight(int mLineBottomRight) {
        lineBottomRight = mLineBottomRight;
        mView.invalidate();
    }

    public void setLineWidth(float mLineWidth) {
        lineWidth = mLineWidth;
        mView.invalidate();
    }

    public void setLineTopColor(int mLineTopColor) {
        lineTopColor = mLineTopColor;
        mView.invalidate();
    }

    public void setLineBottomColor(int mLineBottomColor) {
        lineBottomColor = mLineBottomColor;
        mView.invalidate();
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public int getLineTopColor() {
        return lineTopColor;
    }

    public int getLineBottomColor() {
        return lineBottomColor;
    }
}
