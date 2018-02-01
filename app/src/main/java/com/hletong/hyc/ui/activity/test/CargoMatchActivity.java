package com.hletong.hyc.ui.activity.test;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.fragment.MessageCollectionFragment;
import com.hletong.mob.base.BaseViewPagerActivity;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.util.LocalDisplay;

import java.util.List;

/**
 * Created by Daiyy on 2018/2/1.
 */

public class CargoMatchActivity  extends BaseViewPagerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_switch;
    }

    @Override
    public void getTabInfos(List<TabInfo> tabInfos) {
        tabInfos.add(new TabInfo(CargoMatchFragment.class.getSimpleName(), getString(R.string.cargo_match), R.drawable.nav_left_tab_selector, CargoMatchFragment.class));
        tabInfos.add(new TabInfo(CargoMatchFragment.class.getSimpleName(), getString(R.string.cargo_existing),R.drawable.nav_right_tab_selector, CargoMatchFragment.class));
    }

    @NonNull
    @Override
    public View createTabView(int position, TabInfo tabInfo) {
        TextView tabView = new TextView(this);
        tabView.setTextSize(14);
        tabView.setText(tabInfo.title);
        tabView.setBackgroundResource(tabInfo.bgResId);
        tabView.setTextColor(ContextCompat.getColorStateList(this, R.color.nav_tab_color));
        tabView.setPadding(LocalDisplay.dp2px(12), 0, LocalDisplay.dp2px(12), 0);
        tabView.setGravity(Gravity.CENTER);
        return tabView;
    }
}
