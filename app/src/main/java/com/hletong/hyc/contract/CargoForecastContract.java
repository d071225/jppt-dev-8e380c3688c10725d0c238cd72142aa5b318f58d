package com.hletong.hyc.contract;

import android.content.Intent;

import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.IDialogView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ddq on 2017/3/13.
 */

public interface CargoForecastContract {
    interface View extends FileContract.View, ICommitSuccessView, ITransactionView, IDialogView {
        void chooseDefaultType();

        void tradeTypeChanged(DictionaryItem dictionaryItem, Source source);

        boolean gatherSource(Source source);

        void preview(Source source);

        void gatherSubmitData(HashMap<String, String> params, int billingType);

        void showSelfTradeAlert(String message);

        void showPasswordDialog();//输入长期协议价密码
    }

    interface Presenter extends FileContract.Presenter {
        void submit(String memberCode);

        void tradeTypeSelected(DictionaryItem item, boolean init);

        void preview();

        void saveData();
//        void submit();
    }

    interface View2 extends FileContract.View {

    }

    interface Presenter2 extends FileContract.Presenter {
        void onActivityResult(int requestCode, Intent data);
    }

    interface BlockView extends IBaseView, ITransactionView {
        void showBlock();

        void hideBlock();

        void open();

        void close();

        void showView(int... id);

        void hideView(int... id);

        void showMessage(String message);
    }


    interface BlockPresenter extends IBasePresenter {
        void sourceAttached(Source source);

        void onActivityResult(int requestCode, Intent data);

        void billingTypeChanged(int billingType);

        void dictionarySelected(String key, DictionaryItem item);

        void dataChanged(String event, Object data);

        Map<String, String> getParams();
    }

    /********************************** 其他信息 ********************************************/
    interface BlockOtherView extends BlockView {
        void updateTradeType(String name);

        void showSpecialRequest(String request);

        String getSpecialRequest();
    }

    interface BlockOtherPresenter extends BlockPresenter {

    }

    /********************************** 装货信息 ********************************************/
    interface BlockLoadView extends BlockUnloadView {
        void showDate(String from, String to);

        void showTime(String from, String to);
    }

    interface BlockLoadPresenter extends BlockPresenter {
        void chooseContact();
    }

    /********************************** 卸货信息 ********************************************/
    interface BlockUnloadView extends BlockView {
        void showAddress(String address);

        void showContact(String contact, String tel);
    }

    interface BlockUnloadPresenter extends BlockPresenter {
        void chooseContact();

        void viewTransportRoute();
    }

    /********************************** 开票及投保信息 ********************************************/
    interface BlockInsuranceView extends BlockView {

    }

    interface BlockInsurancePresenter extends BlockPresenter {

    }

    /********************************** 付款及开票信息 ********************************************/
    interface BlockBillView extends BlockView {

    }

    interface BlockBillPresenter extends BlockPresenter {

    }

    /********************************** 运输信息 ********************************************/
    interface BlockTransportView extends BlockView {

    }

    interface BlockTransportPresenter extends BlockPresenter {

    }

    /********************************** 自主交易信息 ********************************************/
    interface BlockSelfCargoView extends BlockView {

    }

    interface BlockSelfCargoPresenter extends BlockPresenter {

    }

    /********************************** 非自主交易货物信息 ********************************************/
    interface BlockCargoView extends BlockView {
        void showCargo(String cargoName, String measureType, String unit, String loss, String volume);

        void showCarrierModel(String model);

        void showCarrierLength(String length);

        void showImages(List<String> images);

        void updateUnitMethod(int method);
    }

    interface BlockCargoPresenter extends BlockPresenter {
        void selectCargo();
    }

    /********************************** 货方联系人 ********************************************/
    interface BlockContactView extends BlockView {

    }

    interface BlockContactPresenter extends BlockPresenter {

    }
}
