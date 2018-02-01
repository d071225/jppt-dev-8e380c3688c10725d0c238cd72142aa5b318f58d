package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;
import android.text.Html;

import com.hletong.hyc.contract.Contract;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.CargoContractDetails;
import com.hletong.hyc.model.CargoContractHistoryDetails;
import com.hletong.hyc.model.validate.CargoContractInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/8/2.
 * 签约、查看合同-货方app
 */

public class CargoContractPresenter extends ContractPresenter<Contract.View> implements Contract.CargoPresenter {
    private String cargoUuid;//货方签合同用到

    public CargoContractPresenter(@NonNull Contract.View baseView, String contractUuid, String cargoUuid) {
        super(baseView, contractUuid);
        this.cargoUuid = cargoUuid;
    }

    @Override
    protected void loadContractDetails() {
        //加载合同详情
        OkRequest request = EasyOkHttp.get(Constant.CARGO_CONTRACT_BY_CARGO_UUID)
                .tag(tag())
                .param("cargoUuid", cargoUuid)
                .build();

        new ExecutorCall<CargoContractDetails>(request).enqueue(new EasyCallback<CargoContractDetails>(getView()) {
            @Override
            public void onSuccess(OkCall<CargoContractDetails> okCall, CargoContractDetails response) {
                //显示密码输入区域
                getView().showPasswordArea();
                //显示合同
                getView().showHtmlContract(Html.fromHtml(getContractStr(response)));
            }
        });
    }

    @Override
    protected void loadHistoryContractDetails(String contractUuid) {
        //加载历史合同详情
        OkRequest request = EasyOkHttp.get(Constant.CARGO_CONTRACT_BY_CONTRACT_UUID)
                .param("contractUuid", contractUuid)
                .extra(JpptParse.ENTRY, "list")
                .tag(tag())
                .build();

        new ExecutorCall<CargoContractHistoryDetails>(request).enqueue(new EasyCallback<CargoContractHistoryDetails>(getView()) {
            @Override
            public void onSuccess(OkCall<CargoContractHistoryDetails> okCall, CargoContractHistoryDetails response) {
                getView().showHtmlContract(Html.fromHtml(getContractStr(response, false)));
            }
        });
    }

    @Override
    public void submitContract(CargoContractInfo contractInfo) {
        contractInfo.setExtras(cargoUuid, 0);
        OkRequest request = contractInfo.validate(getView());
        if (request != null)
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
    }
}
