package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.adapter.ReceiptAdapter;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.model.Receipt;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.HistoryHeader;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;

/**
 * Created by ddq on 2016/11/10.
 * 收货单历史列表
 */
public class ReceiptHistoryFragment extends BaseRefreshFragment<Receipt> {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (hasHeader()) {
            new HistoryHeader((ViewGroup) getActivity().findViewById(R.id.layout_root), 1);
        }
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Receipt> createAdapter() {
        return new ReceiptAdapter(getContext(), null, this, 2);
    }

    public String getRequestUrl() {
        return Constant.getUrl(Constant.RECEIPT_HISTORY);
    }


    public ParamsHelper getRequestJson(boolean isRefresh) {
        return getCommonRequestValue(isRefresh)
                .put("memberType", BuildConfig.app_type);//车船货就这个参数不一样
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_receipt).setCustomState(RequestState.NO_DATA, "暂无历史收货单");
    }

    public boolean hasHeader() {
        return true;
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM)
            requestData(true);
    }
}
