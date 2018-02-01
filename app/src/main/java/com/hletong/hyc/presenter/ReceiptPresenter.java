package com.hletong.hyc.presenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.ReceiptContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.FileInfo;
import com.hletong.hyc.model.Images;
import com.hletong.hyc.model.Receipt;
import com.hletong.hyc.model.validate.ReceiptInfo;
import com.hletong.hyc.ui.activity.TransporterEvaluationActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.util.FinishOptions;
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
 * Created by ddq on 2017/1/4.
 */

public class ReceiptPresenter extends FilePresenter<ReceiptContract.View> implements ReceiptContract.Presenter {
    private Receipt mReceipt;
    private int type;
    private ReceiptInfo mReceiptInfo;

    public ReceiptPresenter(ReceiptContract.View mView, int type, Receipt mReceipt) {
        super(mView);
        this.mReceipt = mReceipt;
        this.type = type;
    }

    @Override
    public void start() {
        if (mReceipt == null)
            return;

        getView().showReceipt(
                mReceipt.getReceipt_code(),
                mReceipt.getOrgin_cargon_kind_name(),
                BuildConfig.carrier_name,
                mReceipt.getCarrier_no(),
                mReceipt.getCargoNumberAndWeightWithUnit('|'),
                mReceipt.getUnLoadingAddress(),
                mReceipt.getLoadingPeriod());

        getView().showContact("卸货联系人", mReceipt.getEncryptedUnLoadContact());

        //货方不能查看合同
        if (AppTypeConfig.isTransporter())
            getView().showContract();

        boolean hasAttach = !TextUtils.isEmpty(mReceipt.getWaybill_file_id()) && !"null".equals(mReceipt.getWaybill_file_id());

        switch (type) {
            case ReceiptContract.FROM_HISTORY:
                getView().showTitle("收货单信息");
                getView().showWeight(mReceipt.getR_weight(), false);
                getView().showNumber(mReceipt.getR_unit_ct(), mReceipt.getCargoUnit(), false, false);
                if (mReceipt.getWaybill_status() == 0 || mReceipt.getWaybill_status() == 1) {
                    //运单尚未审核通过，可以重传
                    getView().showSubmitButton();
                    if (hasAttach) {
                        loadImages();
                        /**
                         * 这里billingType传1并不是说当前货源对应的开票方式是平开，传1只是为了控制提交按钮的状态
                         * billingType为1的时候不传图片是不能点击提交按钮的，仅此而已。下面的是一个道理
                         */
                        getView().showSecondaryGallery("(最多三张)", 1);
                    } else {
                        getView().showPrimaryGallery("运单回传", "(最多三张)", 1);
                    }
                } else if (hasAttach) {
                    loadImages();
                }
                break;
            case ReceiptContract.FROM_MESSAGE:
                getView().showTitle("运单回传");
                getView().showWeight(mReceipt.getR_weight(), false);
                getView().showNumber(mReceipt.getR_unit_ct(), mReceipt.getCargoUnit(), false, false);
                getView().showSubmitButton();
                if (hasAttach) {
                    loadImages();
                    getView().showSecondaryGallery("(必填：最多三张)", 1);
                } else {
                    getView().showPrimaryGallery("运单回传", "(必填：最多三张)", 1);
                }
                break;
            case ReceiptContract.FROM_UPCOMING:
                getView().showTitle("上传收货单");
                getView().showWeight(mReceipt.getS_weight(), true);
                getView().showNumber(mReceipt.getS_unit_ct(), mReceipt.getCargoUnit(), mReceipt.withFraction(), true);
                getView().showPasswordArea();
                /**
                 * 平台开票强制传运单，不传不能点提交按钮
                 * 其他类型不传运单也可提交
                 */
                getView().showPrimaryGallery("运单回传", mReceipt.getBilling_type() == 1 ? "(必填：最多三张)" : "", mReceipt.getBilling_type());
                getView().showSubmitButton();
                if (mReceipt.getBilling_type() == 2) {
                    if (AppTypeConfig.isCargo()) {
                        getView().showDocumentConfirm("到票有异议，同意拒付运费");
                    } else {
                        getView().showDocumentConfirm("到票有异议，同意货方延迟支付运费");
                    }
                }
                getView().startLocating(mReceipt != null && mReceipt.getBilling_type() == 1);//开始定位
                break;
        }
    }

