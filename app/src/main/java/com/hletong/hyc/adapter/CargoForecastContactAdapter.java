package com.hletong.hyc.adapter;

import android.content.Context;

import com.hletong.hyc.R;
import com.hletong.hyc.model.BusinessContact;
import com.hletong.mob.pullrefresh.BaseHolder;

/**
 * Created by ddq on 2017/3/15.
 * 收发货人
 */

public class CargoForecastContactAdapter extends BaseEditAdapter<BusinessContact> {

    public CargoForecastContactAdapter(Context context,boolean modeEdit, int selected) {
        super(context, null, modeEdit, R.layout.recycler_item_cargo_forecast_contact, selected);
    }

    @Override
    protected void bindData(BusinessContact businessContact, BaseHolder<BusinessContact> holder) {
        holder.setText(R.id.title,"姓名：" + businessContact.getContact_name());
        holder.setText(R.id.tax,"纳税号：" + (businessContact.getTaxpayerCode()));
    }
}
