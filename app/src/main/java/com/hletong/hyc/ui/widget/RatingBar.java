package com.hletong.hyc.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hletong.hyc.R;
import com.orhanobut.logger.Logger;

/**
 * Created by dongdaqing on 2017/5/11.
 * 自定义星星bar
 */

public class RatingBar extends LinearLayout implements View.OnClickListener {
    private int starMargin;
    private int stars;
    private float progress;
    private boolean asIndicator;
    private int progressDrawable;
    private OnRatingBarChangeListener mOnRatingBarChangeListener;

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        TypedArray tv = getContext().obtainStyledAttributes(attrs, R.styleable.RatingBar);
        stars = tv.getInteger(R.styleable.RatingBar_stars, 5);
        starMargin = tv.getDimensionPixelSize(R.styleable.RatingBar_starMargin, 0);
        float prog = tv.getFloat(R.styleable.RatingBar_progress, 0);
        progressDrawable = tv.getResourceId(R.styleable.RatingBar_starDrawable, R.drawable.star);
        asIndicator = tv.getBoolean(R.styleable.RatingBar_asIndicator, false);
        tv.recycle();

        initStars();
        setProgress(prog);
    }

    private void initStars() {
        for (int i = 0; i < stars; i++) {
            AppCompatImageView iv = new AppCompatImageView(getContext());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = starMargin;
            }
            iv.setImageResource(progressDrawable);
//            Logger.d("level => " + iv.getDrawable().getLevel());
            if (!asIndicator)
                iv.setOnClickListener(this);
            iv.setTag(i);
            addView(iv, params);
        }
    }

    /**
     * @param progress 范围 0 ~ 1
     */
    public void setProgress(float progress) {
//        Log("local progress => " + this.progress + " new progress => " + progress);
        if (this.progress != progress) {
//            Log("progress => " + progress);
            this.progress = progress;
            float real = progress * stars;
            int left = (int) Math.floor(real);
            float fraction = real - left;
            for (int i = 0; i < getChildCount(); i++) {
                ImageView iv = (ImageView) getChildAt(i);
                Drawable cd = iv.getDrawable();
                cd.mutate();//避免状态共享
                if (i < left) {
                    cd.setLevel(10000);
                } else if (i > left) {
                    cd.setLevel(0);
                } else {
                    cd.setLevel((int) (10000 * fraction));
                }
                //解决5.0以下 LayperDrawable 子layer bound 为0 ，不显示的问题
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && cd instanceof LayerDrawable) {
                    LayerDrawable ld = (LayerDrawable) cd;
                    Rect rect = ld.getBounds();
                    for (int layer = 0; layer < ld.getNumberOfLayers(); layer++) {
                        ld.getDrawable(layer).setBounds(rect);
                    }
                    cd.invalidateSelf();
                }
            }
            if (mOnRatingBarChangeListener != null)
                mOnRatingBarChangeListener.onRatingChanged(progress, stars);
        }
    }

    public int getRating() {
        return (int) (progress * stars);
    }

    public void setOnRatingBarChangeListener(OnRatingBarChangeListener onRatingBarChangeListener) {
        mOnRatingBarChangeListener = onRatingBarChangeListener;
    }

    private void Log(String message) {
//        Log.d("test", message);
    }

    @Override
    public void onClick(View v) {
        float index = (int) v.getTag() + 1;
        setProgress(index / stars);
    }

    public interface OnRatingBarChangeListener {
        void onRatingChanged(float progress, int stars);
    }
}