    private void loadImages() {
        OkRequest request = EasyOkHttp.get(String.format(Constant.getUrl(Constant.FETCH_GROUP_PICTURES_URL), mReceipt.getWaybill_file_id())).build();

        new ExecutorCall<Images>(request).enqueue(new UICallback<Images>() {
            @Override
            public void onError(OkCall<Images> okCall, EasyError error) {

            }

            @Override
            public void onSuccess(OkCall<Images> okCall, Images response) {
                if (response.empty())
                    return;
                List<String> images = new ArrayList<>(response.getList().size());
                for (int index = 0; index < response.getList().size(); index++) {
                    images.add(response.getList().get(index).getFileDownloadUrl());
                }
                if (type == ReceiptContract.FROM_HISTORY) {
                    boolean accepted = mReceipt.getWaybill_status() != 0 && mReceipt.getWaybill_status() != 1;
                    getView().showPrimaryGallery("已上传纸质运单凭证", accepted ? "（已审核）" : "（可重传）", images);
                } else {
                    getView().showPrimaryGallery("已上传纸质运单凭证", "（审核未通过）", images);
                }
            }
        });
    }

    //提交收货单
    private void submit(ReceiptInfo mReceiptInfo) {
        mReceiptInfo.setExtras(getGroupId(), mReceipt.getReceipt_uuid(), mReceipt.getTrade_uuid());
        OkRequest request = mReceiptInfo.validate(getView());
        if (request == null)
            return;

        new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(getView()) {
            @Override
            public void onError(OkCall<CommonResult> okCall, EasyError error) {
                if ((error instanceof BusiError) && ((BusiError) error).getBusiCode() == 500000) {
                    /**
                     * 服务端返回的地址信息出错，客户端这里重新定位，让用户手动重传
                     */
                    getView().onLocationError("您的定位和卸货地地址不一致，请确认后再回传");
                } else {
                    getView().handleError((BaseError) error);
                }
            }

            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                getView().success(response.getErrorInfo());
                if (AppTypeConfig.isCargo()) {
                    //货方app在上传完收货单之后需要对会员进行评价
                    Bundle bundle = new Bundle();
                    bundle.putString("tradeUuid", mReceipt.getTrade_uuid());
                    bundle.putString("billingType", String.valueOf(mReceipt.getBilling_type()));
                    getView().toActivity(TransporterEvaluationActivity.class, bundle, FinishOptions.FORWARD_RESULT());
                } else {
                    //车船上传完毕之后直接返回前一个界面
                    getView().finishWithOptions(FinishOptions.FORWARD_RESULT());
                }
            }
        });
    }

    /**
     * 待办消息里面的运单回传强制要上传图片
     *
     * @param images
     * @return
     */
    @Override
    protected boolean isParamsValid(List<String> images) {
        if (mReceipt.getBilling_type() == 1 && ParamUtil.isEmpty(images)) {
            handleMessage("请先选择图片");
            return false;
        }

        return true;
    }

    //提交运单回传待办信息
    private void submit() {
        OkRequest request = EasyOkHttp.get(Constant.UPLOAD_RECEIPT_UPCOMING)
                .tag(tag())
                .param("receiptUuid", mReceipt.getReceipt_uuid())
                .param("tradeUuid", mReceipt.getTrade_uuid())
                .param("waybillFileId", getGroupId())
                .build();
        new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
    }

    @Override
    public void call() {
        getView().makeCall(mReceipt.getEncryptedUnLoadContact(), mReceipt.getUnload_contacts_tel());
    }

    @Override
    public void viewContract() {
        Bundle bundle = new Bundle();
        bundle.putString("contractUuid", mReceipt.getContract_uuid());//前面保存过，直接取用
        bundle.putString("title", "查看合同");
        getView().toActivity(AppTypeConfig.getContractActivity(), bundle, null);
    }

    @Override
    public void submit(final List<String> list, ReceiptInfo receiptInfo) {
        this.mReceiptInfo = receiptInfo;
        if (type == ReceiptContract.FROM_UPCOMING) {
            if (mReceiptInfo.validate(getView()) == null)
                return;
            //如果填写的收货数（重）量和发货数（重）量不一致，要给用户一个提示
            if (!mReceiptInfo.isSame(mReceipt)) {
                getView().showDialog("上传收货单", "回传重量或数量与发货单量不等，确认提交？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        upload(list, Constant.getUrl(Constant.IMAGE_UPLOAD),false);
                    }
                });
                return;
            }
        }
        upload(list, Constant.getUrl(Constant.IMAGE_UPLOAD),false);
    }

    @Override
    protected void uploadFinished(int remain, boolean error, FileInfo fileInfo) {
        //所有图片上传完毕
        if (remain == 0 && !error) {
            switch (type) {
                case ReceiptContract.FROM_MESSAGE:
                case ReceiptContract.FROM_HISTORY:
                    submit();
                    break;
                case ReceiptContract.FROM_UPCOMING:
                    submit(mReceiptInfo);
                    break;
            }
        }
    }
}
