package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.CBRoundContract;
import com.hletong.hyc.model.CBItem;
import com.hletong.hyc.model.CBPlatformRoundItem;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.GetRequest;
import com.xcheng.okhttp.request.OkCall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongdaqing on 2017/7/19.
 */

public class PlatformBiddingPresenter extends BiddingBasePresenter<CBPlatformRoundItem> {

    public PlatformBiddingPresenter(CBRoundContract.View<CBPlatformRoundItem> view) {
        super(view);
    }

    @Override
    public void loadTime(String roundUuid) {
        GetRequest request = EasyOkHttp.get(Constant.COMPETITIVE_ROUND_TIME_LEFT).tag(tag()).param("roundUUID", roundUuid).build();

        new ExecutorCall<CBItem<CBPlatformRoundItem>>(request).enqueue(new EasyCallback<CBItem<CBPlatformRoundItem>>(getView()) {
            @Override
            public void onSuccess(OkCall<CBItem<CBPlatformRoundItem>> okCall, CBItem<CBPlatformRoundItem> response) {
                getView().timeLoaded(response.getEndDttm(), response.getRemQt());
                List<CBPlatformRoundItem> list = response.getBidRespDtos();
                if (list == null)
                    list = new ArrayList<>();
                final int size = list.size();
                if (size < 8) {
                    for (int i = 0; i < 8 - size; i++) {
                        list.add(new CBPlatformRoundItem(true));
                    }
                }
                getView().showList(list, true);
            }
        });
    }
}
