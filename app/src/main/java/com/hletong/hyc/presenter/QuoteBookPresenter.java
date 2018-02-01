package com.hletong.hyc.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.CallInfo;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.model.validate.QuoteBookInfo;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.hyc.ui.activity.ThirdPartyContractActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.http.EasyCallback;
import com.hletong.mob.util.NumberUtil;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/3/29.
 * 这里面只有车船报价，货方议价处理逻辑在{@link QuotePresenter}里面
 * <p>
 * 1.报价：从货源公告点 "议价"按钮进入，只是单纯提交报价信息
 * 2.报价进度：报价成功之后，会在待办消息里面生成一个待办任务，点进去就是报价进度，可以修改报价价格，取消报价
 * 3.报价摘牌：货方同意了承运方的报价，从上述的待办任务点进去就是报价摘牌
 */

public class QuoteBookPresenter extends NormalBookPresenter<BookContract.QuoteView> implements BookContract.QuotePresenter {
    private Quote mQuote;
    private String quoteUuid;//报价之后才会有quoteUuid
    private CallInfo mCallInfo;

    public QuoteBookPresenter(BookContract.QuoteView view, String quoteUuid) {
        super(view);
        this.quoteUuid = quoteUuid;
    }

    @Override
    protected boolean showContract() {
        return false;
    }

    @Override
    public void sourceLoaded(Source source) {
        super.sourceLoaded(source);
        /**
         * 报价进度界面，非报价界面
         */
        if (quoteUuid != null) {
            getView().showMenu();
            getView().showViewsAtLeft();
            loadQuoteDetails();
        } else {
            //这种情况是国联和报价，都是要选车船的
            if (isQuote()) {
                getView().showButton("电话议价");
            } else {
                getView().showButton("确认报价");
            }
            getView().prefetchTransporter();
        }

        if (isQuote()) {
            mCallInfo = CallInfo.QUOTE_WITH_PHONE(source.getLoadingContact(), source.getCargo_owner_code(), source.getLoading_contacts_tel(), source.getCargo_uuid());
        }
    }

    private void loadQuoteDetails() {
        OkRequest request = EasyOkHttp
                .get(Constant.QUOTE_INFOS_CARRIER)
                .param("quoteUuid", quoteUuid)
                .extra(JpptParse.ENTRY, "quote")
                .tag(tag())
                .build();

        new ExecutorCall<Quote>(request).enqueue(new EasyCallback<Quote>(getView(), true) {
            @Override
            public void onSuccess(OkCall<Quote> okCall, Quote response) {
                mQuote = response;
                mQuote.setBook_type(getSource().getBook_ref_type());
                mQuote.setUnits(getSource().getCargoUnit());
                //更新界面
                updateView();
                //发送广播通知外部数据发生了变化
                broadcast();
            }
        });
    }

    private void updateView() {
        getView().updateTransporter(mQuote.getCarrierNo());
        getView().updateCargo(mQuote.getCargoWithUnit());
        getView().updatePrice(mQuote.getPriceWithUnit());
        getView().updateIncome(mQuote.getTotalPrice());
        if ("00".equals(mQuote.getRealStatus())) {
            getView().showButton("修改报价");
        } else if ("10".equals(mQuote.getRealStatus())) {
            getView().showButton("确认摘牌");
            //展示三方合同
            getView().showContractCheckbox();
        }
    }

    private void broadcast() {
        //接收在SourceDetailsFragment里面
        Intent intent = new Intent(BookContract.QUOTE_INFO_CHANGED);
        intent.putExtra("data", mQuote);
        getView().broadcast(intent);
    }

    @Override
    public void action() {
        if (isQuote()) {
            //国联议价，打电话
            getView().makeCall(getSource().getLoading_contacts_tel());
        } else if (quoteUuid == null) {
            //用户从货源公告进入，提交报价信息
            submitQuote(getView().getSubmitInfo());
        } else {
            if (Validator.isNotNull(mQuote, getView(), "状态出错，无法执行操作")) {
                //从待办消息进入
                //货方还未同意报价，进行修改报价
                if (mQuote.isStatus("00")) {
                    getView().showModifyView(
                            mQuote.getUnits(),
                            mQuote.getBook_type(),
                            NumberUtil.format3f(mQuote.getCargo()),
                            NumberUtil.format2f(mQuote.getUnitAmt()));
                } else if (mQuote.isStatus("10")) {
                    //货方已同意报价，报价摘牌
                    submit(new QuoteBookInfo(mQuote.getQuoteUuid()));
                }
            }
        }
    }

