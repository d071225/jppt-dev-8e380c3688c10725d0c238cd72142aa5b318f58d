package com.hletong.hyc.ui.fragment;

import android.support.annotation.NonNull;

import com.hletong.hyc.adapter.CargoHistoryAdapter;
import com.hletong.hyc.model.CargoSourceItem;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2017/4/13.
 * 货方货源历史
 */

public class CargoOrderHistoryFragment extends BaseRefreshFragment<CargoSourceItem> {
    @NonNull
    @Override
    public HFRecyclerAdapter<CargoSourceItem> createAdapter() {
        return new CargoHistoryAdapter(getContext());
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.ORDER_HISTORY_CARGO);
    }

}
