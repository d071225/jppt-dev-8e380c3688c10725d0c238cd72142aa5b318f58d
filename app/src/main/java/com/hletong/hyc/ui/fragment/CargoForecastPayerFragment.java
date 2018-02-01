package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hletong.hyc.adapter.CargoForecastPayerAdapter;
import com.hletong.hyc.model.Payer;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/15.
 * 付款方
 */

public class CargoForecastPayerFragment extends CargoForecastBaseSelectFragment<Payer> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    protected Class getBottomButtonActionClass() {
        return null;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Payer> createAdapter() {
        return new CargoForecastPayerAdapter(getContext(), isModeEdit(), getSelected());
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh).put("searchType", 4);
    }
}
