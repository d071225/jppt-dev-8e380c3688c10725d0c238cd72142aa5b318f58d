package com.hletong.hyc.model;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.HyApplication;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.ui.dialog.SessionTimeoutDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.UnicornHelper;
import com.hletong.mob.security.Base64;
import com.hletong.mob.security.EncryptUtils;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.SPUtils;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.GetRequest;
import com.xcheng.okhttp.request.OkRequest;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cc on 2016/10/18.
 */
public class LoginInfo implements Serializable {

    private static final long serialVersionUID = 9220604498748258285L;
    private String classify;
    private String user_status;
    private String user_sub_type;
    private String branch_path;
    private String is_submember;
    private String member_code;
    private String user_name;
    private String agent_ext;
    @SerializedName("__sid")
    private String sId;
    private String user_key;
    private String login_name;
    private String branch_no;
    private String user_type;
    private String user_code;
    private String branch_name;
    private String member_classify;
    private String is_agent;
    private String user_role_codes;
    //会员所属会员管理单位信息
    private String mm_biz_contact;//会员管理单位联系人
    private String mm_biz_contact_tel;//会员管理单位联系人电话
    private String mm_unit_name;//会员管理单位名称
    private String mm_unit_code;
    private String mm_unit_uuid;
    /**
     * 设置显示会员评价积分,子账号才有的功能
     */
    //会员积分
    private int member_grade;
    //交易笔笔数
    private int trade_account;

    //会员的摘牌权限信息
    private LinkedTreeMap<String, AuthInfo> mm_trade_auth;//这是本来的数据，但是不能序列化
    private ArrayList<AuthInfo> mm_trade_auth_parsed;//这个是把上面的取出来之后，按照 平台开票，自主开票，自主交易 的顺序放到这个list里面

    public String getMember_code() {
        return member_code;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getLogin_name() {
        return login_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getMember_classify() {
        return member_classify;
    }

    public String getUser_code() {
        return user_code;
    }

    //==================================维护登录信息====================================================

    private static LoginInfo sLoginInfo;

    /**
     * 保存登录信息
     *
     * @param loginInfo 获取到的对象
     */
    public static void setLoginInfo(@NonNull LoginInfo loginInfo, String loginName, String password) {
        loginInfo.parseAuthInfo();
        SPUtils.putBoolean("dirtyData", true);//设置缓存数据需要重新获取

        SPUtils.putBean(Constant.SP_KEY.KEY_LOGIN_INFO, loginInfo, new TypeToken<LoginInfo>() {
        });
        AppManager.setLoginNameAndPassword(loginName, password);
        sLoginInfo = loginInfo;
        UnicornHelper.setUserInfo();
    }

    public static OkRequest request(String loginName, String password, Object tag) {
        GetRequest.Builder builder = EasyOkHttp.get(Constant.LOGIN)
                .tag(tag);
        try {
            SPUtils.putString("RequestInfo", "loginName[" + loginName + "] password[" + password + "]");
            builder.param("loginName", loginName)
                    .param("userPwd", EncryptUtils.md5Encrypt(password))
                    .param("userPwd2", Base64.encode(password))
                    .param("userType", BuildConfig.app_type)
                    .param("channel", "android")
                    .param("updateDate", SimpleDate.formatDate(new Date(), SimpleDate.formatterLoginDate))
                    .param("deviceType", "2")
                    .param("clientId", AppManager.PUSH_ID)
                    .param("versionCode", AppManager.versionCode).extra(JpptParse.TYPETOKEN, new TypeToken<LoginInfo>() {
            });
            AMapLocation location = HyApplication.getAMapLocation();
            if (location != null) {
                builder.param("longitude", location.getLongitude());
                builder.param("latitude", location.getLatitude());
            }
        } catch (UnsupportedEncodingException e) {
            SPUtils.putString("UnsupportedEncodingException", e.getMessage());
            e.printStackTrace();
        }
        return builder.build();

    }

    public static void loginDialog() {
        Intent intent = new Intent(AppManager.getContext(), SessionTimeoutDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppManager.getContext().startActivity(intent);
    }

    /**
     * 清除登录信息
     */
    public static void removeLoginInfo() {
        SPUtils.remove(Constant.SP_KEY.KEY_LOGIN_INFO);
        //退出登录清空__sid
        AppManager.removeCookie();
        AppManager.removePassword();
        sLoginInfo = null;
    }

    /**
     * 获取登录信息
     *
     * @return current LoginInfo
     */
    public static LoginInfo getLoginInfo() {
        if (sLoginInfo != null) {
            return sLoginInfo;
        }
        return SPUtils.getBean(Constant.SP_KEY.KEY_LOGIN_INFO, new TypeToken<LoginInfo>() {
        });
    }

    //==================================end====================================================

    /**
     * 是否已经登录
     *
     * @return true代表已经登录
     */
    public static boolean hasLogin() {
        return getLoginInfo() != null;
    }

    /****
     * 1代表公司，2代表个人
     ***/
    public static boolean isCompany() {
        LoginInfo info = getLoginInfo();
        return info != null && "1".equals(info.member_classify);
    }

    //判断用户登录的是否为子帐号，user_type返回4是子账号
    public static boolean isChildAccount() {
        LoginInfo info = getLoginInfo();
        return info != null && "4".equals(info.user_type);
    }

    private void parseAuthInfo() {
        if (mm_trade_auth == null)
            return;
        mm_trade_auth_parsed = new ArrayList<>(3);
        mm_trade_auth_parsed.add(mm_trade_auth.get("PLATFORM"));//平台开票
        mm_trade_auth_parsed.add(mm_trade_auth.get("ONESELF"));//自主开票
        mm_trade_auth_parsed.add(mm_trade_auth.get("SELF_TRADE"));//自主交易
        mm_trade_auth = null;//置空
    }

    public AuthInfo getAuthInfo(int index) {
        if (mm_trade_auth_parsed != null && index >= 1 && index <= mm_trade_auth_parsed.size()) {
            return mm_trade_auth_parsed.get(index - 1);
        }
        return null;
    }

    public String getMm_unit_name() {
        return mm_unit_name;
    }

    public String getMm_biz_contact() {
        return mm_biz_contact;
    }

    public String getMm_biz_contact_tel() {
        return mm_biz_contact_tel;
    }

    public int getMember_grade() {
        return member_grade;
    }

    public int getTrade_account() {
        return trade_account;
    }
}
