package com.hletong.hyc.presenter;

import android.support.v4.app.FragmentManager;

import com.hletong.hyc.contract.MatchedCarrierContract;
import com.hletong.hyc.model.ForecastTransporterInfo;
import com.hletong.hyc.model.MatchedCarrier;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.mob.architect.presenter.BasePresenter;

/**
 * Created by ddq on 2017/3/27.
 */

public class MatchedCarrierPresenter extends BasePresenter<MatchedCarrierContract.View> implements MatchedCarrierContract.Presenter {
    private MatchedCarrier fti;

    public MatchedCarrierPresenter(MatchedCarrier fti, MatchedCarrierContract.View view) {
        super(view);
        this.fti = fti;
    }

    @Override
    public void start() {
        if (fti == null)
            return;
        if (fti.getForecastUuid() != null) {
            getView().initForecast(fti);
        } else {
            getView().initMatched(fti);
        }
    }


    @Override
    public void call(FragmentManager manager) {
        CallPhoneDialog.getInstance().show(manager, "拨打电话", "确定要联系" + fti.getCarrierNo() + "吗？", fti.getContactTel());
    }
}
