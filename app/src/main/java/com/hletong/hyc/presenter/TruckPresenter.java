package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.TruckContract;
import com.hletong.hyc.model.TransporterData;
import com.hletong.hyc.model.Truck;
import com.hletong.mob.architect.contract.ListContract;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.ListPresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.xcheng.okhttp.util.ParamUtil;

/**
 * Created by ddq on 2016/12/12.
 */
public class TruckPresenter extends ListPresenter<Truck> implements TruckContract.Presenter {

    public TruckPresenter(ListContract.View<Truck> mView) {
        super(mView);
    }

    public void loadItem(ItemRequestValue<TransporterData<Truck>> mRequestValue) {
        getDataRepository().loadItem(mRequestValue, new SimpleCallback<TransporterData<Truck>>(getView()) {
            @Override
            public void onSuccess(@NonNull TransporterData<Truck> response) {
                if (!ParamUtil.isEmpty(response.getList())) {
                    getView().showList(response.getList(), true);
                } else {
                    onError(new DataError(ErrorState.BUSINESS_ERROR, response.getVehiclesAuth()));
                }
            }
        });
    }

    @Override
    protected String getEmptyDataDesc() {
        return BuildConfig.transporter_empty_data;
    }
}