    //提交报价信息，不是报价摘牌
    private void submitQuote(Validate2 validate2) {
        SourceInfo sourceInfo = (SourceInfo) validate2;
        sourceInfo.setBookType(getSource().getBook_ref_type());
        sourceInfo.setExtras(getLoginInfo().getMember_code(), getSource().getCargo_uuid(), getSource().getRem_qt(), getSource().getCargoUnit());

        OkRequest request = validate2.validate(getView());
        if (request != null)
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
    }

    //修改报价信息
    @Override
    public void modifyOffer(final String price, final String count) {
        if (Validator.isNotNull(price, getView(), "报价不能为空")
                && Validator.isNotNull(count, getView(), "请填写要承运的货物")
                && Validator.isNoLargerThan(Double.parseDouble(count), getSource().getCargoDesc(), getView(), "承运的货物不能超过货物总量")) {

            OkRequest request = EasyOkHttp.get(Constant.MODIFY_QUOTE)
                    .param("quoteUuid", quoteUuid)
                    .param("unitAmt", price)
                    .param(getSource().getBook_ref_type() == 0 ? "weight" : "unitCt", count)
                    .tag(tag())
                    .build();

            new ExecutorCall<CommonResult>(request).enqueue(new EasyCallback<CommonResult>(getView()) {
                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    getView().priceModified(response.getErrorInfo());
                    mQuote.update(count, price);
                    //更新界面
                    updateView();
                    broadcast();
                }
            });
        }
    }

    //记录拨号操作
    @Override
    public void record() {
        if (Validator.isNotNull(mCallInfo, getView(), "状态出错，无法执行操作")) {
            new ExecutorCall<CommonResult>(mCallInfo.getRequest(getView())).enqueue(new UICallback<CommonResult>() {
                @Override
                public void onError(OkCall<CommonResult> okCall, EasyError error) {
                    //do nothing
                }

                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    //di nothing
                }
            });
        }
    }

    //撤销报价
    @Override
    public void revoke() {
        if (Validator.isNotNull(quoteUuid, getView(), "状态出错，无法执行操作")) {
            OkRequest request = EasyOkHttp
                    .get(Constant.CANCEL_QUOTE)
                    .param("quoteUuid", quoteUuid)
                    .tag(tag())
                    .build();
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
        }
    }

    @Override
    public void viewContract(int requestCode, String cargo) {
        Bundle bundle = new Bundle();
        bundle.putString("cargoUuid", mQuote.getCargoUuid());
        bundle.putDouble("price", mQuote.getUnitAmt());
        bundle.putDouble("cargo", mQuote.getCargo());
        bundle.putBoolean("showConfirmButton", true);
        getView().toActivity(ThirdPartyContractActivity.class, bundle, requestCode, null);
    }

    @Override
    protected void submitInternal(PayMode payMode) {
        //货方同意了报价，进行议价摘牌
        final OkRequest request = payMode.validate(getView());
        getView().showSubmitAlertDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new ExecutorCall<CommonResult>(request).enqueue(new DataHandler<CommonResult>(getView()) {
                    @Override
                    public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                        selfTradeBookSucceed();
                    }
                });
            }
        });
    }

    @Override
    public void calculateTotalPrice(String unitPrice, String cargoCount) {
        if (quoteUuid == null)
            super.calculateTotalPrice(unitPrice, cargoCount);
        else {
            if (mQuote != null) {
                getView().updateIncome(mQuote.getTotalPrice());
            } else {
                getView().updateIncome("0元");
            }
        }
    }

    @Override
    protected double getCargo() {
        return getSource().getBook_ref_type() == 0 ? mQuote.getWeight() : mQuote.getUnitCt();
    }

    @Override
    protected String getCarrier() {
        return mQuote.getCarrierNo();
    }
}
