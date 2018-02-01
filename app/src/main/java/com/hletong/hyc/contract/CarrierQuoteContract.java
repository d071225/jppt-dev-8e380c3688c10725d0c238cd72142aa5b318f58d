package com.hletong.hyc.contract;

import com.hletong.hyc.model.Quote;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.model.validate.Validate;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.view.IBroadcastDataView;

/**
 * Created by ddq on 2017/3/29.
 */

public interface CarrierQuoteContract {
    interface View extends BookContract.View, IBroadcastDataView {

    }

    interface Presenter extends BookContract.Presenter {

    }
}
