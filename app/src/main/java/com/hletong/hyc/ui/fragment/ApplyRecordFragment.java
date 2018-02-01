package com.hletong.hyc.ui.fragment;

import android.support.annotation.NonNull;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.ApplyRecordAdapter;
import com.hletong.hyc.model.ApplyRecordInfo;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

public class ApplyRecordFragment extends BaseRefreshFragment<ApplyRecordInfo> {
    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.ETC_APPLY_RECORD);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<ApplyRecordInfo> createAdapter() {
        return new ApplyRecordAdapter(getActivity(), null);
    }

    @Override
    public ParamsHelper getRequestJson(boolean isRefresh){
        return new ParamsHelper().put("memberCode", LoginInfo.getLoginInfo().getMember_code());
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.apply_record);
    }

    @Override
    protected String getEntry() {
        return null;
    }
}
