package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.hletong.hyc.adapter.CBAdapter;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by ddq on 2016/12/30.
 * 竞价待办列表
 */

public class CompetitiveBiddingListFragment extends BaseSourceListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history, menu);
        MenuItem mi = menu.findItem(R.id.history);
        mi.setTitle(R.string.bidding_history);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle mBundle = new Bundle();
        mBundle.putInt("statusQueryType", 2);
        mBundle.putBoolean("hasHeader", true);
        toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.bidding_history), CompetitiveBiddingHistoryFragment.class, mBundle), null);
        return true;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Source> createAdapter() {
        return new CBAdapter(getContext(), null, this);
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.COMPETITIVE_BIDDING_LIST);
    }

    @Override
    public ParamsHelper getRequestJson(boolean isRefresh) {
        return getCommonRequestValue(isRefresh)
                .put("type", "06")
                .put("status", "2");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_root);
        TextView bidding = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.common_button, null);
        bidding.setText(R.string.bidding);
        bidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                mBundle.putInt("statusQueryType", 1);
                mBundle.putBoolean("hasHeader", false);
                toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.bidding), CompetitiveBiddingHistoryFragment.class, mBundle), null);
            }
        });
        ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(48));
        mLinearLayout.addView(bidding, mLayoutParams);
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_cb).setCustomState(RequestState.NO_DATA, "暂无竞价投标");
    }
}
