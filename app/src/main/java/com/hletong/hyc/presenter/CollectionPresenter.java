package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.CollectionContract;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/27.
 */

public class CollectionPresenter extends BasePresenter<CollectionContract.View> implements CollectionContract.Presenter {
    private String tmp;

    public CollectionPresenter(CollectionContract.View view) {
        super(view);
    }

    @Override
    public void inform(ParamsHelper ph) {
        tmp = ph.get("carrierMemberCode");
        ParamsHelper pp = new ParamsHelper();
        pp.put("jsontxt", ph.getObject().toString());
        ItemRequestValue<CommonResult> requestValue = new ItemRequestValue<CommonResult>(getView().hashCode(), Constant.getUrl(Constant.INFORM_CARRIER), pp) {
        };
        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(CommonResult response) {
                if ("通知成功".equals(response.getErrorInfo())) {
                    getView().success(response.getErrorInfo());
                    getView().updateItem(tmp);
                } else {
                    onError(new BusiError(response, null));
                }
            }
        });
    }

    @Override
    public void delete(String tradeUuid) {
        if (tradeUuid == null) {
            handleMessage("参数出错");
            return;
        }
        simpleSubmit(
                Constant.getUrl(Constant.DEL_FROM_COLLECTION),
                new ParamsHelper().put("tradeUuid", tradeUuid)
        );
    }
}
