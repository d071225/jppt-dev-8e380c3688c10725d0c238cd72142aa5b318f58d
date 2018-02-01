package com.hletong.hyc.model.validate;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.net.JSONUtils;
import com.hletong.mob.security.EncryptUtils;
import com.hletong.mob.util.ParamsHelper;
import com.hletong.mob.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by cx on 2016/10/26.
 */
public abstract class RegisterInfo implements Serializable, Validate<CommonResult> {
    private static final long serialVersionUID = 2135067176640459633L;
    /**
     * json序列化需要不能设置为局部的变量
     **/
    private final int member_type = BuildConfig.app_type;
    private String member_name;
    //会管编号
    private String unitCode;

    /**
     * 相同字段
     **/
    private String phoneNum;
    private String member_tel;

    /**
     * 相同字段
     **/
    private String code;
    private String messagecheck;

    private String passWord;
    @Expose(serialize = false)
    private String confirmPassword;
    /**
     * 原始密码
     **/
    @Expose(serialize = false)
    private String oriPassWord;
    private String province;
    private String city;
    private String country;
    //是否同意赣州入会协议
    private String initiation_agree = "1";
    //是否落户物流公司 1是0否
    private String settled_status = "0";
    /**
     * 备用电话
     **/
    private String first_tel;

    private String member_uuid = getUUID();
    private String sessionId = getUUID();

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Expose(serialize = false)
    private String registerUrl;

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMessagecheck() {
        return messagecheck;
    }

    public void setMessagecheck(String messagecheck) {
        this.code = messagecheck;
        this.messagecheck = messagecheck;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOriPassWord() {
        return oriPassWord;
    }

    public void setOriPassWord(String oriPassWord) {
        this.oriPassWord = oriPassWord;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMember_tel() {
        return member_tel;
    }

    public void setMember_tel(String member_tel) {
        this.phoneNum = member_tel;
        this.member_tel = member_tel;
    }

    public String getFirst_tel() {
        return first_tel;
    }

    public void setFirst_tel(String first_tel) {
        this.first_tel = first_tel;
    }

    public void setPassWord(String passWord) {
        try {
            this.oriPassWord = passWord;
            //注册提交需要加密
            this.passWord = EncryptUtils.md5Encrypt(passWord);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /***
     * call end
     ***/
    @Override
    public ItemRequestValue<CommonResult> validate(IBaseView baseView) {
        return getRequestValue(baseView.hashCode(), registerUrl);
    }


    public abstract ItemRequestValue<CommonResult> validateBaseInfo(IBaseView baseView, String url);


    public boolean validatePassword(IBaseView baseView) {
        if (StringUtil.isTrimBlank(oriPassWord)) {
            baseView.handleError(ErrorFactory.getParamError("密码不能为空"));
        } else if (!RegexUtil.isPassword(oriPassWord)) {
            baseView.handleError(ErrorFactory.getParamError("密码为6-20位字母或数字组成"));
        } else if (StringUtil.isTrimBlank(confirmPassword)) {
            baseView.handleError(ErrorFactory.getParamError("确认密码不能为空"));
        } else if (!confirmPassword.equals(oriPassWord)) {
            baseView.handleError(ErrorFactory.getParamError("两次密码不一致"));
        } else {
            return true;
        }
        return false;
    }

    /**
     * 验证手机号获取验证码
     ****/
    public ItemRequestValue<CommonResult> validatePhoneNumber(IBaseView baseView) {
        if (StringUtil.isTrimBlank(member_tel)) {
            baseView.handleError(ErrorFactory.getParamError("手机号码不能为空"));
        } else if (RegexUtil.containEmpty(member_tel)) {
            baseView.handleError(ErrorFactory.getParamError("手机号不能包含空格"));
        } else if (!RegexUtil.isPhoneNumber(member_tel)) {
            baseView.handleError(ErrorFactory.getParamError("手机号格式不正确"));
        } else {
            return getRequestValue(baseView.hashCode(), Constant.getUrl(Constant.GET_VERIFY_CODE));
        }
        return null;
    }

    /**
     * 验证手机验证码格式
     ****/
    public ItemRequestValue<CommonResult> validateVerifyCode(IBaseView baseView) {
        if (StringUtil.isTrimBlank(member_tel)) {
            baseView.handleError(ErrorFactory.getParamError("手机号码不能为空"));
        } else if (RegexUtil.containEmpty(member_tel)) {
            baseView.handleError(ErrorFactory.getParamError("手机号不能包含空格"));
        } else if (!RegexUtil.isPhoneNumber(member_tel)) {
            baseView.handleError(ErrorFactory.getParamError("手机号格式不正确"));
        } else if (StringUtil.isTrimBlank(messagecheck)) {
            baseView.handleError(ErrorFactory.getParamError("验证码不能为空"));
        } else if (messagecheck.length() != 6) {
            baseView.handleError(ErrorFactory.getParamError("验证码必须为6位"));
        } else {
            return getRequestValue(baseView.hashCode(), Constant.getUrl(Constant.CHECK_VERIFY_CODE));
        }
        return null;
    }

    protected abstract TypeToken<? extends RegisterInfo> getTypeToken();

    protected ItemRequestValue<CommonResult> getRequestValue(int hashcode, String url) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSONUtils.toJson(this, getTypeToken().getType()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ItemRequestValue<CommonResult>(hashcode, url,
                new ParamsHelper(jsonObject)) {
        };
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public int getMember_type() {
        return member_type;
    }

    /**
     * 获取登录用户名，个人账户是车船号
     *
     * @return
     */
    public String getLoginName() {
        return member_name;
    }

    public void setSettled_status(String settled_status) {
        this.settled_status = settled_status;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
