package com.hletong.hyc.contract;

import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.validate.CargoInfo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.List;

/**
 * Created by ddq on 2017/3/10.
 */

public interface CargoContract {
    interface View extends FileContract.View, ICommitSuccessView, ITransactionView {
        void showTitle(String title);

        void showCargo(String cargoName, String measureType, String unit, String length, String width, String height, String loss, String transportType);

        void showImages(List<String> list);

        void showPrimaryPicker();

        void showSecondPicker();

        void showSubmitButton(String text);

        void showCarrierLength(String length);

        void hideCarrierLength();

        void showCarrierModel(String model, String label);

        void showSelector(String key, int extra);

        void prefetch(List<DictionaryItem> list);
    }

    interface Presenter extends FileContract.Presenter {
        void itemChanged(DictionaryItem dictionaryItem, int extra);

        void submit(CargoInfo cargoInfo, List<String> images);//提交货物信息

        void selectTransportModel();
    }

    interface View2 extends ICommitSuccessView, IBaseView {
        void refreshView();
    }

    interface Presenter2 extends IBasePresenter {
        void delete(String cargoUUid);//删除货物
    }
}
