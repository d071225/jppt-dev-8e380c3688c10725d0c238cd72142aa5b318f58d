package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.ContractHistoryAdapter;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.ContractItemForHistoryList;
import com.hletong.hyc.util.HistoryHeader;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;

/**
 * Created by ddq on 2016/10/28.
 * 网签承运 合同历史（全部）列表
 */

public class ContractHistoryFragment extends BaseRefreshFragment<ContractItemForHistoryList> {

    @Override
    public int getLayoutId() {
        return R.layout.common_recycler_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new HistoryHeader((ViewGroup) getActivity().findViewById(R.id.layout_root), 1);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<ContractItemForHistoryList> createAdapter() {
        return new ContractHistoryAdapter(getActivity(), null, this);
    }

    @NonNull
    @Override
    public String getRequestUrl() {
        return AppTypeConfig.getContractHistorySI();
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh){
        return getCommonRequestValue(refresh).put("memberCode", getArguments().getString("memberCode"));
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_contract).setCustomState(RequestState.NO_DATA, "暂无历史合同");
    }
}
