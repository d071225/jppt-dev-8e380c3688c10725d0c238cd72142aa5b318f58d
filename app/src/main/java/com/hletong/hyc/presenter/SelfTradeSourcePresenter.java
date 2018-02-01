package com.hletong.hyc.presenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.CallInfo;
import com.hletong.hyc.model.SelfTradeDetails;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.SelfTradeComplainActivity;
import com.hletong.hyc.ui.activity.TransporterEvaluationActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.http.EasyCallback;
import com.hletong.mob.util.FinishOptions;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/8/9.
 * 交易确认单（自主交易完成确认）
 */

public class SelfTradeSourcePresenter extends SourcePresenter {
    private String tradeUuid;
    private String confirmType;

    public SelfTradeSourcePresenter(SourceContract.View view, int type, String tradeUuid, String confirmType) {
        super(view, type);
        this.tradeUuid = tradeUuid;
        this.confirmType = confirmType;
    }

    @Override
    public void start() {
        getView().startLocating(false);//只有平台开票才强制开定位，这里是自主交易，所以不强制
        OkRequest request = EasyOkHttp.get(Constant.SELF_TRADE_DETAILS)
                .param("tradeUuid", tradeUuid)
                .extra(JpptParse.ENTRY, "data")
                .build();

        new ExecutorCall<SelfTradeDetails>(request).enqueue(new EasyCallback<SelfTradeDetails>(getView()) {
            @Override
            public void onSuccess(OkCall<SelfTradeDetails> okCall, SelfTradeDetails response) {
                response.setBilling_type(3);//返回的参数里面并没有这个参数，手动设置一下，不然后面的流程都走不下去了
                init(response, response.getTrade_code());
            }
        });
    }

    @Override
    protected CallInfo getCallInfo(Source source, int type) {
        SelfTradeDetails std = (SelfTradeDetails) source;
        if (AppTypeConfig.isCargo()) {
            return CallInfo.CONFIRM(std.getCarrier_contact(), null, std.getCarrier_contact_tel(), std.getTrade_uuid());
        } else {
            return CallInfo.CONFIRM(std.getCargo_owner_sname(), std.getCargo_owner_code(), std.getCargo_owner_tel(), std.getTrade_uuid());
        }
    }

    @Override
    public void complain() {
        //自主交易确认单投诉
        Bundle bundle = new Bundle();
        bundle.putString("tradeUuid", tradeUuid);
        bundle.putString("confirmType", confirmType);
        getView().toActivity(SelfTradeComplainActivity.class, bundle, null);
    }

    @Override
    public void confirm(@Nullable final AMapLocation aMapLocation) {
        getView().showDialog("自主交易确认", "确认交易完成？如有违约待处理，将会自动结束。", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OkRequest request = EasyOkHttp.get(Constant.SELF_TRADE_CONFIRM)
                        .param("tradeUuid", tradeUuid)
                        .param("confirmType", AppTypeConfig.getSelfTradeConfirmType())//车船为2，货方是1
                        .param("latitude", aMapLocation != null ? aMapLocation.getLatitude() : "")
                        .param("longitude", aMapLocation != null ? aMapLocation.getLongitude() : "")
                        .build();

                new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(getView()) {
                    @Override
                    public void onError(OkCall<CommonResult> okCall, EasyError error) {
                        if ((error instanceof BusiError) && ((BusiError) error).getBusiCode() == 500000) {
                            /**
                             * 服务端返回的地址信息出错，客户端这里重新定位，让用户手动重传
                             */
                            getView().onLocationError("您的位置与卸货地地址不一致，请在卸货地确认");
                        } else {
                            getView().handleError((BaseError) error);
                        }
                    }

                    @Override
                    public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                        getView().success(response.getErrorInfo());
                        if (AppTypeConfig.isCargo()) {
                            //货方完成自主交易之后需给车船进行评价
                            Bundle bundle = new Bundle();
                            bundle.putString("tradeUuid", tradeUuid);
                            bundle.putString("billingType", "3");
                            getView().toActivity(TransporterEvaluationActivity.class, bundle, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, FinishOptions.FORWARD_RESULT());
                        } else {
                            //车船直接结束
                            getView().finishWithOptions(FinishOptions.FORWARD_RESULT());
                        }
                    }
                });
            }
        });
    }

    @Override
    protected String getTitle(int type) {
        return "自主交易";
    }
}
