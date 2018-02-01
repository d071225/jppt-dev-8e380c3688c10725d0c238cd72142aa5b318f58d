package com.hletong.hyc.contract;

import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.TransporterData;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.contract.ListContract;

/**
 * Created by ddq on 2017/2/14.
 */

public interface ShipContract {
    interface Presenter extends ListContract.Presenter<Ship>{
        void loadItem(ItemRequestValue<TransporterData<Ship>> mRequestValue);
    }
}
