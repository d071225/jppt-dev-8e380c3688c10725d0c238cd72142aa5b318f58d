package com.hletong.hyc.ui.fragment;

import android.support.annotation.NonNull;

import com.hletong.hyc.adapter.HistoryOrderAdapter;
import com.hletong.hyc.model.HistoryOrder;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2017/1/6.
 */

public class HistoryOrderFragment extends BaseRefreshFragment<HistoryOrder> {
    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.ORDER_HISTORY_TRANSPORTER);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<HistoryOrder> createAdapter() {
        return new HistoryOrderAdapter(getContext());
    }
}
