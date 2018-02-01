package com.hletong.hyc.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hletong.hyc.adapter.BaseEditAdapter;
import com.hletong.hyc.adapter.CargoEditAdapter;
import com.hletong.hyc.contract.CargoContract;
import com.hletong.hyc.model.Cargo;
import com.hletong.hyc.presenter.CargoPresenter2;
import com.hletong.hyc.ui.activity.AddCargoActivity;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/9.
 * 货方发布货源预报-选择货物
 */

public class SelectCargoFragment extends CargoForecastBaseSelectFragment<Cargo> implements CargoContract.View2 {
    private CargoContract.Presenter2 mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CargoPresenter2(this);
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh).put("searchType", 1);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Cargo> createAdapter() {
        CargoEditAdapter adapter = new CargoEditAdapter(getContext(), null, isModeEdit(), getSelected());
        adapter.setDeleteAction(new BaseEditAdapter.OnActionClicked<Cargo>() {
            @Override
            public void actionClicked(View view, final Cargo cargo, BaseHolder holder) {
                DialogFactory.showAlertWithNegativeButton(getContext(), "删除货源", "确定要删除此货源吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.delete(cargo.getGoods_uuid());
                    }
                });
            }
        });

        adapter.setEditAction(new BaseEditAdapter.OnActionClicked<Cargo>() {
            @Override
            public void actionClicked(View view, Cargo cargo, BaseHolder holder) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("cargo", cargo);
                toActivity(AddCargoActivity.class, bundle, 110, null);
            }
        });
        return adapter;
    }

    @Override
    protected Class getBottomButtonActionClass() {
        return AddCargoActivity.class;
    }

    @Override
    public void success(String message) {
        showMessage(message);
        mPtrFrameLayout.autoRefresh(true);
        getActivity().setResult(Activity.RESULT_OK);
    }

    @Override
    public void refreshView() {
        mPtrFrameLayout.autoRefresh(true);
    }
}
