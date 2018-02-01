package com.hletong.hyc.adapter;

import android.content.Context;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Cargo;
import com.hletong.mob.pullrefresh.BaseHolder;

import java.util.List;

/**
 * Created by ddq on 2017/3/9.
 */

public class CargoEditAdapter extends BaseEditAdapter<Cargo> {
    public CargoEditAdapter(Context context, List<Cargo> data, boolean modeEdit, int selected) {
        super(context, data, modeEdit, R.layout.recycler_item_cargo_management, selected);
    }

    @Override
    public void bindData(Cargo cargo, BaseHolder<Cargo> holder) {
        holder.setText(R.id.title, cargo.getOrgin_cargon_kind_name());
        holder.setText(R.id.measure_type, "计量方式：" + cargo.getMeasure_type());
//        holder.setText(R.id.transport_fee, "运费单价：" + cargo.getOrignUnitAmtWithUnit());
        holder.setText(R.id.transport_loss, "运输损耗：" + cargo.getMaxWastageRtWithUnit());
        holder.setText(R.id.volume, "长/宽/高：" + cargo.getVolume("/"));
        holder.setText(R.id.unit, "单位：" + cargo.getCargoUnit());
        View view = holder.getView(R.id.carrier_length);
        view.setVisibility(View.INVISIBLE);
        if (1 == cargo.getTransport_type()) {
            view.setVisibility(View.VISIBLE);
            holder.setText(R.id.carrier_model, "车型要求：" + cargo.getCarrier_model_type_());
            holder.setText(R.id.carrier_length, "车长要求：" + (cargo.getCarrier_length_type_() == null ? "无" : cargo.getCarrier_length_type_()));
        } else if (2 == cargo.getTransport_type()) {
            holder.setText(R.id.carrier_model, "船型要求：" + cargo.getCarrier_model_type_());
        }
    }
}
