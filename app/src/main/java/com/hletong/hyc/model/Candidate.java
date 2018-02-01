package com.hletong.hyc.model;

import java.io.Serializable;

/**
 * 候选人实体
 * Created by chengxin on 16/7/24.
 */
public class Candidate implements Serializable {

    //报名否
    public static final String KEY_SIGN = "key_sign";
    //好司机已报名
    public static int ACT_CANDIDATE_REGISTERED = 888;
    //投票已报名
    public static int ACT_VOTE_REGISTERED = 999;
    //参选好司机和投票都未报名
    public static int ACT_ALL_UNREGISTER = 777;


    private static final long serialVersionUID = -5746695868273635443L;
    //候选人uuid
    private String reg_uuid;
    //候选人编号
    private String serial_num;
    //候选人名称
    private String reg_name;
    //手机号
    private String phone;
    //自述
    private String readme;
    //头像
    private String img_group_id;
    //省份排序
    private String province_sort;
    //得票数
    private int vote_num;
    //省份
    private String province;
    private String carrier_no;

    public String getReg_uuid() {
        return reg_uuid;
    }

    public String getSerial_num() {
        return serial_num;
    }

    public String getReg_name() {
        return reg_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getReadme() {
        return readme;
    }

    public String getImg_group_id() {
        return img_group_id;
    }

    public String getProvince_sort() {
        return province_sort;
    }

    public int getVote_num() {
        return vote_num;
    }

    public void upVote_num() {
        vote_num++;
    }

    public String getProvince() {
        return province;
    }

    public String getCarrier_no() {
        return carrier_no;
    }
}
