package com.hletong.hyc.presenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.model.validate.SourceInfo;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.ThirdPartyContractActivity;
import com.hletong.hyc.ui.fragment.SourceDetailsFragment;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.util.FinishOptions;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/8/3.
 */

public class NormalBookPresenter<V extends BookContract.NormalView> extends BookPresenter<V> implements BookContract.NormalPresenter {
    private PayMode lastSubmit;

    public NormalBookPresenter(V view) {
        super(view);
    }

    protected boolean showContract() {
        return getSource().getBilling_type() == 3 && getSource().getTrans_type() != 300;
    }

    @Override
    public void sourceLoaded(Source source) {
        super.sourceLoaded(source);

        //是否展示自主交易三方合同：非议价的自主交易要展示合同
        if (showContract()) {
            getView().showContractCheckbox();
        }
    }

    @Override
    public void viewContract(int requestCode, String cargo) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(cargo)) {
            bundle.putDouble("cargo", Double.parseDouble(cargo));
        }
        bundle.putString("cargoUuid", getSource().getCargo_uuid());
        bundle.putInt("bookRefType", getSource().getBook_ref_type());
        bundle.putBoolean("showConfirmButton", true);
        getView().toActivity(ThirdPartyContractActivity.class, bundle, requestCode, null);
    }

    @Override
    public void bookWithAdvance() {
        //会员管理单位垫付
        lastSubmit.setPayMode(1);
        submitInternal(lastSubmit);
    }

    @Override
    protected void bookFailedWhileAdvanceAvailable() {
        getView().showAlertForBorrowInsuranceMoney();
    }

    @Override
    public final void submit(PayMode payMode) {
        lastSubmit = payMode;
        submitInternal(payMode);
    }

    protected void submitInternal(PayMode payMode) {
        SourceInfo sourceInfo = (SourceInfo) payMode;
        sourceInfo.setBookType(getSource().getBook_ref_type());
        sourceInfo.setExtras(getSource().getRound_uuid(), getSource().getCargo_uuid(), getSource().shouldDisplayDeductRt(), getSource().getRem_qt(), getSource().getCargoUnit(), getSource().getWrtr_serv_unit_amt());
        sourceInfo.setSelfTrade(getSource().getBilling_type() == 3);
        final OkRequest request = payMode.validate(getView());
        if (request != null) {
            getView().showSubmitAlertDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //自主交易和非自主交易解析的返回内容不一样
                    if (getSource().getBilling_type() == 3) {
                        //在生成参数的时候把这个放进去了，但这里并不需要，下面那个请求才需要
                        new ExecutorCall<CommonResult>(request).enqueue(new DataHandler<CommonResult>(getView()) {
                            @Override
                            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                                selfTradeBookSucceed();
                            }
                        });
                    } else {
                        new ExecutorCall<String>(request).enqueue(new DataHandler<String>(getView()) {
                            @Override
                            public void onSuccess(OkCall<String> okCall, String response) {
                                //跳转到签合同界面
                                getView().success("摘牌成功");
                                Bundle bundle = new Bundle();
                                bundle.putString("bookUuid", response);
                                bundle.putString("title", "签约合同");
                                getView().toActivity(AppTypeConfig.getContractActivity(), bundle, FinishOptions.FORWARD_RESULT());
                            }
                        });
                    }
                }
            });
        }
    }

    protected void selfTradeBookSucceed() {
        if (lastSubmit.getPayMode() == 1) {
            //会员管理单位垫付，跳转到预摘牌见面
            Bundle bundle = new Bundle();
            bundle.putInt("type", SourcePresenter.PRE_DE_LIST_SELF_TRADE);//类型为垫付保证金预摘牌
            bundle.putString("carrier", getCarrier());
            Source source = getSource();
            if (source.getBook_ref_type() == 0) {
                source.setWeight(getCargo());
            } else {
                source.setUnit_ct(getCargo());
            }

            bundle.putParcelable("source", source);
            getView().toActivity(
                    CommonWrapFragmentActivity.class,
                    CommonWrapFragmentActivity.getBundle("自主交易", SourceDetailsFragment.class, bundle),
                    FinishOptions.FORWARD_RESULT());
        } else {
            //提示并退出
            getView().showDialog(false, "摘牌成功", "摘牌成功，请及时联系货方", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getView().finishWithOptions(FinishOptions.FORWARD_RESULT());
                }
            }, null);
        }
    }

    protected double getCargo() {
        return Double.parseDouble(getView().getCargo());
    }

    protected String getCarrier() {
        return getView().getCarrier();
    }
}
