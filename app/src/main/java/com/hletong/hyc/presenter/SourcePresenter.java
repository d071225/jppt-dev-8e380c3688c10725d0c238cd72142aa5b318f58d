package com.hletong.hyc.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.amap.api.location.AMapLocation;
import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.CallInfo;
import com.hletong.hyc.model.FileInfo;
import com.hletong.hyc.model.PreDeListSource;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.PathPlanActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.fragment.BiddingHallFragment;
import com.hletong.hyc.ui.fragment.PlatformBiddingHallFragment;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.hletong.mob.util.NumberUtil;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2017/1/5.
 * 货源详情
 */

public class SourcePresenter extends BasePresenter<SourceContract.View> implements SourceContract.Presenter {

    public static final int DEFAULT = 0;
    public static final int GUAJIA = 1;//挂价货源
    public static final int CB = 2;//竞价货源
    public static final int CB_HISTORY = 4;//竞价历史
    public static final int TRANSPORTER_UPCOMING_SELF_TRADE = 5;//自主交易待办任务
    public static final int CARGO_UPCOMING_SELF_TRADE = 6;//自主交易待办任务
    public static final int PRE_DE_LIST_SELF_TRADE = 7;//申请垫付保证金-进行的预摘牌信息展示
    public static final int QUOTE = 8;//自主交易-议价
    public static final int QUOTE_PROGRESS = 9;//自主交易-议价进度(车船)，这个跟上面的议价相似度很高，但不相等

    /**
     * 页面展示的数据类型
     * {@value DEFAULT} 默认
     * {@value GUAJIA} 挂价
     * {@value CB} 竞价
     * {@value CB_HISTORY} 竞价历史
     * {@value TRANSPORTER_UPCOMING_SELF_TRADE,CARGO_UPCOMING_SELF_TRADE} 自主交易待办任务
     * {@value PRE_DE_LIST_SELF_TRADE} 垫付保证金的摘牌
     * {@value QUOTE} 自主交易-议价
     */
    private int mType;
    private Source mSource;
    //用户拨打电话的时候需要对行为进行记录，这里有很多种情况，
    // 不同情况下参数不一样，详见下面的初始化步骤
    private CallInfo mCallInfo;

    public SourcePresenter(SourceContract.View view, int type) {
        super(view);
        mType = type;
    }

    @Override
    public final void quoteChanged(Quote quote) {
        mCallInfo = quoteChanged(quote, mSource);
    }

    @Override
    public void complain() {

    }

    @Override
    public void confirm(@Nullable AMapLocation location) {
        //自主交易确认单完成确认
    }

    protected CallInfo quoteChanged(Quote quote, Source source) {
        return null;
    }

    @Override
    public void call() {
        if (Validator.isNotNull(mCallInfo, getView(), "状态出错")) {
            getView().makeCall(mSource.getEncryptedLoadingContact(), mCallInfo.getCalledTel(), new CallPhoneDialog.OnCalledListener() {
                @Override
                public void onCalled() {
                    record();
                }
            });
        }
    }

