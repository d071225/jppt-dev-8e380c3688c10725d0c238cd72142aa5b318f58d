package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.CargoRefusedReasonContract;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/31.
 */

public class CargoRefusedReasonPresenter extends BasePresenter<CargoRefusedReasonContract.View> implements CargoRefusedReasonContract.Presenter {
    private String cargoUuid;

    public CargoRefusedReasonPresenter(CargoRefusedReasonContract.View view, String cargoUuid) {
        super(view);
        this.cargoUuid = cargoUuid;
    }

    @Override
    public void start() {
        if (cargoUuid == null)
            return;

        ItemRequestValue<String> requestValue = new ItemRequestValue<String>(
                getView().hashCode(), Constant.getUrl(Constant.CARGO_REFUSED_REASON),
                new ParamsHelper().put("cargoUuid", cargoUuid),
                "data"
        ){};

        getDataRepository().loadItem(requestValue, new SimpleCallback<String>(getView()) {
            @Override
            public void onSuccess(@NonNull String response) {
                getView().showReason(response);
            }
        });
    }
}
