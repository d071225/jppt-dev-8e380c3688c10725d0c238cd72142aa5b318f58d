package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.model.CBHistoryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/8/7.
 */

public class BiddingHistorySourcePresenter extends SourcePresenter {
    private String bidUuid;

    public BiddingHistorySourcePresenter(SourceContract.View view, int type, String bidUuid) {
        super(view, type);
        this.bidUuid = bidUuid;
    }

    @Override
    public void start() {
        OkRequest request = EasyOkHttp
                .get(Constant.CARGO_DETAIL_CB_HISTORY)
                .param("bidUuid", bidUuid)
                .build();

        new ExecutorCall<CBHistoryItem>(request).enqueue(new EasyCallback<CBHistoryItem>(getView()) {
            @Override
            public void onSuccess(OkCall<CBHistoryItem> okCall, CBHistoryItem response) {
                init(response, response.getBidder_code());
            }
        });
    }

    @Override
    protected void init(Source source, String docNo) {
        super.init(source, docNo);
        getView().hideBiddingPriceView();
    }

    @Override
    protected String getTitle(int type) {
        return "历史详情";
    }
}
