/**
 *
 */
package com.hletong.mob.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hletong.mob.R;
import com.xcheng.view.adapter.EasyFragmentAdapter;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.controller.IPagerView;
import com.xcheng.view.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础Fragment，提供公有方法
 *
 * @author Lean @date:2014-8-4
 */
public abstract class BaseViewPagerFragment extends BaseFragment implements IPagerView {
    protected ViewPager mViewPager;
    protected EasyFragmentAdapter mTabsAdapter;
    protected PagerSlidingTabStrip mIndicator;

    @Override
    public void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.initView(savedInstanceState);
        mViewPager = findViewById(R.id.ev_id_viewpager);
        mViewPager.setOffscreenPageLimit(getScreenPageLimit());
        List<TabInfo> tabInfos = new ArrayList<>();
        getTabInfos(tabInfos);
        mTabsAdapter = new EasyFragmentAdapter(getChildFragmentManager(), getContext(), tabInfos) {
            @NonNull
            @Override
            public View getTabView(int position) {
                return createTabView(position, mTabsAdapter.getTabInfo(position));
            }
        };
        mViewPager.setAdapter(mTabsAdapter);
        mIndicator = findViewById(R.id.ev_id_tab_indicator);
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    public int getScreenPageLimit() {
        return DEFAULT_PAGE_LIMIT;
    }
}
