package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.adapter.YunLiHistoryAdapter;
import com.hletong.hyc.model.ForecastTransporterInfo;
import com.hletong.hyc.ui.activity.ship.ForecastShipActivity;
import com.hletong.hyc.ui.activity.truck.ForecastTruckActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by ddq on 2017/1/3.
 * 运力预报历史列表
 */

public class TransportHistoryListFragment extends BaseRefreshFragment<ForecastTransporterInfo> {

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.TRANSPORT_HISTORY);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<ForecastTransporterInfo> createAdapter() {
        return new YunLiHistoryAdapter(getContext(), null, this);
    }

    @Override
    public ParamsHelper getRequestJson(boolean isRefresh) {
        //carrier_type 会随着不同的build参数而不同，车主版是1，船主版是2，详情参见build.gradle(app)
        return getCommonRequestValue(isRefresh).put("carrier_type", BuildConfig.carrier_type);
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(BuildConfig.transporter_empty_view).setCustomState(RequestState.NO_DATA, "暂无运力预报");
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        switch (requestCode) {
            case SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM:
            case 200: {
                requestData(true);
                break;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_root);
        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.common_button, null);
        tv.setText(R.string.transport_forecast_title);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (BuildConfig.app_type) {
                    case 2:
                        toActivity(ForecastTruckActivity.class, null, 200, null);
                        break;
                    case 3:
                        toActivity(ForecastShipActivity.class, null, 200, null);
                        break;
                }

            }
        });
        ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(48));
        mLinearLayout.addView(tv, mLayoutParams);
    }
}
