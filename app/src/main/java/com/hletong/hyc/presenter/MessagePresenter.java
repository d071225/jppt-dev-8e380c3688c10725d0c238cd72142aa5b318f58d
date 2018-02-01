package com.hletong.hyc.presenter;

import android.text.Html;

import com.hletong.hyc.contract.MessageContract;
import com.hletong.hyc.model.FileInfo;
import com.hletong.hyc.model.MessageInfo;
import com.hletong.hyc.model.Upcoming;
import com.hletong.hyc.model.ZhiYaDan;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.model.CommonResult;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/2/8.
 * 消息
 */

public class MessagePresenter extends FilePresenter<MessageContract.View> implements MessageContract.Presenter {
    private MessageInfo mMessageInfo;
    private Upcoming mUpcoming;

    public MessagePresenter(MessageContract.View view, MessageInfo messageInfo, boolean unread) {
        super(view);
        mMessageInfo = messageInfo;
        if (unread)
            markAsRead();
    }

    public MessagePresenter(MessageContract.View view, Upcoming upcoming) {
        super(view);
        mUpcoming = upcoming;
    }

    @Override
    public void start() {
        if (mMessageInfo != null)
            getView().initCommonMessage(format(mMessageInfo.getFormatContent()), mMessageInfo.getFormatDate());
        else if (mUpcoming != null) {
            switch (mUpcoming.getType()) {
                case 11://违约单
                case 12://守约单
                case 61://卸货地变更
                case 62://卸货地变更通知
                    getView().initUpcomingWithBottomBar(mUpcoming.getDescription(), mUpcoming.getType());
                    break;
                case 14://滞压单
                    ZhiYaDan mZhiYaDan = mUpcoming.getZhiYaDan();
                    if (mZhiYaDan != null)
                        getView().initStagnation(Html.fromHtml(mZhiYaDan.getContent()));
                    break;
            }
        }
    }

    private String format(String message) {
        //app 车船配送通知单  货方收发货通知当显示友情提示
        if (mMessageInfo.isShowTip()) {
            if (mMessageInfo.isType("12")) {
                message += "\n";
                message += "<p style=\"color:red\">友情提醒：平台作为无车无船名义承运人，将承接货方会员的货运业务全部交由实际承运的车船会员运输。实际承运的车船会员自愿承担在运输过程中出现的全部赔偿责任。实际承运的车船会员同意平台在网签合同时向货方会员披露姓名、电话及车船等相关信息。发生事故时，货方会员和承保保险公司有权直接向实际承运车船会员追偿。实际承运车船会员愿意按货方会员提供的运输货物的供销合同的价格作为全额赔偿的依据。</p>";
            } else if (mMessageInfo.isType("13")) {
                message += "\n";
                message += "<p style=\"color:red\">友情提醒：平台作为无车无船名义承运人，将承接货方会员的货运业务全部交由实际承运的车船会员，实际承运的车船会员自愿承担在运输过程中出现的各种事故给货方会员造成损失的全部赔偿责任。</p>";
            }
        }
        return message;
    }

    @Override
    public void legalConfirm() {//法务单确认
        if (Validator.isNotNull(mUpcoming, getView(), "法务单确认参数出错")) {
            OkRequest request = EasyOkHttp.get(Constant.LEGAL_CONFIRM)
                    .param("legalUuid", mUpcoming.getRefUuid())
                    .param("taskerType", String.valueOf(mUpcoming.getType()))
                    .build();
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
        }
    }

    @Override
    public void resignContract() {
        if (Validator.isNotNull(mUpcoming, getView(), "补签合同参数出错")) {
            OkRequest request = EasyOkHttp.get(Constant.RESIGN_CONTRACT)
                    .param("changeUuid", mUpcoming.getRefUuid())
                    .build();
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
        }
    }

    @Override
    public void contractConfirm() {//车船合同补签确认
        if (Validator.isNotNull(mUpcoming, getView(), "确认补签合同参数出错")) {
            OkRequest request = EasyOkHttp.get(Constant.CONTRACT_RESIGN_CONFIRM)
                    .param("changeUuid", mUpcoming.getRefUuid())
                    .build();
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
        }
    }

    @Override
    public void stagnationConfirm() {//滞压单上传
        if (Validator.isNotNull(mUpcoming, getView(), "滞压单上传参数出错")) {
            OkRequest request = EasyOkHttp.get(Constant.STAGNATION_UPLOAD)
                    .param("legalUuid", mUpcoming.getRefUuid())
                    .param("StoragefileUuid", getGroupId())
                    .build();
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
        }
    }

    private void markAsRead() {
        OkRequest request = EasyOkHttp.get(Constant.MARK_AS_READ)
                .param("inform_call_uuid", mMessageInfo.getInform_call_uuid())
                .param("version", mMessageInfo.getVersion())
                .build();
        new ExecutorCall<CommonResult>(request).enqueue(new UICallback<CommonResult>() {
            @Override
            public void onError(OkCall<CommonResult> okCall, EasyError error) {
                //do nothing
            }

            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                //do nothing
            }
        });
    }

    @Override
    protected void uploadFinished(int remain, boolean error, FileInfo fileInfo) {
        if (remain == 0 && !error) {//所有图片已上传完毕，且成功
            //目前只有滞压单会上传图片
            stagnationConfirm();//提交滞压单信息
        }
    }
}
