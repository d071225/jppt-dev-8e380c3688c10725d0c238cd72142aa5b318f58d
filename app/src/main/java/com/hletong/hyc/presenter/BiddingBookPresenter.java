package com.hletong.hyc.presenter;

import android.content.DialogInterface;
import android.content.Intent;

import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.model.BidInfo;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.BiddingHallFragment;
import com.hletong.hyc.ui.fragment.PlatformBiddingHallFragment;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.http.EasyCallback;
import com.hletong.mob.util.FinishOptions;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.GetRequest;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/8/3.
 * 竞价投标
 */

public class BiddingBookPresenter extends BookPresenter<BookContract.BiddingView> implements BookContract.BiddingPresenter {
    private BidInfo mBidInfo;//竞价的单价信息

    public BiddingBookPresenter(BookContract.BiddingView view) {
        super(view);
    }

    @Override
    public void start() {
        //注册要接收的广播
        getView().register(SourceContract.SOURCE_LOADED, SourceContract.PLATFORM_BIDDING_PRICE_CHANGED);
        getView().showSourceDetailFragment();
    }

    @Override
    public void sourceLoaded(Source source) {
        super.sourceLoaded(source);
        //显示竞价大厅按钮
        getView().showBiddingHallView();
    }

    @Override
    public void toBiddingHall() {
        //平台开票和自主开票进入不同的界面
        getView().toActivity(
                CommonWrapFragmentActivity.class,
                CommonWrapFragmentActivity.getBundle(
                        "竞价大厅",
                        getSource().getBilling_type() == 1 ? PlatformBiddingHallFragment.class : BiddingHallFragment.class,
                        BiddingHallFragment.getBundleParams(getSource(), false)),
                null);
    }

    @Override
    public void submit(Validate2 validate2) {
        //提交竞价信息
        SourceInfo sourceInfo = (SourceInfo) validate2;
        sourceInfo.setBookType(getSource().getBook_ref_type());
        sourceInfo.setExtras(getSource().getRound_uuid(), getSource().getCargo_uuid(), getSource().shouldDisplayDeductRt(), getSource().getRem_qt(), getSource().getCargoUnit(), getSource().getWrtr_serv_unit_amt());
        if (getSource().getBilling_type() == 1) {
            //平台开票的竞价价格
            sourceInfo.setBidPrice(mBidInfo);
        }

        final OkRequest request = validate2.validate(getView());
        if (request != null) {
            getView().showSubmitAlertDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new ExecutorCall<CommonResult>(request).enqueue(new DataHandler<CommonResult>(getView()) {
                        @Override
                        public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                            //弹出对话框
                            success();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void priceChanged(String price) {
        if (getSource().getBilling_type() == 1) {
            EasyOkHttp.cancel(Constant.CALCULATE_BID_PRICE);//取消之前的请求

            //平台开票竞价，向服务器获取价格信息
            GetRequest request = EasyOkHttp
                    .get(Constant.CALCULATE_BID_PRICE)
                    .tag(Constant.CALCULATE_BID_PRICE)
                    .param("cargoUuid", getSource().getCargo_uuid())
                    .param("bookUnitAmt", price)
                    .build();

            new ExecutorCall<BidInfo>(request).enqueue(new EasyCallback<BidInfo>(getView(), true) {
                @Override
                public void onSuccess(OkCall<BidInfo> okCall, BidInfo response) {
                    mBidInfo = response;
                    //计算运费并更新界面
                    calculateTotalPrice(null);
                    Intent intent = new Intent(BookContract.BID_INFO_CHANGED);
                    String s = "运输收入：" + response.transport_unit_price + getSource().getUnitForFee() +
                            "\n" + "其他收入：" + response.other_unit_amt + getSource().getUnitForFee() +
                            "\n" + "合计收入：" + response.getSum() + getSource().getUnitForFee();
                    intent.putExtra("data", s);
                    getView().broadcast(intent);
                }
            });
        } else
            super.priceChanged(price);
    }

    @Override
    protected String calculatePlatformBiddingPrice(double cargo) {
        if (mBidInfo != null) {
            return mBidInfo.getCargoFee(cargo);
        }
        return "0";
    }

    private void success() {
        getView().showDialog(false, "投标成功", "赶快去竞价大厅看看您的排名吧", "确定", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getView().toActivity(
                        CommonWrapFragmentActivity.class,
                        CommonWrapFragmentActivity.getBundle(
                                "竞价大厅",
                                getSource().getBilling_type() == 1 ? PlatformBiddingHallFragment.class : BiddingHallFragment.class,
                                BiddingHallFragment.getBundleParams(getSource(), false)),
                        FinishOptions.FINISH_ONLY()
                );
            }
        }, null);
    }
}
