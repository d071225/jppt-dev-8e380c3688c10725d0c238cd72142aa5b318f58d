package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.SettingContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.util.ParamsHelper;
import com.hletong.mob.util.StringUtil;

/**
 * 登录的业务处理
 * Created by cc on 2017/1/4.
 */
public class ExitMemberPresenter extends BasePresenter<SettingContract.IExitMemberView> implements SettingContract.IExitMemberPresenter {

    public ExitMemberPresenter(SettingContract.IExitMemberView view) {
        super(view);
    }

    @Override
    public void exitMember() {
        simpleSubmit(
                Constant.getUrl(Constant.REFUND_APP),
                getRequestJson()
        );
    }

    @Override
    protected void simpleSubmitSucceed(CommonResult cr) {
        getView().exitMemberSuccess();
    }

    @Override
    public boolean validate() {
        return Validator.isNotNull(getView().getExitReason(), getView(), "请输入退会原因");
    }


    private ParamsHelper getRequestJson() {
        ParamsHelper modifyJson = new ParamsHelper();
        modifyJson.put("refundAdvice", getView().getExitReason());
        if (!AppTypeConfig.isCargo()) {
            modifyJson.put("userType", getView().getUserType()).put(AppTypeConfig.getExitMemberUUIDKey(), "main");
        }
        return modifyJson;
    }
}
