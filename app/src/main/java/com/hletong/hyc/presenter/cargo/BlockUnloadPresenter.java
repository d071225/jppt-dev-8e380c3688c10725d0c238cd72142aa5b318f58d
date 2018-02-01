package com.hletong.hyc.presenter.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.CargoContact;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.PathPlanActivity;
import com.hletong.hyc.ui.fragment.CargoAddressListFragment;
import com.hletong.hyc.util.Validator;

import java.util.Map;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockUnloadPresenter extends BlockBasePresenter<CargoForecastContract.BlockUnloadView> implements CargoForecastContract.BlockUnloadPresenter {
    private CargoContact mContact;
    private Address startAddress;
    private Address endAddress;

    public BlockUnloadPresenter(@NonNull CargoForecastContract.BlockUnloadView baseView) {
        super(baseView);
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public void chooseContact() {
        getView().toActivity(
                CommonWrapFragmentActivity.class,
                CommonWrapFragmentActivity.getBundle("卸货地信息",
                        CargoAddressListFragment.class,
                        CargoAddressListFragment.getDefaultBundle(
                                false, -1, "新增常用地址", "管理装卸货信息", isSelfTrade())),
                103,
                null);
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == 103) {
//            mContact = data.getParcelableExtra("data");
//            if (mContact.isAddressValid()) {
//                endAddress = mContact.getCustomAddress();
//                getView().showAddress(endAddress.buildAddress());
//                getView().showContact(mContact.getContact_name(), mContact.getContact_tel());
//            }
        }
    }

    @Override
    public void viewTransportRoute() {
        if (Validator.isNotNull(startAddress, getView(), "请选择装货地")
                && Validator.isNotNull(endAddress, getView(), "请选择卸货地")) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PathPlanActivity.START_ADDRESS, startAddress);
            bundle.putParcelable(PathPlanActivity.END_ADDRESS, endAddress);
            getView().toActivity(PathPlanActivity.class, bundle, null);
        }
    }

    @Override
    public void dataChanged(String event, Object data) {
        if ("loadAddress".equals(event)){
            //用户选择了装货地址
            startAddress = (Address) data;
        }
    }
}
