package com.hletong.hyc.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.HistoryOrderAdapter;
import com.hletong.hyc.contract.CollectionContract;
import com.hletong.hyc.model.HistoryOrder;
import com.hletong.hyc.presenter.CollectionPresenter;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/4/6.
 * 货方收藏的车船-承运历史
 */

public class CarrierHistoryFragment extends BaseRefreshFragment<HistoryOrder> implements CollectionContract.View {
    private CollectionContract.Presenter mPresenter;

    public static Bundle getParam(String carrierNo, String tradeUuid) {
        Bundle bundle = new Bundle();
        bundle.putString("carrierNo", carrierNo);
        bundle.putString("tradeUuid", tradeUuid);
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new CollectionPresenter(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.revoke, menu);
        MenuItem item = menu.findItem(R.id.revoke);
        item.setTitle("取消收藏");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFactory.showAlertWithNegativeButton(getContext(), "取消收藏", "确定要取消收藏吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.delete(getArguments().getString("tradeUuid"));
            }
        });
        return true;
    }

    @Override
    public void updateItem(String carrierMemberCode) {
        //do nothing
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.CARRIER_TRADE_HISTORY);
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh)
                .put("carrierNo", getArguments().getString("carrierNo"));
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<HistoryOrder> createAdapter() {
        return new HistoryOrderAdapter(getContext());
    }

    @Override
    protected String getEntry() {
        return null;
    }

    @Override
    public void success(String message) {
        showMessage(message);
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
