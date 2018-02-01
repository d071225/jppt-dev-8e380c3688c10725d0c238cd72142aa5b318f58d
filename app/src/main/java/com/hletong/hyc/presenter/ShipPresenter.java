package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.ShipContract;
import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.TransporterData;
import com.hletong.mob.architect.contract.ListContract;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.ListPresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.xcheng.okhttp.util.ParamUtil;

/**
 * Created by ddq on 2017/2/14.
 */

public class ShipPresenter extends ListPresenter<Ship> implements ShipContract.Presenter {

    public ShipPresenter(ListContract.View<Ship> mView) {
        super(mView);
    }

    @Override
    public void loadItem(ItemRequestValue<TransporterData<Ship>> mRequestValue) {
        getDataRepository().loadItem(mRequestValue, new SimpleCallback<TransporterData<Ship>>(getView()) {
            @Override
            public void onSuccess(@NonNull TransporterData<Ship> response) {
                if (!ParamUtil.isEmpty(response.getList())) {
                    getView().showList(response.getList(), true);
                } else {
                    onError(new DataError(ErrorState.BUSINESS_ERROR,response.getVehiclesAuth()));
                }
            }
        });
    }

    @Override
    protected String getEmptyDataDesc() {
        return BuildConfig.transporter_empty_data;
    }
}
