package com.hletong.hyc.contract;

import com.hletong.hyc.model.CertInfo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

import java.util.Map;

/**
 * Created by ddq on 2017/3/14.
 */

public interface CertInfoContract {
    interface View extends IBaseView {
        void initCertInfo(CertInfo info);

        void certSuccess(String message);
    }

    interface Presenter extends IBasePresenter{
        void getCertInfo();

        void summitTruckPerson(Map<String, Object> map);
    }
}
