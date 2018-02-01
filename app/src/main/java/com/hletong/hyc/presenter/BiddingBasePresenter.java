package com.hletong.hyc.presenter;

import android.text.TextUtils;

import com.hletong.hyc.contract.CBRoundContract;
import com.hletong.hyc.model.CBHistoryItem;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.GetRequest;
import com.xcheng.okhttp.request.OkCall;

/**
 * Created by ddq on 2017/1/4.
 */
public abstract class BiddingBasePresenter<T> extends BasePresenter<CBRoundContract.View<T>> implements CBRoundContract.Presenter {
    private String bidPrice;//上一次修改的竞价价格

    public BiddingBasePresenter(CBRoundContract.View<T> view) {
        super(view);
    }

    @Override
    public void loadCargoDetails(String bidUuid) {
        GetRequest request = EasyOkHttp.get(Constant.CARGO_DETAIL_CB_HISTORY).tag(tag()).param("bidUuid", bidUuid).build();

        new ExecutorCall<CBHistoryItem>(request).enqueue(new EasyCallback<CBHistoryItem>(getView()) {
            @Override
            public void onSuccess(OkCall<CBHistoryItem> okCall, CBHistoryItem response) {
                getView().cargoLoaded(response.getRound_uuid(), response.getBidPrice(), response.canModifyBidPrice());
            }
        });
    }

    @Override
    public void modifyBidPrice(String money, String bidUuid) {
        if (TextUtils.isEmpty(money)) {
            handleMessage("请填写竞价价格");
            return;
        }
        this.bidPrice = money;//保存下来

        simpleSubmit(
                Constant.getUrl(Constant.MODIFY_CB_PRICE),
                new ParamsHelper().put("bidUuid", bidUuid).put("bidUnitAmt", money)
        );
    }

    @Override
    protected void simpleSubmitSucceed(CommonResult cr) {
        getView().bidPriceChanged(bidPrice);
    }
}
