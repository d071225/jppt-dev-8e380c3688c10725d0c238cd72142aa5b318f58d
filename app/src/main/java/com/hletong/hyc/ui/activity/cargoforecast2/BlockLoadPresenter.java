package com.hletong.hyc.ui.activity.cargoforecast2;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CargoContact;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.presenter.cargo.BlockBasePresenter;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.cargo.ForecastPresenterManager;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.CargoAddressListFragment;

import java.util.Map;

/**
 * Created by dongdaqing on 2017/8/25.
 * 装货信息
 */

public class BlockLoadPresenter extends BlockBasePresenter<CargoForecastContract.BlockLoadView> implements CargoForecastContract.BlockLoadPresenter {
    private CargoContact mContact;

    public BlockLoadPresenter(@NonNull CargoForecastContract.BlockLoadView baseView) {
        super(baseView);
    }

    @Override
    public void sourceAttached(Source source) {
        super.sourceAttached(source);
    }

    @Override
    public void chooseContact() {
        getView().toActivity(
                CommonWrapFragmentActivity.class,
                CommonWrapFragmentActivity.getBundle(
                        "装货地信息",
                        CargoAddressListFragment.class,
                        CargoAddressListFragment.getDefaultBundle(
                                false,
                                -1,
                                "新增常用地址",
                                "管理装卸货信息", isSelfTrade())),
                102,
                null);
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == 102) {
            mContact = data.getParcelableExtra("data");
            //把事件发出去，路径规划功能需要装货地址
            ForecastPresenterManager.notifyDataChanged(this, "loadAddress", mContact.getCustomAddress());
            //数据显示
            getView().showAddress(mContact.getCustomAddress().buildAddress());
//            getView().showContact(mContact.getContactName(), mContact.getContactTel());
        }
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    protected void handleBillingType(int billingType) {
        super.handleBillingType(billingType);
        if (isSelfTrade()) {
            getView().hideView(R.id.loadContactTel, R.id.loadContact, R.id.loadTM_);
        } else {
            getView().showView(R.id.loadContactTel, R.id.loadContact, R.id.loadTM_);
        }
    }
}
