package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.CargoSourceItem;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.CargoRefusedReasonActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.SourcePreviewActivity;
import com.hletong.hyc.ui.activity.SuitableCarrierActivity;
import com.hletong.hyc.ui.fragment.SourceTrackFragment;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2017/3/6.
 * 货方APP，货源预报
 */

public class CargoSourceAdapter extends HFRecyclerAdapter<CargoSourceItem> {
    private ITransactionView mFragmentDelegate;
    private int colorActivate;
    private int colorDeactivate;

    public CargoSourceAdapter(Context context, ITransactionView fragmentDelegate) {
        super(context, null, 30);
        this.mFragmentDelegate = fragmentDelegate;
        colorActivate = ContextCompat.getColor(context, R.color.colorAccent);
        colorDeactivate = ContextCompat.getColor(context, R.color.text_hint);
    }

    @Override
    protected BaseHolder<CargoSourceItem> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                LinearLayout layout = new LinearLayout(parent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                getInflater().inflate(R.layout.recycler_item_source, layout);
                return new CargoViewHolder(new StatusItemUtil().wrapView(getInflater().inflate(R.layout.recycler_item_source_track_buttons, layout)), mFragmentDelegate);
            case 2:
                return new ViewHolder(new StatusItemUtil().wrapView(getInflater().inflate(R.layout.recycler_item_source, parent, false)), mFragmentDelegate);
        }
        return null;
    }

    @Override
    public int getViewType(int position) {
        if (getItem(position).isButtonAvailable())
            return 1;
        return 2;
    }

    private class ViewHolder extends SourceListAdapter.BaseViewHolder<CargoSourceItem> {

        public ViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            setVisible(R.id.action, View.GONE);
            setVisible(R.id.flag,View.GONE);
            itemView.setOnClickListener(this);
        }

        public void setData(CargoSourceItem source) {
            super.setData(source);
            if (source.isCarried()) {
                setVisible(R.id.CargoRemain, View.VISIBLE);
                setText(R.id.CargoRemain, source.getRemainCargoDesc());
            } else {
                setVisible(R.id.CargoRemain, View.GONE);
            }
            //设置状态栏
            StatusItemUtil statusItemUtil = (StatusItemUtil) itemView.getTag();
            statusItemUtil.setData(source.activate() ? colorActivate : colorDeactivate, source.getStatusDesc(), source.getAvailableTime());
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("cargoUuid", getSource().getCargo_uuid());
            bundle.putString("title", "货源详情");
            bundle.putBoolean("revoke", getItem(getAdapterPosition()).canRevoke());
            startActivityForResult(SourcePreviewActivity.class, bundle);
        }
    }

    private class CargoViewHolder extends ViewHolder {
        private TextView actionView;
        private TextView matchedVehicles;

        public CargoViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            actionView = (TextView) itemView.findViewById(R.id.br);
            actionView.setOnClickListener(this);
            matchedVehicles = (TextView) itemView.findViewById(R.id.bl);
            matchedVehicles.setText("适运车船");
            matchedVehicles.setOnClickListener(this);
        }

        @Override
        public void setData(CargoSourceItem source) {
            super.setData(source);
            //自主交易，显示匹配车船
            if (source.getBilling_type() == 3 && "50".equals(source.getStatus()) && !source.isMatchSuccess()) {
                matchedVehicles.setVisibility(View.VISIBLE);
            } else {
                matchedVehicles.setVisibility(View.GONE);
            }

            actionView.setVisibility(View.VISIBLE);
            switch (source.getStatus()) {
                case "00":
                    actionView.setText("提交货源");
                    break;
                case "20":
                    actionView.setText("查看原因");
                    break;
                case "40":
                    actionView.setText("网签锁货");
                    break;
                case "50":
                    actionView.setText("货源跟踪");
                    break;
                default:
                    actionView.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.br) {
                switch (getItem(getAdapterPosition()).getStatus()) {
                    case "00": {
                        //提交货源
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("source", getSource());
                        bundle.putBoolean("full_copy", true);
                        bundle.putBoolean("submit", true);
                        bundle.putBoolean("saveToDraft", false);
                        startActivityForResult(CargoForecastActivity.class, bundle);
                        break;
                    }
                    case "20": {
                        //查看原因
                        Bundle bundle = new Bundle();
                        bundle.putString("cargoUuid", getSource().getCargo_uuid());
                        toActivity(CargoRefusedReasonActivity.class, bundle);
                        break;
                    }
                    case "40"://网签锁货
                        Intent intent = new Intent(v.getContext(), AppTypeConfig.getContractActivity());
                        intent.putExtra("title", getString(R.string.signContract));
                        intent.putExtra("cargoUuid", getSource().getCargo_uuid());
                        startActivityForResult(intent);
                        break;
                    case "50":
                        //货源跟踪
                        startActivityForResult(CommonWrapFragmentActivity.class,
                                CommonWrapFragmentActivity.getBundle(
                                        "货源跟踪",
                                        SourceTrackFragment.class,
                                        SourceTrackFragment.getInstance(getItem(getAdapterPosition()))));
                        break;
                }
            } else if (v.getId() == R.id.bl) {
                //适运货源
                Bundle bundle = new Bundle();
                bundle.putString("cargoUuid", getSource().getCargo_uuid());
                bundle.putString("transType", String.valueOf(getSource().getTransport_type()));
                toActivity(SuitableCarrierActivity.class, bundle);
            } else {
                super.onClick(v);
            }
        }
    }
}
