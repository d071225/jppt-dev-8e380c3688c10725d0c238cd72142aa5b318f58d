package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.InvoiceAdapter;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.model.Invoice;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;

/**
 * Created by ddq on 2016/10/28.
 */

public class InvoiceFragment extends InvoiceHistoryFragment {

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
        mi.setTitle(R.string.invoice_history);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.invoice_history), InvoiceHistoryFragment.class), null);
        return true;
    }

    @NonNull
    @Override
    public String getRequestUrl() {
        return Constant.getUrl(Constant.INVOICE_LIST);
    }

    @Override
    public ParamsHelper getRequestJson(boolean isRefresh) {
        return getCommonRequestValue(isRefresh)
                .put("type", "03")
                .put("status", 2);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Invoice> createAdapter() {
        return new InvoiceAdapter(getContext(), null, this, 1);
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
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_invoice).setCustomState(RequestState.NO_DATA, "暂无发货单");
    }

    @Override
    public boolean hasHeader() {
        return false;
    }
}
