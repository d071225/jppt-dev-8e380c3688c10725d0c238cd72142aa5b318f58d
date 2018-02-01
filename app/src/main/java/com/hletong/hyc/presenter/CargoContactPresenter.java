package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.CargoContactContract;
import com.hletong.hyc.model.BusinessContact;
import com.hletong.hyc.model.validate.ContactInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/20.
 */

public class CargoContactPresenter extends BasePresenter<CargoContactContract.View> implements CargoContactContract.Presenter {
    private BusinessContact mContact;

    public CargoContactPresenter(CargoContactContract.View view, BusinessContact contact) {
        super(view);
        mContact = contact;
    }

    @Override
    public void start() {
        if (mContact != null){
            getView().showTitle("编辑收发货人");
            getView().showName(mContact.getContact_name());
            getView().showTaxCode(mContact.getTaxpayer_code());
        }else {
            getView().showTitle("新增联系人");
        }
    }

    @Override
    public void submit(ContactInfo contactInfo) {
        if (mContact != null)
            contactInfo.setExtras(mContact.getContact_uuid());

        OkRequest request = contactInfo.validate(getView());
        if (request != null)
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
    }
}
