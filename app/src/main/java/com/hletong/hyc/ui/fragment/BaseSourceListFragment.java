package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.model.Source;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2016/12/30.
 */

public abstract class BaseSourceListFragment extends BaseRefreshFragment<Source> {

    @NonNull
    @Override
    public HFRecyclerAdapter<Source> createAdapter() {
        return new SourceListAdapter(getContext(), null, this);
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM){
            requestData(true);
        }
    }
}
