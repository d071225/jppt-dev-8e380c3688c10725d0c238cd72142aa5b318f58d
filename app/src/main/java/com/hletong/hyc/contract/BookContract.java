package com.hletong.hyc.contract;

import android.content.DialogInterface;

import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.model.validate.Validate2;
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
 * 摘牌过程进入界面有两个数据要获取： 1.检查E商贸通是否开通，2.获取车辆（船舶）列表
 * 如果是自主交易：先1后2
 * 非自主交易：先2后1
 */

public interface BookContract {
    String BID_INFO_CHANGED = "com.hletong.hyc.bid_info_changed";
    String QUOTE_INFO_CHANGED = "quote_data_changed";

    interface View extends IBaseView, ITransactionView, IBroadcastDataView, IReceiverView, ICommitSuccessView, IDialogView {
        void showTitle(String title);

        void showSourceDetailFragment();//展示货源详情

        void showTransporter(String label);//显示承运车船

        void showInputView(String label, String hint, String unit, boolean fraction);//展示输入框

        void disableInputView(String label, String cargo);//禁止用户输入

        void showDeductRateSelector(List<Source.DeductRt> list);//展示抵扣税率选择框

        void showIncome(String label, int colorRes);//展示合计价格栏

        void updateIncome(String income);//更新合计价格

        void showPrice(String label, String unit, String hint);//展示价格栏

        void showButton(String text);//展示提交按钮

        void showAlertForUserInfoNotComplete(String content, DialogInterface.OnClickListener mListener);//用户资料不全，需要补全

        void showSubmitAlertDialog(DialogInterface.OnClickListener mListener);

        void showProtocolTransportHint(CharSequence hint);//兜底运输提示
    }

    interface Presenter extends IBasePresenter {
        void sourceLoaded(Source source);

        void priceChanged(String price);//报价或是竞价金额发生了变化

        void cargoChanged(String cargo);//摘牌数(重)量发生了变化
    }

    //竞价
    interface BiddingView extends View {
        void showBiddingHallView();
    }

    interface BiddingPresenter extends Presenter {
        void toBiddingHall();

        void submit(Validate2 validate2);
    }

    //挂价
    interface NormalView extends View {
        void showContractCheckbox();//展示合同

        void showAlertForBorrowInsuranceMoney();//自主交易垫付提示框

        String getCarrier();

        String getCargo();
    }

    interface NormalPresenter extends Presenter {
        void viewContract(int requestCode, String cargo);//查看三方合同

        void bookWithAdvance();//通过会员管理单位垫付

        void submit(PayMode payMode);
    }

    //报价
    interface QuoteView extends NormalView {
        void showMenu();//撤销议价

        void showViewsAtLeft();

        void prefetchTransporter();//预加载车船信息

        void updateTransporter(String transporter);

        void updatePrice(String price);

        void updateCargo(String cargo);

        void makeCall(String tel);//国联数据，打电话

        void showModifyView(String unit, int bookType, String book, String price);

        void priceModified(String message);

        Validate2 getSubmitInfo();
    }

    interface QuotePresenter extends NormalPresenter {
        void action();

        void modifyOffer(String price, String count);

        void record();//记录国联议价

        void revoke();//撤销报价
    }
}
