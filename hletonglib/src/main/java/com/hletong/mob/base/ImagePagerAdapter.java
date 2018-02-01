package com.hletong.mob.base;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xcheng.view.util.LocalDisplay;

/**
 * http://www.cnblogs.com/kobe8/p/4343478.html
 *
 * @author xincheng
 */
public class ImagePagerAdapter extends BasePagerAdapter {
    private Context mContext;
    private View[] mViews;
    private int[] mDrawableRes;
    private int mDotDrawableId;

    public ImagePagerAdapter(Context context, @NonNull @Size(min = 1) int[] drawableRes, @DrawableRes int dotDrawableId) {
        this.mContext = context;
        this.mDrawableRes = drawableRes;
        mViews = new View[drawableRes.length];
        mDotDrawableId = dotDrawableId;
    }

    @Override
    protected View getPagerView(int position) {
        //延迟初始化
        if (mViews[position] == null) {
            // 定义一个布局并设置参数
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(params);
            iv.setImageResource(mDrawableRes[position]);
            mViews[position] = iv;
        }
        return mViews[position];
    }

    @Override
    public int getCount() {
        return mViews.length;
    }

    @NonNull
    @Override
    public View getTabView(int position) {
        ImageView dotView = new ImageView(mContext);
        dotView.setImageResource(mDotDrawableId);
        dotView.setPadding(LocalDisplay.dp2px(3), 0, LocalDisplay.dp2px(3), 0);
        return dotView;
    }
}
