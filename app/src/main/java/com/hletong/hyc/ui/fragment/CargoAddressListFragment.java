package com.hletong.hyc.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hletong.hyc.adapter.BaseEditAdapter;
import com.hletong.hyc.adapter.CargoForecastAddressAdapter;
import com.hletong.hyc.contract.AddressContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.CargoContact;
import com.hletong.hyc.presenter.AddressPresenter2;
import com.hletong.hyc.ui.activity.AddAddressActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/14.
 * 发布货源-装卸货地址列表
 */

public class CargoAddressListFragment extends CargoForecastBaseSelectFragment<CargoContact> implements AddressContract.View2 {
    private AddressContract.Presenter2 mPresenter;

    public static Bundle getDefaultBundle(boolean edit, int selected, String button, String editTitle, boolean selfTrade) {
        Bundle bundle = CargoForecastBaseSelectFragment.getDefaultBundle(edit, selected, button, editTitle);
        bundle.putBoolean("selfTrade", selfTrade);
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddressPresenter2(this);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CargoContact> createAdapter() {
        CargoForecastAddressAdapter adapter = new CargoForecastAddressAdapter(getContext(), null, isModeEdit(), getSelected(), getArguments().getBoolean("selfTrade"));
        adapter.setDeleteAction(new BaseEditAdapter.OnActionClicked<CargoContact>() {
            @Override
            public void actionClicked(View view, final CargoContact info, BaseHolder holder) {
                DialogFactory.showAlertWithNegativeButton(getContext(), "删除", "确定要删除当前信息吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.delete(info.getContactUuid());
                    }
                });
            }
        });

        adapter.setEditAction(new BaseEditAdapter.OnActionClicked<CargoContact>() {
            @Override
            public void actionClicked(View view, CargoContact info, BaseHolder holder) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("address", info);
                toActivity(getBottomButtonActionClass(), bundle, 110, null);
            }
        });
        return adapter;
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.CARGO_CONTACT_LIST);
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh).put("searchType", 2);
    }

    @Override
    protected Class getBottomButtonActionClass() {
        return AddAddressActivity.class;
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == 101) {
            Intent intent = new Intent();
            CargoContact contact = new CargoContact(Address.getAddress(data));
            contact.set(data.getStringExtra("tel"), data.getStringExtra("contact"));
            intent.putExtra("data", contact);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            super.onActivityResultOk(requestCode, data);
        }
    }

    @Override
    public void refreshList() {
        mPtrFrameLayout.autoRefresh(true);
        getActivity().setResult(Activity.RESULT_OK);
    }
}
