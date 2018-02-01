package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.AddressContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.CargoContact;
import com.hletong.hyc.model.validate.AddressInfo;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/14.
 * 装卸货地址相关信息管理
 */

public class AddressPresenter extends BasePresenter<AddressContract.View> implements AddressContract.Presenter {
    private CargoContact mInfo;
    private Address mAddress;

    public AddressPresenter(AddressContract.View view, CargoContact info) {
        super(view);
        mInfo = info;
    }

    @Override
    public void start() {
        if (mInfo != null) {
            getView().showTitle("修改地址信息");
            mAddress = mInfo.getCustomAddress();
            getView().showAddress(mInfo.getCustomAddress().buildAddress(), mInfo.getAddress());
            getView().showContact(mInfo.getContactName(), mInfo.getContactTel());
            getView().showDeep(mInfo.getWaterDepth());
        }else {
            getView().showTitle("新增常用地址");
        }
    }

    @Override
    public void submit(AddressInfo addressInfo) {
        if (mInfo != null)
            addressInfo.setId(mInfo.getId());

        addressInfo.setAddress(mAddress);
        OkRequest request = addressInfo.validate(getView());
        if (request != null) {
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView(), addressInfo.getResult()));
        }
    }

    @Override
    public void addressChanged(Address address) {
        mAddress = address;
    }
}
