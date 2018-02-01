package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.BiddingHistoryAdapter;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.model.CBHistoryItem;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.HistoryHeader;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;

/**
 * Created by ddq on 2016/12/30.
 * 竞价中和竞价历史
 */

public class CompetitiveBiddingHistoryFragment extends BaseRefreshFragment<CBHistoryItem> {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean hasHeader = getArguments().getBoolean("hasHeader", false);

        if (hasHeader) {
            new HistoryHeader((ViewGroup) getActivity().findViewById(R.id.layout_root), 1);
        }
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CBHistoryItem> createAdapter() {
        return new BiddingHistoryAdapter(getContext(), null, this);
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.BID_LIST);
    }


    @Override
    public ParamsHelper getRequestJson(boolean isRefresh) {
        return getCommonRequestValue(isRefresh).put("statusQueryType", getArguments().getInt("statusQueryType", 1));
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_cb).setCustomState(RequestState.NO_DATA, "暂无投标信息");
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM) {
            requestData(true);
        }
    }
}
