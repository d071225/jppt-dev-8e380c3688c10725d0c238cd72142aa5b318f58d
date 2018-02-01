package com.hletong.hyc.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.ReceiptContract;
import com.hletong.hyc.model.PushData;
import com.hletong.hyc.model.QuoteUpcoming;
import com.hletong.hyc.model.Receipt;
import com.hletong.hyc.model.Upcoming;
import com.hletong.hyc.presenter.SourcePresenter;
import com.hletong.hyc.ui.activity.BookActivityBase;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.MessageInfoActivity;
import com.hletong.hyc.ui.activity.QuoteProgressActivity;
import com.hletong.hyc.ui.activity.ReceiptUploadActivityNew;
import com.hletong.hyc.ui.activity.TransporterEvaluationActivity;
import com.hletong.hyc.ui.fragment.SourceDetailsFragment;
import com.hletong.mob.util.AppManager;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.orhanobut.logger.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dongdaqing on 2017/8/11.
 * 推送数据处理
 */

public class GTDataService extends GTIntentService {
    private static final AtomicInteger flag = new AtomicInteger();
    private final String NOTIFICATION = "gt_notification";
    private final int FROM_NOTIFICATION = 20171001;

    @Override
    public void onReceiveServicePid(Context context, int i) {
       // Logger.d("GTDataService pid: " + i);
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        //Logger.d("GTDataService clientId: " + s);
        AppManager.PUSH_ID = s;
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        String s = new String(gtTransmitMessage.getPayload());
        PushData pushData = new Gson().fromJson(s, PushData.class);
        Logger.d("GTDataService data:" + pushData.getJsonText());
        //待办任务
        if ("100".equals(pushData.getBizType())) {
            handleMessage(pushData.getJsonText());
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
      //  Logger.d("GTDataService online: " + b);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
       // Logger.d("GTDataService command result: " + gtCmdMessage.getAction());
    }

    private void fire(String title, String content, Intent intent) {
        if (title != null && content != null) {
            flag.incrementAndGet();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(title);
            builder.setContentText(content);
            if (intent != null){
                intent.setAction(AppManager.appId + "_notification_action:" + flag.get());//每个notification设置一个独立pendingIntent
                builder.setContentIntent(PendingIntent.getActivity(this, FROM_NOTIFICATION, intent, PendingIntent.FLAG_ONE_SHOT));
            }
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setGroup(AppManager.appId + "_notification:" + flag.get());//防止通知被group
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(NOTIFICATION, flag.get(), builder.build());
            if (flag.get() > 65535)
                flag.set(0);
        }
    }

    private void handleMessage(String s) {
        /******************************************************************************************/
        /*以下内容必须跟{@link com.hletong.hyc.adapter.UpcomingAdapter.LegalViewHolder}事件处理保持一致*/
        /******************************************************************************************/
        String title = null;
        String content = null;
        Intent intent = null;
        if (s.contains("\"type\":")) {
            Upcoming upcoming = new Gson().fromJson(s, Upcoming.class);
            title = upcoming.getType_();
            content = upcoming.getDescription();
            //自主交易结束确认
            if (upcoming.getType() == 201 || upcoming.getType() == 200) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", upcoming.getType() == 201 ? SourcePresenter.TRANSPORTER_UPCOMING_SELF_TRADE : SourcePresenter.CARGO_UPCOMING_SELF_TRADE);//类型为待办-自主交易
                bundle.putString("tradeUuid", upcoming.getStringFromParams("tradeUuid"));
                bundle.putString("confirmType", upcoming.getStringFromParams("confirmType"));

                intent = CommonWrapFragmentActivity.getIntentForTransAction(this, "自主交易", SourceDetailsFragment.class, bundle);
            }

            //车船-自主交易预摘牌
            else if (upcoming.getType() == 203) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", SourcePresenter.PRE_DE_LIST_SELF_TRADE);
                Logger.d("upcoming => " + upcoming.getStringFromParams("uniqueId"));
                bundle.putString("cargoUuid", upcoming.getStringFromParams("uniqueId"));

                intent = CommonWrapFragmentActivity.getIntentForTransAction(this, "自主交易", SourceDetailsFragment.class, bundle);
            }

            //货方会员-自主交易议价进度
            else if (upcoming.getType() == 204) {
                intent = new Intent(this, QuoteProgressActivity.class);
                intent.putExtra("cargoUuid", upcoming.getQuoteEntity().getCargoUuid());
            }

            //车船会员-自主交易报价进度
            else if (upcoming.getType() == 205) {
                intent = BookActivityBase.getBookActivity(this, SourcePresenter.QUOTE_PROGRESS);
                QuoteUpcoming qu = upcoming.getQuoteEntity();
                intent.putExtra("cargoUuid", qu.getCargoUuid());
                intent.putExtra("quoteUuid", qu.getQuoteUuid());
                intent.putExtra("type", SourcePresenter.QUOTE_PROGRESS);
            } else if (upcoming.getType() == 300) {//会员评价
                intent = new Intent(this, TransporterEvaluationActivity.class);
                intent.putExtra("tradeUuid", upcoming.getStringFromParams("tradeUuid"));
                intent.putExtra("billingType", upcoming.getStringFromParams("billingType"));
            } else {
                switch (upcoming.getType()) {
                    case 11://违约单
                    case 12://守约单
                        title = getString(R.string.legal_title);
                        break;
                    case 14://滞压单
                        title = getString(R.string.stagnation_title);
                        break;
                    case 61:
                    case 62://卸货地变更,补签货方合同，车船确认
                        title = getString(R.string.resign_contract_title);
                        break;
                }
                intent = new Intent(this, MessageInfoActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("upcoming", upcoming);
                intent.putExtra("content", MessageInfoActivity.TODO);
            }
        } else if (s.contains("\"todoType\":")) {
            Receipt receipt = new Gson().fromJson(s, Receipt.class);
            title = "运单回传";
            content = receipt.getContentDescription();

            intent = new Intent(this, ReceiptUploadActivityNew.class);
            intent.putExtra("receipt", receipt);
            intent.putExtra("type", ReceiptContract.FROM_MESSAGE);
        }

        fire(title, content, intent);
    }
}
