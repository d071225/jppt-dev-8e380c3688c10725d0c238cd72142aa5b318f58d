package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.ForecastTransporterInfo;
import com.hletong.hyc.ui.activity.ship.ForecastShipActivity;
import com.hletong.hyc.ui.activity.truck.ForecastTruckActivity;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2016/10/28.
 * 运力历史列表
 */

public class YunLiHistoryAdapter extends HFRecyclerAdapter<ForecastTransporterInfo> {
    private ITransactionView delegate;

    public YunLiHistoryAdapter(Context context, List<ForecastTransporterInfo> data, ITransactionView delegate) {
        super(context, data);
        this.delegate = delegate;
    }

    @Override
    protected BaseHolder<ForecastTransporterInfo> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_yunli_history, parent, false));
    }

    private class ViewHolder extends BaseHolder<ForecastTransporterInfo> implements View.OnClickListener {
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(ForecastTransporterInfo forecastTransporterInfo) {
            setText(R.id.title, forecastTransporterInfo.getCarrier_no());
            setText(R.id.CargoWeight, forecastTransporterInfo.getCargoWeight());
            setText(R.id.startProvince, Address.chooseCity(forecastTransporterInfo.getOriginProvinceForDisplay(), forecastTransporterInfo.getOriginCityForDisplay()));//不是显示省，而是市
            setText(R.id.startCity, forecastTransporterInfo.getOriginCountyForDisplay());//不是显示市，而是区
            setText(R.id.endProvince, Address.chooseCity(forecastTransporterInfo.getDestinationProvinceForDisplay(), forecastTransporterInfo.getDestinationCityForDisplay()));//不是显示省，而是市
            setText(R.id.endCity, forecastTransporterInfo.getDestinationCountyForDisplay());//不是显示市，而是区
            setText(R.id.time, forecastTransporterInfo.getTimeDes(" - ") + "可承运");
        }

        @Override
        public void onClick(View v) {
            switch (BuildConfig.app_type) {
                case 2://车辆
                {
                    Intent intent = new Intent(itemView.getContext(), ForecastTruckActivity.class);
                    intent.putExtra("data", getItem(getAdapterPosition()));
                    delegate.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
                    break;
                }
                case 3://船舶
                {
                    Intent intent = new Intent(itemView.getContext(), ForecastShipActivity.class);
                    intent.putExtra("data", getItem(getAdapterPosition()));
                    delegate.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
                    break;
                }
            }
        }
    }
}
