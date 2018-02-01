package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hletong.hyc.adapter.CopySourceAdapter;
import com.hletong.hyc.model.CargoSourceItem;
import com.hletong.hyc.ui.activity.SearchActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/6.
 * 复制货源
 */

public class CopySourceFragment extends BaseRefreshFragment<CargoSourceItem> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.phone_selector, menu);
//        MenuItem menuItem = menu.findItem(R.id.call);
//        menuItem.setTitle("搜索");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        toActivity(SearchActivity.class, null, null);
        return true;
    }

    @Override
    protected String getRequestUrl() {
        if (getArguments() == null) {
            return Constant.getUrl(Constant.PERSONAL_SOURCE_LIST);
        }
        return Constant.getUrl(Constant.PERSONAL_SOURCE_LIST) + "?" + getArguments().getString("extra");
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CargoSourceItem> createAdapter() {
        return new CopySourceAdapter(getContext(), this);
    }
}
