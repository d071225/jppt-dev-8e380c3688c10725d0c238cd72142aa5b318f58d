package com.hletong.hyc.contract;

import android.support.annotation.IntDef;

import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.validate.EtcTransporterInfoPart2;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.SimpleDate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public interface VehicleInfoContract {
    int TYPE_IDENTITY = 1;
    int TYPE_SEX = 2;
    int TYPE_VALIDITY = 3;

    @IntDef({TYPE_IDENTITY, TYPE_SEX, TYPE_VALIDITY})
    @Retention(RetentionPolicy.SOURCE)
    @interface SelectorType {
    }

    interface View extends IBaseView {
        void initBasic(String name, String phone, String cardName, String plate);

        void initValidity(DictionaryItem validity, SimpleDate expireDate);

        void initIdentityNo(DictionaryItem type, String id_no);

        void initPersonalInfo(DictionaryItem sex, SimpleDate birthday);

        void initNameSpell(String spell);

        void onSuccess(String url);

        void showSelector(ArrayList<DictionaryItem> items, String type, String title, int extra);
    }

    interface Presenter extends IBasePresenter {
        void identityChanged(String identity);

        void nameChanged(String name);

        void submit(EtcTransporterInfoPart2 vehicleInfo);

        void prepareDataForSelector(@SelectorType int type, int extra);
    }
}
