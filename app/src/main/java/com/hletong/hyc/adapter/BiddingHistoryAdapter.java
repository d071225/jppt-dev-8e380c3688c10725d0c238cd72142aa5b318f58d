package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CBHistoryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.presenter.SourcePresenter;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.BiddingHallFragment;
import com.hletong.hyc.ui.fragment.PlatformBiddingHallFragment;
import com.hletong.hyc.ui.fragment.SourceDetailsFragment;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2016/11/4.
 * 竞价历史列表
 */

public class BiddingHistoryAdapter extends HFRecyclerAdapter<CBHistoryItem> {
    private ITransactionView delegate;

    public BiddingHistoryAdapter(Context context, List<CBHistoryItem> data, ITransactionView delegate) {
        super(context, data);
        this.delegate = delegate;
    }

    @Override
    protected BaseHolder<CBHistoryItem> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new StatusItemUtil().wrapView(getInflater().inflate(R.layout.recycler_item_source, parent, false)), delegate);
    }

    private class ViewHolder extends SourceListAdapter.BiddingViewHolder<CBHistoryItem> {
        private Bundle bundle;
        private String bidUuid;
        private boolean platform;

        public ViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
        }

        public void setData(CBHistoryItem source) {
            bidUuid = source.getBidUuid();
            platform = source.getBilling_type() == 1;
            bundle = BiddingHallFragment.getBundleParams(source, true);
            super.setData(source);
            setVisible(R.id.flag, View.GONE);
            TextView textView = getView(R.id.action);
            textView.setText(R.string.hall);

            StatusItemUtil mStatusItemUtil = (StatusItemUtil) itemView.getTag();
            switch (source.getStatus()) {
                case "0"://竞价中
                    mStatusItemUtil.setData("竞价中", source.getAvailableTime());
                    break;
                case "1"://已中标
                    mStatusItemUtil.setData("已中标", source.getAvailableTime());
                    break;
                case "2"://投标失败
                    mStatusItemUtil.setData(getColor(R.color.text_hint), "投标失败", source.getAvailableTime());
                    break;
            }
        }

        @Override
        protected String getCargoDesc(Source source) {
            return source.getCargoDescWithUnit();
        }

        @Override
        protected String getTimeLabelDesc(Source source) {
            return source.getCarrierAndLoadingPeriod();
        }

        @Override
        protected int getSourceType() {
            return SourcePresenter.CB_HISTORY;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.action) {
                //由于缺少竞价场次的round id，进入竞价大厅需要先去根据bidUuid请求货源详情，然后从货源详情获取round_uuid，再去获取获取场次信息
                Intent intent = new Intent(v.getContext(), CommonWrapFragmentActivity.class);
                intent.putExtras(CommonWrapFragmentActivity.getBundle(getString(R.string.cb_hall), platform ? PlatformBiddingHallFragment.class : BiddingHallFragment.class, bundle));
                itemView.getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(v.getContext(), CommonWrapFragmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bidUuid", bidUuid);
                bundle.putInt("type", getSourceType());
                intent.putExtras(CommonWrapFragmentActivity.getBundle("货源详情", SourceDetailsFragment.class, bundle));
                startActivityForResult(intent);
            }
        }
    }
}
