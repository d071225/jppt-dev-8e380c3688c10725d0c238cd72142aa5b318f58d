package com.hletong.hyc.contract;

import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.amap.api.location.AMapLocation;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.IBroadcastDataView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.IDialogView;
import com.hletong.mob.architect.view.IReceiverView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.List;

/**
 * Created by ddq on 2017/1/5.
 * 下面是不同的情况下SourceDetailsActivity的界面初始化流程
 * <p>
 * 货源详情可以通过货源公告、挂架摘牌、竞价投标列表界面进入，自主交易预摘牌完成进入
 * 1.竞价 -> 竞价界面初始化 initCB -> 是不是自主交易，是的话initSelfTradeDetails，不是的话initNormalDetails -> 初始化共享部分界面 initWithData
 * 2.挂价 -> 挂价界面初始化 initGJ -> 是不是自主交易，是的话initSelfTradeDetails，不是的话initNormalDetails -> 初始化共享部分界面 initWithData
 * 待办-自主交易，通过消息里面的待办任务列表进入
 * 初始化待办任务界面initUpcomingSelfTrade -> 初始化自主交易详情initSelfTradeDetails，初始化共享部分界面initWithData
 * 自主交易预摘牌（垫付保证金）
 * initPreDeListSelfTradeDetails -> initSelfTradeDetails -> initWithData
 */

public interface SourceContract {
    String PLATFORM_BIDDING_PRICE_CHANGED = "SourceContract.platform_bidding_price_changed";
    String SOURCE_LOADED = "com.hletong.jppt.SOURCE_LOADED";

    interface View extends IBaseView, ICommitSuccessView, LocationView, ITransactionView, IBroadcastDataView, IReceiverView, IDialogView {
        void showTitle(String title);

        void showRequests(String requests);

        void showCargoName(String cargoName);

        void showCargo(String cargo);//货源重(数)量

        void showAddress(String from, String to);

        void showTransporterRequire(int icon, String require);

        void showLoadingDate(String date);

        void showTransportDays(int icon, String days);

        void showCargoPrice(String cargoPrice);

        void showCargoInfo(String docNo, String value, String volume, String measureType, String chargeType, String billingType, String settleType, String multiTransport, String transportLoss, String tax);

        void showCargoInfo(String docNo, String volume);

        void showImages(List<String> images);

        void showMenu(int menu, Toolbar.OnMenuItemClickListener listener);//路径规划或者竞价大厅

        void showCallBlock(String label, String data);//自主交易拨号

        void showBiddingPriceView(String unit);//平台开票竞价

        void hideBiddingPriceView();

        void showBiddingButton();//竞价历史-竞价按钮

        void showBookInfo(String label, String cargo, String price);

        void showUnitBlock();

        void showActionButtons();//自主交易确认单，底部有"违约投诉"和"完成交易"两个按钮

        void makeCall(String contact, String tel, CallPhoneDialog.OnCalledListener listener);

        String getCarrier();
    }

    interface Presenter extends IBasePresenter {
        //        void loadSource(ItemRequestValue<Source> mRequestValue);
//
//        void loadCBHistory(ItemRequestValue<CBHistoryItem> mRequestValue);
//
//        void loadPreDeListSource(ItemRequestValue<PreDeListSource> mRequestValue);
//
//        注意这里加载的是待办详情（即这个货源被摘过了之后生成的待办任务的详情），
//         不是货源详情，不是货源详情，不是货源详情
//         要获取自主交易的货源详情应调用上面的loadSource函数
//        void loadUpcomingSource(ItemRequestValue<SelfTradeDetails> mRequestValue);
//

        void call();

        void quoteChanged(Quote quote);

        void complain();//自主交易确认单投诉

        void confirm(@Nullable AMapLocation location);//自主交易确认单完成确认

    }
}
