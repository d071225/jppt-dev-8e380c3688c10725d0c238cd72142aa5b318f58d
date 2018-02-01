package com.hletong.hyc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CargoContact;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.util.NumberUtil;

import java.util.List;

/**
 * Created by ddq on 2017/3/14.
 */

public class CargoForecastAddressAdapter extends BaseEditAdapter<CargoContact> {
    private boolean selfTrade;

    public CargoForecastAddressAdapter(Context context, List<CargoContact> data, boolean modeEdit, int selected, boolean selfTrade) {
        super(context, data, modeEdit, R.layout.recycler_item_caro_forecast_address, selected);
        this.selfTrade = selfTrade;
    }

    @Override
    protected View viewInflated(View view) {
        if (selfTrade) {
            view.findViewById(R.id.contact).setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    protected void bindData(CargoContact cargoForecastAddress, BaseHolder<CargoContact> holder) {
        holder.setText(R.id.address, cargoForecastAddress.getCustomAddress().buildAddress(' ', true));
        holder.setText(R.id.contact, cargoForecastAddress.getContactInfo());
        boolean isNotEmpty = !TextUtils.isEmpty(cargoForecastAddress.getWaterDepth());
        holder.setVisible(R.id.deep, isNotEmpty ? View.VISIBLE : View.GONE);
        holder.setText(R.id.deep, "水深" + NumberUtil.format3f(cargoForecastAddress.getWaterDepth()) + "米");
    }
}
