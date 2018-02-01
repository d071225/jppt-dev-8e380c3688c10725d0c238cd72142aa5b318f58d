package com.hletong.mob.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.widget.PagerSlidingTabStrip;

/**
 * http://www.cnblogs.com/kobe8/p/4343478.html
 *
 * @author xincheng
 */
public abstract class BasePagerAdapter extends PagerAdapter implements PagerSlidingTabStrip.TabAdapter {

    protected abstract View getPagerView(int position);

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View pagerView = getPagerView(position);
        view.addView(pagerView);
        return pagerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
