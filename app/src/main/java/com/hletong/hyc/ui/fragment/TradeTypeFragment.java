package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hletong.hyc.adapter.TradeTypeAdapter;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.CargoPermission;
import com.hletong.hyc.model.TradeType;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.view.IDialogView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.http.EasyCallback;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/13.
 */

public class TradeTypeFragment extends BaseRefreshFragment<TradeType> implements ITransactionView, IDialogView {

    public static Bundle getExtras(int selected) {
        Bundle bundle = new Bundle();
        bundle.putInt("selected", selected);
        return bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
        OkRequest request = EasyOkHttp.get(Constant.CARGO_PERMISSION).extra(JpptParse.ENTRY, "data").build();
        new ExecutorCall<CargoPermission>(request).enqueue(new EasyCallback<CargoPermission>(this) {
            @Override
            public void onSuccess(OkCall<CargoPermission> okCall, CargoPermission response) {
                TradeTypeAdapter adapter = (TradeTypeAdapter) mAdapter;
                adapter.refresh(response.getData());
            }
        });
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<TradeType> createAdapter() {
        TradeTypeAdapter adapter = new TradeTypeAdapter(getContext(), this, this, null, getArguments().getInt("selected", -1));
        adapter.hideFooter();
        return adapter;
    }

    @Override
    protected boolean onlyView() {
        return true;
    }
}
