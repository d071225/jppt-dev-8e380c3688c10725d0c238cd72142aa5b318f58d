package com.hletong.hyc.contract;

import android.text.Spanned;

import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by ddq on 2017/1/20.
 */

public interface MessageContract {
    interface View extends FileContract.View, ITransactionView, ICommitSuccessView {
        void initCommonMessage(String message, String data);

        void initUpcomingWithBottomBar(String message, int type);

        void initStagnation(Spanned message);//滞压单界面初始化
    }

    interface Presenter extends FileContract.Presenter {
        void legalConfirm();//法务单确认（守约单，违约单）

        void contractConfirm();//补签合同确认

        void resignContract();//货方补签合同

        void stagnationConfirm();//滞压单确认
    }
}
