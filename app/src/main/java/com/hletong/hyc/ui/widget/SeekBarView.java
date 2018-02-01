package com.hletong.hyc.ui.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.xcheng.view.util.LocalDisplay;

public class SeekBarView extends View {
    private static final int MAX = 100;
    private int mWidth;
    private int mHeight;

    private Paint paint;
    private Path path;
    private int dWidth;
    private int preCircleY;
    private int preCircieX;
    private boolean isTouchCircle;
    private OnTextSizeChangeListener onTextSizeListener;
    private int dHeight;
    private GestureDetector gestureDetector;
    public SeekBarView(Context context) {
        this(context, null);
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mWidth = LocalDisplay.widthPixel();
        dWidth = mWidth / 6;
        mHeight = 50;
        dHeight = mHeight / 2-10;
        preCircieX = dWidth;
        gestureDetector = new GestureDetector(context, onGestureListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
    }

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int singY = (int) e.getY();
            int singX = (int) e.getX();
            boolean lineOn = isLineOn(singX, singY);
            if (lineOn) {
                if (singX <= 2 * dWidth) {
                    preCircieX = dWidth;
                    if (onTextSizeListener != null) {
                        onTextSizeListener.setSmallSize();
                    }
                } else if (singX > 2 * dWidth && singX <= 4 * dWidth) {
                    preCircieX = 3 * dWidth;
                    if (onTextSizeListener != null) {
                        onTextSizeListener.setMiddleSize();
                    }
                } else if (singX > 4 * dWidth && singX < 5 * dWidth) {
                    preCircieX = 5 * dWidth;
                    if (onTextSizeListener != null) {
                        onTextSizeListener.setLargeSize();
                    }
                }
                invalidate();
            }
            return true;
        }
    };

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(dWidth, dHeight);
        path.lineTo(dWidth, dHeight + 10);

        path.lineTo(5 * dWidth, dHeight + 10);
        path.lineTo(5 * dWidth, dHeight);

        path.moveTo(3 * dWidth, dHeight + 10);
        path.lineTo(3 * dWidth, dHeight);

        canvas.drawPath(path, paint);

        preCircleY = dHeight + 10;
        drawCircle(canvas, preCircieX, preCircleY);
    }

    private void drawCircle(Canvas canvas, int preCircieX, int preCircleY) {
        canvas.drawCircle(preCircieX, preCircleY, 20, paint);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(preCircieX, preCircleY, 19, paint);
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int downX = (int) event.getX();
                int downY = (int) event.getY();
                isTouchCircle = isInner(downX, downY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTouchCircle) {
                    int moveX = (int) event.getX();
                    if (moveX < dWidth) {
                        moveX = dWidth;
                    } else if (moveX > 5 * dWidth) {
                        moveX = 5 * dWidth;
                    }
                    preCircieX = moveX;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isTouchCircle) {
                    int upX = (int) event.getX();
                    if (upX <= 2 * dWidth) {
                        preCircieX = dWidth;
                        if (onTextSizeListener != null) {
                            onTextSizeListener.setSmallSize();
                        }
                    } else if (upX > 2 * dWidth && upX <= 4 * dWidth) {
                        preCircieX = 3 * dWidth;
                        if (onTextSizeListener != null) {
                            onTextSizeListener.setMiddleSize();
                        }
                    } else if (upX > 4 * dWidth) {
                        preCircieX = 5 * dWidth;
                        if (onTextSizeListener != null) {
                            onTextSizeListener.setLargeSize();
                        }
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    public boolean isInner(int x, int y) {
        int dx = Math.abs(preCircieX - x);
        int dy = Math.abs(preCircleY - y);
        double hypot = Math.hypot(dx, dy);
        return hypot - 20 < 0;
    }

    public boolean isLineOn(int x, int y) {
        return y >= (dHeight - 10) && y <= (dHeight + 30) && x >= dWidth && x <= 5 * dWidth;
    }

    public interface OnTextSizeChangeListener {
        void setSmallSize();

        void setMiddleSize();

        void setLargeSize();
    }

    public void setOnTextSizeListener(OnTextSizeChangeListener onTextSizeListener) {
        this.onTextSizeListener = onTextSizeListener;
    }

    public void setProgress(int progress) {
        preCircieX = progress*(mWidth -2* dWidth)/MAX+ dWidth;
        invalidate();
    }
}
