package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.contract.ListContract;

/**
 * Created by ddq on 2017/1/4.
 */

public interface CBRoundContract {
    interface View<T> extends ListContract.View<T> {
        void timeLoaded(long end, String remain);

        void cargoLoaded(String roundUUID, String bidPrice, boolean canModifyBidPrice);

        void bidPriceChanged(String money);
    }

    interface Presenter extends IBasePresenter {
        void loadTime(String roundUuid);

        void loadCargoDetails(String bidUuid);

        void modifyBidPrice(String money, String bidUuid);
    }
}