    private void record() {
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

    private void download(String group) {
        if (group != null && !group.equals("null")) {
            OkRequest request = EasyOkHttp
                    .get(String.format(Constant.getUrl(Constant.FETCH_GROUP_PICTURES_URL), group))
                    .tag(tag())
                    .build();

            new ExecutorCall<Images>(request).enqueue(new EasyCallback<Images>(getView(), true) {
                @Override
                public void onSuccess(OkCall<Images> okCall, Images response) {
                    if (response.empty())
                        return;

                    List<String> images = new ArrayList<>(response.list.size());
                    for (int index = 0; index < response.list.size(); index++) {
                        images.add(response.list.get(index).getFileDownloadUrl());
                    }
                    getView().showImages(images);
                }
            });
        }
    }

    //界面初始化
    protected void init(Source source, String docNo) {
        mSource = source;
        mCallInfo = getCallInfo(source, mType);
        //界面标题
        getView().showTitle(getTitle(mType));
        //特殊要求
        if (!"无".equals(source.getSpecial_req())) {
            getView().showRequests(source.getSpecial_req());
        }
        //货物名称
        getView().showCargoName(source.getOrgin_cargon_kind_name());
        //如果是挂价、竞价、议价使用余量描述，其他使用货源总量描述
        if (mType == GUAJIA || mType == CB || mType == QUOTE) {
            getView().showCargo(source.getRemainCargoDescWithUnit());
        } else {
            getView().showCargo(source.getCargoDescWithUnit());
        }
        //货物地址
        getView().showAddress(source.getLoadingAddress(), source.getUnLoadingAddress());
        //车辆(船舶)需求
        getView().showTransporterRequire(BuildConfig.icon_model, BuildConfig.transporter_require + "：" + source.getTransporterRequests());
        //装货时间段
        if (source.hasTimeString()) {
            getView().showLoadingDate("装货时间段：" + source.getLoadingPeriodWithTime());
        } else {
            getView().showLoadingDate("装货时间段：" + source.getLoadingPeriod());
        }

        //根据不同的类型初始化不同的界面
        if (source.getBilling_type() == 3) {//自主交易
            //拨号
            if (mType != PRE_DE_LIST_SELF_TRADE)
                getView().showCallBlock("货方联系人", source.getEncryptedLoadingContact());
            //详情
            getView().showCargoInfo(docNo, source.getVolume("/"));

            if (mType == TRANSPORTER_UPCOMING_SELF_TRADE || mType == CARGO_UPCOMING_SELF_TRADE) {
                getView().showBookInfo("承运" + source.getBookRefType(), source.getCargoDescWithUnit(), source.getTransportUnitAmtFee());
            } else if (mType != QUOTE && mType != QUOTE_PROGRESS && mType != PRE_DE_LIST_SELF_TRADE) {
                getView().showCargoPrice("参考交易单价：" + source.getSelfTradeReferUnitPrice());
            }
        } else {
            getView().showTransportDays(BuildConfig.icon_transport_days, "运输天数：" + source.getTransPortDays());
            getView().showCargoInfo(
                    docNo,
                    source.getCargoPriceInThousands(),
                    source.getVolume("/"),
                    source.getMeasure_type(),
                    source.getChargeReferType(),
                    source.getBilling_type_(),
                    source.getSettle_type(),
                    source.getMultiTransportFlag(),
                    source.getMaxWastageRtWithUnit(),
                    source.getTransportTaxRtWithUnit());

            getView().showCargoPrice("参考交易单价：" + source.getSelfTradeReferUnitPrice());

            //平台开票
            if (source.getBilling_type() == 1) {
                getView().showCargoPrice("运输收入：" + source.getTransport_unit_price() + " " + source.getUnitForFee() +
                        "\n" + "其他收入：" + source.getOther_unit_amt() + " " + source.getUnitForFee() +
                        "\n" + "合计收入：" + NumberUtil.format2f(source.getTransport_unit_price() + source.getOther_unit_amt()) + " " + source.getUnitForFee());
                //竞价输入价格
                if (source.getTrans_type() == 1)
                    getView().showBiddingPriceView(source.getUnitForFee());
            }
        }
        //下载图片
        download(source.getCargoFileId());

        //路径规划，暂时只支持车
        if (AppTypeConfig.isTruck() && (mType == GUAJIA || mType == CB || mType == QUOTE)) {
            getView().showMenu(R.menu.path_plan, new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(PathPlanActivity.START_ADDRESS, mSource.getStartAddress());
                    bundle.putParcelable(PathPlanActivity.END_ADDRESS, mSource.getEndAddress());
                    getView().toActivity(PathPlanActivity.class, bundle, null);
                    return true;
                }
            });
        }

        //竞价历史：界面右上角的竞价大厅入口
        if (mType == CB_HISTORY) {
            getView().showMenu(R.menu.cb_hall, new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    getView().toActivity(
                            CommonWrapFragmentActivity.class,
                            CommonWrapFragmentActivity.getBundle(
                                    "竞价大厅",
                                    mSource.getBilling_type() == 1 ? PlatformBiddingHallFragment.class : BiddingHallFragment.class,
                                    BiddingHallFragment.getBundleParams(mSource, false)),
                            null);
                    return true;
                }
            });

            if (source.getRound_uuid() != null) {
                //如果竞价场次非空，说明可以再次竞价
                getView().showBiddingButton();
            }
        }

        //自主交易预摘牌
        if (mType == PRE_DE_LIST_SELF_TRADE) {
            if (source instanceof PreDeListSource) {
                PreDeListSource pds = (PreDeListSource) source;
                getView().showCargoName(String.format("%s(%s)", source.getOrgin_cargon_kind_name(), pds.getCarrierNo()));
            } else {
                getView().showCargoName(String.format("%s(%s)", source.getOrgin_cargon_kind_name(), getView().getCarrier()));
            }

            getView().showUnitBlock();
        }

        //交易确认单，底部有两个按钮
        if (mType == TRANSPORTER_UPCOMING_SELF_TRADE || mType == CARGO_UPCOMING_SELF_TRADE) {
            getView().showActionButtons();
        }
    }

    protected CallInfo getCallInfo(Source source, int type) {
        return null;
    }

    protected String getTitle(int type) {
        return null;
    }

    private class Images {
        private String fileGroupId;
        private List<FileInfo> list;

        boolean empty() {
            return ParamUtil.isEmpty(list);
        }
    }
}
