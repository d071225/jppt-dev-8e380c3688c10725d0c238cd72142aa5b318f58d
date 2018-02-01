package com.hletong.hyc.model;

import java.io.Serializable;

/**
 * Created by ddq on 2017/2/14.
 */

public class AuthInfo implements Serializable{
    private String auth_msg;
    private String auth_flag;//是否具有摘牌的权限
    private String auth_unit_flag;//会员的会员管理单位是否跟平台签订了垫付保证金协议

    public String getAuth_msg() {
        return auth_msg;
    }

    public String getAuth_flag() {
        return auth_flag;
    }

    public boolean isAuthorized(){
        return "1".equals(auth_flag);
    }

    public boolean isBorrowable(){
        return "1".equals(auth_unit_flag);
    }
}
