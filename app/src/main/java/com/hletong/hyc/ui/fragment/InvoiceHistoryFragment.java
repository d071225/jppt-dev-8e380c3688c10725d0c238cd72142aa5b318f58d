package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.adapter.InvoiceAdapter;
import com.hletong.hyc.model.Invoice;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.HistoryHeader;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;

/**
 * Created by ddq on 2016/11/9.
 */
public class InvoiceHistoryFragment extends BaseRefreshFragment<Invoice> {

    @NonNull
    @Override
    public HFRecyclerAdapter<Invoice> createAdapter() {
        return new InvoiceAdapter(getContext(), null, this, 2);
    }

    public String getRequestUrl() {
        return Constant.getUrl(Constant.INVOICE_HISTORY);
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh){
        return getCommonRequestValue(refresh)
                .put("memberType", BuildConfig.app_type);//车船货就这个参数不一样
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (hasHeader()) {
            new HistoryHeader((ViewGroup) getActivity().findViewById(R.id.layout_root),1);
        }
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_invoice).setCustomState(RequestState.NO_DATA, "暂无历史发货单");
    }

    public boolean hasHeader() {
        return true;
    }
}
