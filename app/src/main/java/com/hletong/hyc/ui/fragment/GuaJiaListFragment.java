package com.hletong.hyc.ui.fragment;

import android.support.annotation.NonNull;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.GuaJiaAdapter;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;

/**
 * Created by ddq on 2016/12/30.
 */

public class GuaJiaListFragment extends BaseSourceListFragment {

    @Override
    protected ParamsHelper getRequestJson(boolean refresh){
        return getCommonRequestValue(refresh)
                .put("type", "01")
                .put("status", "2");
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.GUAJIA_SOURCE_LIST);
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_recycler_list;
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_data).setCustomState(RequestState.NO_DATA, "暂无挂价摘牌");
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Source> createAdapter() {
        return new GuaJiaAdapter(getContext(), null, this);
    }
}
