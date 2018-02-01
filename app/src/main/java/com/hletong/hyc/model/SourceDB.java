package com.hletong.hyc.model;

import java.util.List;

/**
 * Created by ddq on 2017/3/28.
 */

public class SourceDB {
    private String orgin_cargon_kind_name;//品名
    private String create_ts;//创建时间

    private double unit_ct;//数量
    private String units;//单位-数字 00吨->float，02立方->float，其余单位均为int
    private String units_;//单位-中文
    private String loading_start_dt;//开始日期
    private String loading_end_dt;//结束日期
    private String loading_start_tm;//开始时间
    private String loading_end_tm;//结束时间
    private int stevedorage_days;//装卸货天数
    //装货地信息，装货人信息
    private String loading_province;
    private String loading_city;
    private String loading_country;
    private String loading_addr;
    private String loading_pwd;
    private String loading_contacts;
    private String loading_contacts_tel;
    private String loading_contacts_name;//发货人
    private String loading_contact_info_;//
    private String loading_longitude_top;//装货地经度上限
    private String loading_latitude_top;
    private String loading_latitude_bottom;
    private String loading_longitude_bottom;
    private String loading_harbor_depth;
    private String loading_latitude;//装货地纬度
    private String loading_longitude;//装货地经度
    private String loading_contacts_tax_code;//发货人纳税识别编号
    //卸货地信息，卸货人信息
    private String unload_province;
    private String unload_city;
    private String unload_country;
    private String unload_addr;
    private String unload_contacts_tel;
    private String unload_contacts;//卸货联系人
    private String unload_contacts_name;//卸货联系人
    private String unload_contacts_tax_code;//收货人纳税识别编号
    private String unload_contact_info_;
    private String unload_pwd;//收货密码
    private String unload_latitude;
    private String unload_longitude;//卸货地经度
    private String unload_longitude_top;
    private String unload_latitude_top;//卸货地纬度上限
    private String unload_longitude_bottom;//卸货地经度下限
    private String unload_latitude_bottom;//卸货地纬度下限
    private String unload_harbor_depth;//卸货港口水深
    //体积
    private double length;
    private double width;
    private double height;

    private double cargo_price;//总货值
    private String measure_type;//计量方式，别换成int类型，有些货源传输的是不是数字
    private String measure_type_;//计量方式，别换成int类型，有些货源传输的是不是数字
    private int book_ref_type = -1;//计费依据(摘牌依据) 0 => 重量, 1 => 数量
    private String book_ref_type_;//计费依据(摘牌依据) 0 => 重量, 1 => 数量
    private String settle_type;//结算方式
    private String settle_type_;//结算方式
    //车辆要求
    private String carrier_model_type;//车型要求
    private String carrier_model_type_;//车型要求
    private String carrier_length_type;//车长要求
    private String carrier_length_type_;//车长要求

    private String transport_days;//运输天数
    private double max_wastage_rt;//运输损耗
    private String special_req;//特别要求
    private String charge_refer_type;//收费依据
    private String charge_refer_type_;//收费依据
    private double transport_unit_amt;//承运单价
    private double orign_unit_amt;//原始运费单价

    private String multi_transport_flag;//是否允许多次运输 0-否
    private String multi_transport_flag_;//是否允许多次运输 0-否

    private int trans_type;//交易类型
    private String trans_type_;//交易类型

    private int settle_auth;//结算权限
    private String settle_auth_;//结算权限

    private int insure_type;//投保方式
    private String insure_type_;//投保方式

    private int billing_type = -1;//开票方式 1 => 平台开票，2 => 自主开票 3 => 自主交易
    private String billing_type_;

    private String payer_member_code;//付款人编号

    private String drawee_open_bank_name;//受票方开户行
    private String drawee_tax_code;//受票人纳税标识号
    private String drawee_name;//受票人名称
    private String drawee_addr;//受票人联系地址
    private String drawee_info_;
    private String drawee_bank_acct_no;//受票人银行账号
    private String drawee_tel;//受票人联系电话

    private String appoint_transport_flag;//是否指定运输：1 -> 是，其他 -> 否
    private String appoint_transport_flag_;//是否指定运输：1 -> 是，其他 -> 否
    private String cargo_file_id;//货物附件编号
    private List<AppointCarrier> appointTransportList;//指定车船信息
    private PayerMemberInfo payerMemberInfo;//付款人信息
}
