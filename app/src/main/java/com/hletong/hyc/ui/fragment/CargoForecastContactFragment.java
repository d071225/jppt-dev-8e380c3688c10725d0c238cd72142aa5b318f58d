package com.hletong.hyc.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hletong.hyc.adapter.BaseEditAdapter;
import com.hletong.hyc.adapter.CargoForecastContactAdapter;
import com.hletong.hyc.contract.CargoContactContract;
import com.hletong.hyc.model.BusinessContact;
import com.hletong.hyc.presenter.CargoContactPresenter2;
import com.hletong.hyc.ui.activity.CargoForecastEditContactActivity;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/15.
 * 收发货人
 */

public class CargoForecastContactFragment extends CargoForecastBaseSelectFragment<BusinessContact> implements CargoContactContract.View2 {
    private CargoContactContract.Presenter2 mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CargoContactPresenter2(this);
    }

    @Override
    protected Class getBottomButtonActionClass() {
        return CargoForecastEditContactActivity.class;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<BusinessContact> createAdapter() {
        CargoForecastContactAdapter adapter = new CargoForecastContactAdapter(getContext(), isModeEdit(), getSelected());
        adapter.setDeleteAction(new BaseEditAdapter.OnActionClicked<BusinessContact>() {
            @Override
            public void actionClicked(View view, final BusinessContact contact, BaseHolder holder) {
                DialogFactory.showAlertWithNegativeButton(getContext(), "删除", "确定要出当前联系人吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.delete(contact.getContact_uuid());
                    }
                });
            }
        });
        adapter.setEditAction(new BaseEditAdapter.OnActionClicked<BusinessContact>() {
            @Override
            public void actionClicked(View view, BusinessContact contact, BaseHolder holder) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("contact", contact);
                toActivity(getBottomButtonActionClass(), bundle, 110, null);
            }
        });
        return adapter;
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh).put("searchType", 5).put("contactType", 2);
    }

    @Override
    public void refresh() {
        mPtrFrameLayout.autoRefresh(true);
    }
}
