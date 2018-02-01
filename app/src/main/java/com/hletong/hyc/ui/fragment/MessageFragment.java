package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.MessageAdapter;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.model.MessageInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * 消息tab页面Fragment
 */
public class MessageFragment extends BaseRefreshFragment<MessageInfo> {

    @Override
    public int getLayoutId() {
        return R.layout.common_recycler_list;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<MessageInfo> createAdapter() {
        return new MessageAdapter(getActivity(), this, "1".equals(getArguments().getString("status")));
    }

    @NonNull
    @Override
    public String getRequestUrl() {
        return Constant.getUrl(Constant.MESSAGE);
    }

    @Override
    public ParamsHelper getRequestJson(boolean isRefresh) {
        ParamsHelper json = super.getRequestJson(isRefresh);
        String status = getArguments().getString("status");
        String inform_call_type = getArguments().getString("inform_call_type");
        if (status != null) {
            json.put("status", status);
        }
        if (inform_call_type != null) {
            json.put("informTypeList", inform_call_type);
        }
        return json;
    }

    @Override
    protected String getEntry() {
        return null;
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM)
            mPtrFrameLayout.autoRefresh();
    }
}
