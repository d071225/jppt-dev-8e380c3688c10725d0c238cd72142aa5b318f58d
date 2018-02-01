package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.adapter.UpcomingAdapter;
import com.hletong.hyc.contract.UpcomingContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.datasource.CollectionDataSource;
import com.hletong.hyc.presenter.UpcomingPresenter;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;

/**
 * Created by ddq on 2016/12/12.
 * 待办消息列表
 */
public class UpcomingMessageFragment extends BaseRefreshFragment<Object> implements UpcomingContract.View {
    private UpcomingContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UpcomingPresenter(this, new CollectionDataSource(getContext()));
        setPresenter(mPresenter);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Object> createAdapter() {
        return new UpcomingAdapter(getContext(), this, mPresenter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_recycler_list;
    }

    public ParamsHelper getRequestJson(boolean isRefresh) {
        return getCommonRequestValue(isRefresh).put("type", AppTypeConfig.getUpcomingMessageType());
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.TODO);
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM) {
            requestData(true);
        }
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setCustomState(RequestState.NO_DATA, "暂无待办消息");
    }

    @Override
    public void success(UpcomingContract.Action action) {
        showMessage(action.getMessage());
        UpcomingAdapter adapter = (UpcomingAdapter) mAdapter;
        adapter.collectStatusChanged(action);
    }
}
