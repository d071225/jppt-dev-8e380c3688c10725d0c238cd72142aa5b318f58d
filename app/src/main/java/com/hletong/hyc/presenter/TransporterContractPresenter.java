package com.hletong.hyc.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.Contract;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.ContractItemForDetails;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TransportContractDetails;
import com.hletong.hyc.model.validate.ContractInfo;
import com.hletong.hyc.ui.activity.ThirdPartyContractActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.okhttp.util.ParamUtil;

/**
 * Created by dongdaqing on 2017/8/2.
 * 签约、查看合同-车船app
 */

public class TransporterContractPresenter extends ContractPresenter<Contract.TransporterView> implements Contract.TransporterPresenter {
    private TransportContractDetails mContractDetails;
    private ContractInfo tmp;//车船签合同用到
    private String bookUuid;//车船签合同

    public TransporterContractPresenter(@NonNull Contract.TransporterView baseView, String contractUuid, String bookUuid) {
        super(baseView, contractUuid);
        this.bookUuid = bookUuid;
    }

    @Override
    protected void loadContractDetails() {
        //加载合同详情
        OkRequest request = EasyOkHttp.get(Constant.TRANSPORT_CONTRACT_BY_BOOK_UUID)
                .param("bookUuid", bookUuid)
                .tag(tag())
                .build();

        new ExecutorCall<TransportContractDetails>(request).enqueue(new EasyCallback<TransportContractDetails>(getView()) {
            @Override
            public void onSuccess(OkCall<TransportContractDetails> okCall, TransportContractDetails response) {
                mContractDetails = response;
                //这个TransportContractDetails里面返回的数据有几个字段乱搞，在这里校正下
                Source source = response.getBookOrder();
                source.setCarrierModel(new DictionaryItem(String.valueOf(source.getTransport_type()), source.getTransportType()));
                source.setTransportType(new DictionaryItem(String.valueOf(BuildConfig.carrier_type), BuildConfig.carrier));

                //初始化密码输入区域，让用户填写信息
                getView().showPasswordArea();
                //初始化订单信息
                getView().showContractInfo(response.getOrderDtlList(), response.getBookOrder().getBilling_type(), response.getCarryList(), response.getBookOrder().getCargoUnit());
                //开始倒计时
                getView().startCountingInMillis(response.getBookOrder().getExpireTimeInMilliSeconds());
                //显示合同
                getView().showHtmlContract(Html.fromHtml(getContractStr(response)));

                //自主开票货源，展示三方协议让用户勾选
                if (response.getBookOrder().getBilling_type() == 2)
                    getView().showContractArea();
                //记录数据，后面提交合同时会用到
                tmp = new ContractInfo(response.getBookOrder().getBook_uuid(), response.getOrderDtlList());
                //加载兜底服务信息
                String tips = response.getTipMessage();
                if (!TextUtils.isEmpty(tips)) {
                    getView().showProtocolTransportHint(tips);
                }
            }
        });
    }

    @Override
    protected void loadHistoryContractDetails(String contractUuid) {
        //加载历史合同详情
        OkRequest request = EasyOkHttp.get(Constant.TRANSPORT_CONTRACT_BY_CONTRACT_UUID)
                .param("contractUuid", contractUuid)
                .extra(JpptParse.ENTRY, "list")
                .tag(tag())
                .build();

        new ExecutorCall<ContractItemForDetails>(request).enqueue(new EasyCallback<ContractItemForDetails>(getView()) {
            @Override
            public void onSuccess(OkCall<ContractItemForDetails> okCall, ContractItemForDetails response) {
                //展示合同信息
                getView().showHtmlContract(Html.fromHtml(getContractStr(response, true)));
                if (!ParamUtil.isEmpty(response.getOrderDtlList())) {
                    //展示签约信息
                    getView().showSignedContractInfo(response.getOrderDtlList(), response.getBilling_type(), response.getCargoUnit());
                }
            }
        });
    }

    @Override
    public void viewContract() {
        //查看合同
        Bundle bundle = new Bundle();
        bundle.putParcelable("contract", mContractDetails);
        bundle.putBoolean("showConfirmButton", true);
        getView().toActivity(ThirdPartyContractActivity.class, bundle, 101, null);
    }

    @Override
    public void submitContract(ContractInfo contractInfo) {
        contractInfo.setExtras(tmp);//添加之前请求到的数据
        OkRequest request = contractInfo.validate(getView());
        if (request != null)
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
    }
}
