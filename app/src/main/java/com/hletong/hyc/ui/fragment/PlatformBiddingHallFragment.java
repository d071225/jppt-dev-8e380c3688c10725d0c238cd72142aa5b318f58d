package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.PlatformBiddingAdapter;
import com.hletong.hyc.contract.CBRoundContract;
import com.hletong.hyc.model.CBPlatformRoundItem;
import com.hletong.hyc.presenter.PlatformBiddingPresenter;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.DividerItemDecoration;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by dongdaqing on 2017/7/19.
 * 竞价大厅（平开）
 */

public class PlatformBiddingHallFragment extends BaseRefreshFragment<CBPlatformRoundItem> implements CBRoundContract.View<CBPlatformRoundItem> {
    private BiddingHallCommon mCommon;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCommon = new BiddingHallCommon(new PlatformBiddingPresenter(this), this, view);
        mCommon.onViewCreated();
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CBPlatformRoundItem> createAdapter() {
        return new PlatformBiddingAdapter(getContext(), getArguments().getString("unit"), getArguments().getInt("bookRefType", -1));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bidding_hall;
    }

    @Override
    public void timeLoaded(long end, String remain) {
        mCommon.timeLoaded(end, remain);
    }

    @Override
    public void cargoLoaded(String roundUUID, String bidPrice, boolean canModifyBidPrice) {
        mCommon.cargoLoaded(roundUUID, bidPrice, canModifyBidPrice);
    }

    @Override
    public void bidPriceChanged(String money) {
        mCommon.bidPriceChanged(money);
    }

    @Override
    protected boolean isAutoLoad() {
        return false;
    }

    @Override
    protected boolean onlyView() {
        return true;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCommon.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCommon.onResume();
    }
}
