package com.hletong.hyc.contract;

import com.hletong.hyc.model.validate.ReceiptInfo;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.IDialogView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.List;

/**
 * Created by ddq on 2017/1/4.
 * 能否上传图片：
 * 未上传收货单 -> 可上传图片，可不传
 * 运单回传（待办消息） -> 一定要上传图片，不上传不能提交
 * 已上传收货单 -> 如果运单尚未审核通过，可以重新上传收货单，也可不重传
 * 界面初始化的步骤：
 * 可编辑 -> 初始化可编辑界面initEditState -> 如果是自主开票，初始化到票确认界面initZZKP -> 初始化共享界面initSharedViewWithData
 * 不可编辑 -> 初始化不可编辑界面initNoneEditState -> 是运单回传 -> 初始化运单回传界面initForceUpdateState -> 初始化共享界面initSharedViewWithData
 * 不可编辑 -> 初始化不可编辑界面initNoneEditState -> 不是运单回传 -> 运单是否已通过审核 -> 已通过审核 -> 初始化共享界面initSharedViewWithData
 * 不可编辑 -> 初始化不可编辑界面initNoneEditState -> 不是运单回传 -> 运单是否已通过审核 -> 未通过审核 -> 初始化运单重传界面initReUpload -> 初始化共享界面initSharedViewWithData
 */

public interface ReceiptContract {
    int FROM_UPCOMING = 1;//未上传列表
    int FROM_HISTORY = 2;//上传历史
    int FROM_MESSAGE = 3;//运单回传待办任务

    interface View extends FileContract.View, ITransactionView, ICommitSuccessView, IDialogView, LocationView {
        void showTitle(String title);

        void showContact(String label, String contact);//联系人

        void showReceipt(String doc, String cargoName, String carrierLabel, String carrier, String cargoDesc, String address, String date);//收货单信息

        void showContract();//合同查看按钮

        void showWeight(String cargo, boolean input);

        void showNumber(String cargo, String unit, boolean fraction, boolean input);

        void showPrimaryGallery(String label, String hint, int billingType);

        void showPrimaryGallery(String label, String hint, List<String> images);

        void showSecondaryGallery(String hint, int billingType);

        void showDocumentConfirm(String title);

        void showSubmitButton();

        void showPasswordArea();

        void makeCall(String contact, String tel);

//        void initEditState(Receipt receipt);
//
//        void initZZKP();
//
//        void initNoneEditState(Receipt receipt);
//
//        void initForceUpdateState(Receipt receipt);
//
//        void initReUpload();
//
//        void initSharedViewWithData(Receipt data);
//
//        void showAlertDialog(String title, String content, DialogInterface.OnClickListener listener);
    }

    interface Presenter extends FileContract.Presenter {
        void call();

        void viewContract();

        void submit(List<String> list, ReceiptInfo mReceiptInfo);
    }
}
