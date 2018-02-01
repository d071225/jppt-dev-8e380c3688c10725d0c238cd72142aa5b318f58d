package com.hletong.hyc.contract;

import com.hletong.hyc.model.Truck;
import com.hletong.hyc.model.TransporterData;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.contract.ListContract;

/**
 * Created by ddq on 2016/12/22.
 */
public interface TruckContract {
    interface Presenter extends ListContract.Presenter<Truck>{
        void loadItem(ItemRequestValue<TransporterData<Truck>> mRequestValue);
    }
}
