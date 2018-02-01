package com.hletong.hyc.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by ddq on 2017/3/14.
 * 这个类的子类具有两个功能：一、普通展示，二、item编辑
 */

public abstract class CargoForecastBaseSelectFragment<T extends Parcelable> extends BaseRefreshFragment<T> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isModeEdit())
            setHasOptionsMenu(true);
    }

    public static Bundle getDefaultBundle(boolean edit, int selected, String button, String editTitle) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("modeEdit", edit);
        bundle.putInt("selected", selected);
        bundle.putString("button", button);
        bundle.putString("editTitle", editTitle);
        return bundle;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.phone_selector, menu);
        MenuItem item = menu.findItem(R.id.call);
        item.setTitle("管理");
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        toActivity(
                CommonWrapFragmentActivity.class,
                CommonWrapFragmentActivity.getBundle(
                        getEditTitle(),
                        getClass(),
                        getDefaultBundle(true, -1, null, null)),
                100, null);
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isModeEdit() || getBottomButtonActionClass() == null) return;
        LinearLayout mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_root);
        TextView bidding = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.common_button, null);
        bidding.setText(getArguments().getString("button"));
        bidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //界面底部的大黑色按钮
                toActivity(getBottomButtonActionClass(), null, 101, null);
            }
        });
        ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(48));
        mLinearLayout.addView(bidding, mLayoutParams);
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.CARGO_FORECAST_SELECT_DATA);
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        mPtrFrameLayout.autoRefresh(true);
        //子类选择requestCode的时候不要使用下面这两个
        if (requestCode != 100 && requestCode != 101) {
            getActivity().setResult(Activity.RESULT_OK);
        }
    }

    protected boolean isModeEdit() {
        return getArguments() != null && getArguments().getBoolean("modeEdit", false);
    }

    protected int getSelected() {
        if (getArguments() != null)
            return getArguments().getInt("selected", -1);
        return -1;
    }

    protected String getEditTitle() {
        if (getArguments() != null)
            return getArguments().getString("editTitle");
        return "";
    }

    protected abstract Class getBottomButtonActionClass();
}
