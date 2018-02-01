package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.hletong.hyc.R;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.mob.base.BaseFragment;
import com.hletong.mob.base.ImagePagerAdapter;
import com.xcheng.view.widget.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页
 * Created by chengxin on 2017/4/11.
 */
public class GuideFragment extends BaseFragment implements
        ViewPager.OnPageChangeListener {
    // 定义ViewPager对象
    @BindView(R.id.ev_id_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.ev_id_tab_indicator)
    PagerSlidingTabStrip mIndicator;
    @BindView(R.id.btn_enjoy)
    Button btnEnjoy;
    private boolean isDragging;
    private OnRemoveGuideListener mOnRemoveGuideListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this, getView());
        // 实例化ViewPager适配器
        int[] pics = AppTypeConfig.getGuidePic();
        ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(getContext(), pics, R.drawable.nav_dot);
        // 设置数据
        mViewPager.setAdapter(pagerAdapter);
        // 设置监听
        mViewPager.addOnPageChangeListener(this);
        mIndicator.setViewPager(mViewPager);
    }

    @OnClick(R.id.btn_enjoy)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (mOnRemoveGuideListener != null) {
            mOnRemoveGuideListener.onRemoved();
        }
    }

    /**
     * 当滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                if (mViewPager.getCurrentItem() == getAdapterCount() - 1) {
                    isDragging = true;
                } else {
                    isDragging = false;
                }
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                if (isDragging
                        && mViewPager.getCurrentItem() == getAdapterCount() - 1) {
                }
                break;
        }
    }

    /**
     * 当当前页面被滑动时调用
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    private int getAdapterCount() {
        if (mViewPager.getAdapter() != null) {
            return mViewPager.getAdapter().getCount();
        }
        return -1;
    }

    /**
     * 当新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int position) {
        btnEnjoy.setVisibility(position == getAdapterCount() - 1 ? View.VISIBLE : View.INVISIBLE);
    }

    public void setOnRemoveGuideListener(OnRemoveGuideListener onRemoveGuideListener) {
        mOnRemoveGuideListener = onRemoveGuideListener;
    }

    public interface OnRemoveGuideListener {
        void onRemoved();
    }
}
