package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.adapter.UnsignedContractAdapter;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.UnSignedContract;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;

/**
 * Created by ddq on 2016/11/8.
 * 未签约合同列表
 */
public class ContractUnSignedFragment extends BaseRefreshFragment<UnSignedContract> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history, menu);
        MenuItem mi = menu.findItem(R.id.history);
        mi.setTitle(R.string.contract_history);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("memberCode", LoginInfo.getLoginInfo().getMember_code());
        toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.contract_history), ContractHistoryFragment.class, bundle), null);
        return true;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<UnSignedContract> createAdapter() {
        return new UnsignedContractAdapter(getContext(), null, this);
    }

    @NonNull
    @Override
    public String getRequestUrl() {
        return Constant.getUrl(Constant.CONTRACT_UNSIGNED);
    }

    @Override
    public ParamsHelper getRequestJson(boolean isRefresh) {
        return getCommonRequestValue(isRefresh)
                .put("type", BuildConfig.unsigned_contract_type)
                .put("status", 2);
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM)
            requestData(true);
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_contract).setCustomState(RequestState.NO_DATA, "暂无网签承运");
    }
}