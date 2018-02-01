package com.hletong.hyc.contract;

import android.content.DialogInterface;

import com.hletong.hyc.model.CallInfo;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.model.QuoteInfos;
import com.hletong.hyc.model.Source;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.IBroadcastDataView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.IDialogView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.architect.contract.ListContract;

import java.util.List;

/**
 * Created by ddq on 2017/3/27.
 */

public interface QuoteContract {
    interface BasicView extends IBaseView {
        void initWithData(Source source);

        void initList(QuoteInfos quoteInfos, boolean guolian);
    }

    interface BasicPresenter extends IBasePresenter {
        void loadDetails();
    }

    interface View extends ListContract.View<Quote>, ICommitSuccessView, IBroadcastDataView, ITransactionView, DirectQuoteView, IDialogView {
        void initQuote(String cargoUnit, int bookRefType);

        void showAlert501(String message);

        void showAlert500(String message);

        void showAlertForUserInfoNotComplete(String content, DialogInterface.OnClickListener mListener);//用户证件资料不全，需要补全之后才能摘牌
    }

    interface Presenter extends DirectQuotePresenter {
        //        void checkUserInfo(String quoteUuid);//检查用户信息是否完整
        void accept(String quoteUuid);

        void acceptWithMU();//垫付

        void refuse(String quoteUuid);

        void refresh(List<Quote> list);
    }

    interface DirectQuotePresenter extends IBasePresenter {
        void record(CallInfo callInfo);
    }

    interface DirectQuoteView extends IBaseView {
        void showImages(List<String> images);
    }
}
