package com.hletong.hyc.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hletong.hyc.adapter.TransportHistoryAdapter;
import com.hletong.hyc.model.HistoryOrderDetail;
import com.hletong.hyc.ui.widget.TimeLineItem;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/1/6.
 */

public class OrderDetailsFragment extends BaseRefreshFragment<HistoryOrderDetail> {
    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.ORDER_HISTORY_DETAILS);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<HistoryOrderDetail> createAdapter() {
        return new TransportHistoryAdapter(getContext(),null);
    }

    @Override
    public ParamsHelper getRequestJson(boolean isRefresh){
        return new ParamsHelper().put("tradeUuid",getArguments().getString("tradeUuid"));
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new TimeLineItem(getContext());
    }

    @Override
    protected String getEntry() {
        return null;
    }
}
