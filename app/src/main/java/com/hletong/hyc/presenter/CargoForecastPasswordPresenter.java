package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.CargoForecastPasswordContract;
import com.hletong.hyc.model.CargoNoContract;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.security.EncryptUtils;
import com.hletong.mob.util.FinishOptions;
import com.hletong.mob.util.ParamsHelper;

import java.io.UnsupportedEncodingException;

/**
 * Created by dongdaqing on 2017/6/8.
 */

public class CargoForecastPasswordPresenter extends BasePresenter<CargoForecastPasswordContract.View> implements CargoForecastPasswordContract.Presenter {
    private CargoNoContract mCargo;

    public CargoForecastPasswordPresenter(CargoForecastPasswordContract.View view, CargoNoContract cargo) {
        super(view);
        this.mCargo = cargo;
    }

    @Override
    public void start() {
        if (mCargo != null) {
            getView().showHint("您发布的货源为长期协议价货源，协议单价为" + mCargo.getUnitAmt());
        }
    }

    @Override
    public void submit(String dp, String dpc, String rp, String rpc) throws UnsupportedEncodingException {
        if (mCargo == null) {
            handleMessage("状态出错，无法提交");
            return;
        }

        if (dp.length() != 6 || rp.length() != 6) {
            handleMessage("收发货密码为6位数字");
            return;
        }

        if (!dp.equals(dpc)) {
            handleMessage("发货密码与确认密码不一致");
            return;
        }

        if (!rp.equals(rpc)) {
            handleMessage("收货密码与确认密码不一致");
            return;
        }

        if (rp.equals(dp)){
            handleMessage("收发货密码不能相同");
            return;
        }

        ItemRequestValue<String> requestValue = new ItemRequestValue<String>(
                getView().hashCode(),
                Constant.getUrl(Constant.SUBMIT_AGRT_PASSWORD),
                new ParamsHelper()
                        .put("loadingPwd", EncryptUtils.md5Encrypt(dp))//发货密码
                        .put("unloadPwd", EncryptUtils.md5Encrypt(rp))//收获密码
                        .put("cargoUuid", mCargo.getCargoUuid())
                        .put("agrtPriceUuid", mCargo.getAgrtPriceUuid())
                        .put("orignUnitAmt", mCargo.getAgrtUnitAmt())) {
        };
        getDataRepository().loadItem(requestValue, new SimpleCallback<String>(getView()) {
            @Override
            public void onSuccess(String response) {
                getView().success("操作成功");
                getView().finishWithOptions(FinishOptions.FORWARD_RESULT());
            }
        });
    }
}
