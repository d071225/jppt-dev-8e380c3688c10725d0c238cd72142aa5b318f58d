package com.hletong.hyc.ui.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.xcheng.view.util.LocalDisplay;

@SuppressLint("AppCompatCustomView")
public class DragImageView extends ImageView {
    private int parentHeight;
    private int parentWidth;
    private int lastX;
    private int lastY;
    private boolean isDrag;
    public static final int DURATION = 300;
    public static final int LIMIT = LocalDisplay.dp2px(50);
    public static final int TOUCHSLOP = LocalDisplay.dp2px(10);

    public DragImageView(Context context) {
        super(context);
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (parentHeight <= 0 || parentWidth == 0) {
                    isDrag = false;
                    break;
                }
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance < TOUCHSLOP && !isDrag) {
                    break;
                } else {
                    isDrag = true;
                }
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = y < 0 ? 0 : y + getHeight() > parentHeight ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                // Log.i("print", "isDrag=" + isDrag + "getX=" + getX() + ";getY=" + getY() + ";parentWidth=" + parentWidth);
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                    if (getX() >= LIMIT && getY() <= LIMIT && getX() <= parentWidth - getWidth() - LIMIT) {
                        //靠上吸附
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "y", getY(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(DURATION);
                        oa.start();
                    } else if (getX() >= LIMIT && parentHeight - getHeight() - getY() <= LIMIT && getX() <= parentWidth - getWidth() - LIMIT) {
                        //靠下吸附
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(DURATION)
                                .yBy(parentHeight - getHeight() - getY())
                                .start();
                    } else if (rawX >= parentWidth / 2) {
                        //靠右吸附
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(DURATION)
                                .xBy(parentWidth - getWidth() - getX())
                                .start();
                    } else {
                        //靠左吸附
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(DURATION);
                        oa.start();
                    }
                }
                break;
        }
        //如果是拖拽则消s耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event);
    }
}