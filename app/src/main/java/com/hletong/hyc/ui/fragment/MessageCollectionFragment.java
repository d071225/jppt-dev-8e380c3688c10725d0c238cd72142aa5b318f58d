package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.mob.base.BaseViewPagerFragment;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.util.LocalDisplay;

import java.util.List;

/**
 * Created by ddq on 2016/12/12.
 * 消息列表
 */
public class MessageCollectionFragment extends BaseViewPagerFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message_collection;
    }

    public static Bundle getBundle(String status, String inform_call_type) {
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        bundle.putString("inform_call_type", inform_call_type);
        return bundle;
    }

    @Override
    public void getTabInfos(List<TabInfo> tabInfos) {
        AppTypeConfig.getMessageTabs(tabInfos);
    }

    @NonNull
    @Override
    public View createTabView(int position, TabInfo tabInfo) {
        TextView tabView = new TextView(getContext());
        tabView.setTextSize(14);
        tabView.setText(tabInfo.title);
        tabView.setBackgroundResource(tabInfo.bgResId);
        tabView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.common_tab_text_color));
        tabView.setPadding(LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(15), 0);
        tabView.setGravity(Gravity.CENTER);
        return tabView;
    }
}
