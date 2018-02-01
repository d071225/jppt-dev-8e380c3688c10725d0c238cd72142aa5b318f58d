package com.hletong.hyc.ui.fragment;

import android.support.annotation.NonNull;

import com.hletong.hyc.adapter.SelfTradeContactAdapter;
import com.hletong.hyc.model.CargoContact;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by dongdaqing on 2017/5/22.
 */

public class SelfTradeContactFragment extends BaseRefreshFragment<CargoContact> {
    @NonNull
    @Override
    public HFRecyclerAdapter<CargoContact> createAdapter() {
        return new SelfTradeContactAdapter(getContext());
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.SELF_TRADE_CONTACT);
    }
}
