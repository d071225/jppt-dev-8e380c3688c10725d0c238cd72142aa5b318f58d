package com.hletong.hyc.model.validate;

import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.security.EncryptUtils;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by ddq on 2017/2/24.
 */

public class CargoContractInfo implements Validate2 {
    private String loadingPwd;
    private String loadingPwdConfirm;
    private String unloadPwd;
    private String unloadPwdConfirm;

    private String cargoUuid;
    private int loans;

    public CargoContractInfo(String loadingPwd, String loadingPwdConfirm, String unloadPwd, String unloadPwdConfirm) {
        this.loadingPwd = loadingPwd;
        this.loadingPwdConfirm = loadingPwdConfirm;
        this.unloadPwd = unloadPwd;
        this.unloadPwdConfirm = unloadPwdConfirm;
    }

    public void setExtras(String cargoUuid, int loans) {
        this.cargoUuid = cargoUuid;
        this.loans = loans;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isLength(loadingPwd, 6, "发货密码必须为6位数字", baseView)
                && Validator.isSame(loadingPwd, loadingPwdConfirm, baseView, "两次输入的发货密码不一致")
                && Validator.isLength(unloadPwd, 6, "收货密码必须为6位数字", baseView)
                && Validator.isSame(unloadPwd, unloadPwdConfirm, baseView, "两次输入的收货密码不一致")
                && Validator.isNotSame(loadingPwd, unloadPwd, baseView, "收发货密码不能相同")) {
            try {
                return EasyOkHttp.get(Constant.SIGN_CONTRACT_CARGO)
                        .param("cargoUuid", cargoUuid)
                        .param("loadingPwd", EncryptUtils.md5Encrypt(loadingPwd))
                        .param("unloadPwd", EncryptUtils.md5Encrypt(unloadPwd))
                        .param("loans", loans)
                        .tag(baseView.hashCode())
                        .build();
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
