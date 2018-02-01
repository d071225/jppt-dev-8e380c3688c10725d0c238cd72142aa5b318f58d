package com.hletong.hyc.presenter;

import com.hletong.hyc.contract.SettingContract;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.security.Base64;
import com.hletong.mob.security.EncryptUtils;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

import java.io.UnsupportedEncodingException;

/**
 * 修改登录密码
 * Created by cx on 2017/1/4.
 */
public class ModifyPsdPresenter extends BasePresenter<SettingContract.IModifyPsdView> implements SettingContract.IIModifyPsdPresenter {
    public ModifyPsdPresenter(SettingContract.IModifyPsdView view) {
        super(view);
    }

    @Override
    public void changePassword(String oldPsw, String newPsw, String confirmPsw) {
        if (isEmpty(oldPsw)) {
            handleMessage("旧密码不能为空");
        } else if (isEmpty(newPsw)) {
            handleMessage("新密码不能为空");
        } else if (RegexUtil.containEmpty(newPsw)) {
            handleMessage("新密码不能包含空格");
        } else if (!RegexUtil.isPassword(newPsw)) {
            handleMessage("密码为6-20位字母或数字组成");
        } else if (oldPsw.equals(newPsw)) {
            handleMessage("新密码和旧密码不能一样");
        } else if (isEmpty(confirmPsw)) {
            handleMessage("旧密码不能为空");
        } else if (!confirmPsw.equals(newPsw)) {
            handleMessage("确认密和码新密码不一致");
        } else {
            try {
                OkRequest request = EasyOkHttp.get(Constant.MODIFY_PASSWORD)
                        .tag(tag())
                        .param("oldPasswd", EncryptUtils.md5Encrypt(oldPsw))
                        .param("oldPasswd2", Base64.encode(oldPsw))
                        .param("newPasswd", EncryptUtils.md5Encrypt(newPsw))
                        .build();
                easySubmit(request);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                handleMessage("加密失败");
            }
        }
    }

    @Override
    protected void easySubmitSucceed(CommonResult cr) {
        getView().success("修改成功，请重新登录");
    }
}
