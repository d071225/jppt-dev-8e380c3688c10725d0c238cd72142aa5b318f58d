package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.model.PreDeListSource;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/8/9.
 * 自主交易预摘牌
 */

public class SelfTradePreBookPresenter extends SourcePresenter {
    private String cargoUuid;
    private Source mSource;

    public SelfTradePreBookPresenter(SourceContract.View view, int type, String cargoUuid, Source source) {
        super(view, type);
        this.cargoUuid = cargoUuid;
        mSource = source;
    }

    @Override
    public void start() {
        if (mSource != null) {
            init(mSource, mSource.getCargo_code());
        }else {
            OkRequest request = EasyOkHttp.get(Constant.SELF_TRADE_PRE_DE_LIST_CARGO_DETAILS)
                    .param("cargoUuid",cargoUuid)
                    .build();

            new ExecutorCall<PreDeListSource>(request).enqueue(new EasyCallback<PreDeListSource>(getView()) {
                @Override
                public void onSuccess(OkCall<PreDeListSource> okCall, PreDeListSource response) {
                    response.setBilling_type(3);//返回的参数里面并没有这个参数，手动设置一下，不然后面的流程都走不下去了
                    init(response, response.getCargo_code());
                }
            });
        }
    }

    @Override
    protected String getTitle(int type) {
        return "预摘牌详情";
    }
}
