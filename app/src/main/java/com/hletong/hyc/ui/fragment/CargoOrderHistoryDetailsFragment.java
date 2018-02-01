package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.CargoOrderDetailsAdapter;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.CargoSourceItem;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.DividerItemDecoration;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by dongdaqing on 2017/4/18.
 * 货方承运历史
 */

public class CargoOrderHistoryDetailsFragment extends BaseRefreshFragment<CargoSourceItem.TransporterInfo> {

    public static Bundle getParams(CargoSourceItem csi) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("cargo", csi);
        return bundle;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_cargo_order_details;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CargoSourceItem.TransporterInfo> createAdapter() {
        CargoSourceItem csi = getArguments().getParcelable("cargo");
        return new CargoOrderDetailsAdapter(getContext(), csi.getBook_ref_type(), csi.getCargoUnit());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CargoSourceItem source = getArguments().getParcelable("cargo");
        if (source == null)
            return;

        setViewGone(R.id.flag);
        setViewGone(R.id.action);
        setText(R.id.title, source.getOrgin_cargon_kind_name());
        setText(R.id.CargoWeight, source.getCargoDescWithUnit());
        setText(R.id.startProvince, Address.chooseCity(source.getLoading_province(), source.getLoading_city()));//不是显示省，而是市
        setText(R.id.startCity, Address.chooseCountry(source.getLoading_province(), source.getLoading_city(), source.getLoading_country()));//不是显示市，而是区
        setText(R.id.endProvince, Address.chooseCity(source.getUnload_province(), source.getUnload_city()));//不是显示省，而是市
        setText(R.id.endCity, Address.chooseCountry(source.getUnload_province(), source.getUnload_city(), source.getUnload_country()));//不是显示市，而是区
        setText(R.id.time, "装货时间：" + source.getLoadingPeriod());
        showList(source.getTransportTradeDtlRespDtoList(), true);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration();
    }

    private void setText(int id, String s) {
        TextView tv = findViewById(id);
        tv.setText(s);
    }

    private void setViewGone(int id) {
        findViewById(id).setVisibility(View.GONE);
    }

    @Override
    protected boolean onlyView() {
        return true;
    }
}
