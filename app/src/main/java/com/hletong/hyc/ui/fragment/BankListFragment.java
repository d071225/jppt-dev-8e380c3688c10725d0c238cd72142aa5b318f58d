package com.hletong.hyc.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.BankListAdapter;
import com.hletong.hyc.contract.DictItemContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.datasource.DictItemSource;
import com.hletong.hyc.presenter.DictItemPresenter;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.MvpBaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.pullrefresh.SpaceItemDecoration;
import com.xcheng.view.util.LocalDisplay;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ddq on 2017/3/8.
 */

public class BankListFragment extends MvpBaseRefreshFragment<DictionaryItem, DictItemContract.View, DictItemContract.Presenter> implements DictItemContract.View {
    private BankListAdapter mBankListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.phone_selector, menu);
        MenuItem menuItem = menu.findItem(R.id.call);
        menuItem.setTitle("确定");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectPos = mBankListAdapter.getSelectPos();
        if (mBankListAdapter.getItem(selectPos) != null) {
            Intent intent = new Intent();
            intent.putExtra(DictionaryItem.class.getSimpleName(), (Serializable) mBankListAdapter.getItem(selectPos));
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            showMessage("请选择银行信息");
        }
        return true;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(LocalDisplay.dp2px(4));
    }

    @Override
    public void showLoading() {
        //super.showLoading();
    }

    @Override
    public void hideLoading() {
        // super.hideLoading();
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<DictionaryItem> createAdapter() {
        mBankListAdapter = new BankListAdapter(getContext(), null);
        mBankListAdapter.hideFooter();
        return mBankListAdapter;
    }
//
//    private ListRequestValue<DictionaryItem> getRequestValue() {
//        ListRequestValue<DictionaryItem> requestValue = new ListRequestValue<DictionaryItem>(
//                hashCode(),
//                Constant.getUrl(Constant.TRUCK_OPTIONAL_ITEM),
//                null, null) {
//        };
//        requestValue.setRefresh(true);
//        return requestValue;
//    }

    @Override
    public void requestData(boolean refresh) {
        getPresenter().loadDictItems("bank_type", Constant.TRUCK_OPTIONAL_ITEM);
    }

    @Override
    public void showDicts(List<DictionaryItem> list) {
        showList(list, true);
    }

    @Override
    protected DictItemContract.Presenter createPresenter() {
        return new DictItemPresenter(new DictItemSource(getContext()), this);
    }
}
