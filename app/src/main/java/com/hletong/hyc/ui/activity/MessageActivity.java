package com.hletong.hyc.ui.activity;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.fragment.MessageCollectionFragment;
import com.hletong.hyc.ui.fragment.UpcomingMessageFragment;
import com.hletong.mob.base.BaseViewPagerActivity;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.util.LocalDisplay;

import java.util.List;

/**
 * Created by ddq on 2016/10/28.
 * 消息界面
 */

public class MessageActivity extends BaseViewPagerActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_switch;
    }

    @Override
    public void getTabInfos(List<TabInfo> tabInfos) {
        tabInfos.add(new TabInfo(UpcomingMessageFragment.class.getSimpleName(), getString(R.string.tab_upcoming), R.drawable.nav_left_tab_selector, UpcomingMessageFragment.class));
        tabInfos.add(new TabInfo(MessageCollectionFragment.class.getSimpleName(), getString(R.string.tab_message),R.drawable.nav_right_tab_selector, MessageCollectionFragment.class));

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
