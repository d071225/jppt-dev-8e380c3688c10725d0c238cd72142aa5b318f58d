package com.hletong.hyc.presenter;

import android.os.Bundle;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.InvoiceContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Invoice;
import com.hletong.hyc.model.validate.InvoiceInfo;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.util.FinishOptions;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/1/5.
 * 发货单
 */

public class InvoicePresenter extends BasePresenter<InvoiceContract.View> implements InvoiceContract.Presenter {
    private Invoice mInvoice;

    public InvoicePresenter(InvoiceContract.View view, Invoice mInvoice) {
        super(view);
        this.mInvoice = mInvoice;
    }

    @Override
    public void start() {
        getView().showContact("装货联系人", mInvoice.getEncryptedLoadingContact());
        getView().showCargo(
                mInvoice.getInvoice_code(),
                mInvoice.getOrgin_cargon_kind_name(),
                BuildConfig.carrier_name,
                mInvoice.getCarrier_no(),
                mInvoice.getCargoNumbersWithUnit() + "|" + mInvoice.getCargoWeightWithUnit(),
                mInvoice.getLoadingAddress(),
                mInvoice.getLoadingPeriod());
        //货方不能查看合同
        if (AppTypeConfig.isTransporter())
            getView().showContract();

        if (mInvoice.getInvoice_status() == 0) {
            //上传发货单
            getView().showTitle("上传发货单");
            getView().showNumber(mInvoice.getTradeUnitCt(), mInvoice.getUnits_(), mInvoice.withFraction(), true);
            getView().showWeight(mInvoice.getTradeWeight(), true);
            getView().showPasswordArea();
            getView().showSubmitButton();

            getView().startLocating(mInvoice.getBilling_type() == 1);//开始定位
        } else {
            getView().showTitle("发货单信息");
            getView().showNumber(mInvoice.getInvoiceUnitCt(), mInvoice.getUnits_(), mInvoice.withFraction(), false);
            getView().showWeight(mInvoice.getInvoiceWeight(), false);
        }
    }

    @Override
    public void submit(InvoiceInfo invoiceInfo) {
        invoiceInfo.setExtras(mInvoice.getInvoice_uuid(), mInvoice.getTrade_uuid());
        OkRequest request = invoiceInfo.validate(getView());
        if (request != null) {
            new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(getView()) {
                @Override
                public void onError(OkCall<CommonResult> okCall, EasyError error) {
                    if ((error instanceof BusiError) && ((BusiError) error).getBusiCode() == 500000) {
                        /**
                         * 服务端返回的地址信息出错，客户端这里重新定位，让用户手动重传
                         */
                        getView().onLocationError("您的定位和装货地地址不一致，请确认后再回传");
                    } else {
                        getView().handleError((BaseError) error);
                    }
                }

                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    getView().success(response.getErrorInfo());
                    getView().finishWithOptions(FinishOptions.FORWARD_RESULT());
                }
            });
        }
    }

    @Override
    public void viewContract() {
        Bundle bundle = new Bundle();
        bundle.putString("contractUuid", mInvoice.getContract_uuid());//前面保存过了，直接取用
        bundle.putString("title", "合同详情");
        getView().toActivity(AppTypeConfig.getContractActivity(), bundle, null);
    }

    @Override
    public void call() {
        getView().makeCall(mInvoice.getEncryptedLoadingContact(), mInvoice.getLoading_contacts_tel());
    }
}
