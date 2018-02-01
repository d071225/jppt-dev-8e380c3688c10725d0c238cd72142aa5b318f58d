package com.component.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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





    //==================================end====================================================




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
