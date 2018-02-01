package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hletong.hyc.adapter.CargoForecastBillReceiverAdapter;
import com.hletong.hyc.model.Drawee;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/15.
 * 受票方
 */

public class CargoForecastBillReceiverFragment extends CargoForecastBaseSelectFragment<Drawee> {

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
    public HFRecyclerAdapter<Drawee> createAdapter() {
        return new CargoForecastBillReceiverAdapter(getContext(), isModeEdit(), getSelected());
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh){
        return super.getRequestJson(refresh).put("searchType", 3);
    }
}
