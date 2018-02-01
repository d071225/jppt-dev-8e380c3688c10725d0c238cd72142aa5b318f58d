package com.component.model;

/**
 * Created by ddq on 2016/10/28.
 */

public class Source {
    private String cargo_code;//货物代码 == 业务单编号
    private String cargo_uuid;//
    private String round_uuid;//

    private boolean _disableRow_;//货源公告当前item是否可以点击
    private int round_trans_type;//交易类型 1 => 竞价 2 => 挂价 300 => 议价
    private String round_trans_type_;//交易类型 1 => 竞价 2 => 挂价 300 => 议价
    private String orgin_cargon_kind_name;//品名
    private double unit_ct;//数量
    private String units;//单位-数字 00吨->float，02立方->float，其余单位均为int
    private String units_;//单位-中文
    private double weight;//重量
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

    private String car_model_type;// 车型要求编码(车辆运输)
    private String car_model_type_;// 车型要求中文(车辆运输)

    private String truck_model_type;
    private String truck_length_type;

    //船舶要求
    private String ship_model_type;// 船型要求编码(船舶运输)
    private String ship_model_type_;// 船型要求中文(船舶运输)

    private String transport_days;//运输天数
    private double max_wastage_rt;//运输损耗
    private String special_req;//特别要求
    private String charge_refer_type;//收费依据
    private String charge_refer_type_;//收费依据
    private double transport_unit_amt;//承运单价
    private double rem_qt;//待签约保留量---真实返回的数据里面用的这个，文档里面查不到的
    private double reserve_qt;//待签约保留量---这个是假的，但是说明文档里面写的这个
    private double accept_qt;//已签约承运量
    private double orign_unit_amt;//原始运费单价
    private double confer_unit_amt;//协商运费单价

    private double transport_unit_price;//运输收入--仅限平开货源使用，非平开货源请使用其他单价字段
    private double other_unit_amt;//其他收入

    private String status;//状态
    private String status_;//状态

    private String cargo_status;
    private String cargo_owner_sname;//货方会员名称
    private String cargo_agent_sname;//货方代理名称

    private String cargo_owner_tel;// 货方联系电话
    private String cargo_owner_fax;// 货方传真号码
    private String cargo_owner_addr;// 货方联系地址

    private String multi_transport_flag;//是否允许多次运输 0-否
    private String multi_transport_flag_;//是否允许多次运输 0-否
    private int wrtr_transport_flag;//是否兜底运输
    private double per_min_qt;//单次摘牌最小量
    private double transport_tax_rt;//承运税率
    private double wrtr_serv_unit_amt;//兜底服务单价,计算运费时如果当前货物是兜底运输，那么要用这个字段
    private double unit_weight;//单位重量
    private int trans_type;//交易类型， 1 => 竞价 2 => 挂价，300 => 议价
    private String trans_type_;//交易类型
    private int settle_auth;//结算权限
    private String settle_auth_;//结算权限
    private double last_min_rem_qt;//尾单最小剩余量
    private int insure_type;//投保方式
    private String insure_type_;//投保方式
    private double deduct_tax_rt;

    private String confer_cargo_price;//协商总货物价值

    private String premium_unit_amt;//单位保费
    private String create_ts;//创建时间
    private String appr_dttm;//审批时间
    private int billing_type = -1;//开票方式 1 => 平台开票，2 => 自主开票 3 => 自主交易
    private String billing_type_;
    private String cargo_src_type;//货物来源类型，trans_type为300同时cargo_src_type为4 货物是国联议价
    private String cargo_spare_tel;//备用联系电话
    private String cargo_agent_code;//货方代理编号
    private String cargo_owner_code;//货方会员编号
    private double total_unit_amt;//费用总单价，包含了税之类的其他费用，货方需要支付的运费

    private String payer_member_code;//付款人编号

    private String drawee_open_bank_name;//受票方开户行
    private String drawee_tax_code;//受票人纳税标识号
    private String drawee_name;//受票人名称
    private String drawee_addr;//受票人联系地址
    private String drawee_info_;
    private String drawee_bank_acct_no;//受票人银行账号
    private String drawee_tel;//受票人联系电话

    private String estimate_unit_amt;//测算运费单价
    private String agrt_price_flag;//是否长期协议价
    private String agrt_price_flag_;
    private String agrt_price_uuid;//长期协议价协议编号

    private String match_status;//匹配状态
    private String match_status_;//匹配状态
    private String bill_req;//开票要求
    private String last_mod_dttm;//最后修改时间
    private int transport_type = -1;//运输方式 1 =>车辆 2 => 船舶
    private String transport_type_;//运输方式

    private String appoint_transport_flag;//是否指定运输：1 -> 是，其他 -> 否
    private String appoint_transport_flag_;//是否指定运输：1 -> 是，其他 -> 否
    private String insurance_rt;//保费费率
    private String revoke_qt;//撤销量
    private String cargo_file_id;//货物附件编号
    private String cargo_owner_sub_code;//货方子账号
    private String trans_round_ct;//竞挂价交易场次
    private String accepter_code;//受理人
    private String match_phase;//匹配阶段

    private String trans_fee_rt;//交易费比例
    private String cargo_kind_code;//货品代码
    private String appr_code;//审批人
    private String cargo_owner_mgt_unit_code;//货方会员管理单位
    private String profit_amt;//平台收益
    private String version;

    //下面的字段不是从服务端返回，是将上面的一个或多个字段处理之后得到的，由于列表滑动过程会
    // 不断取值，这里把处理过得值缓存下来，避免做无用功
    private String cached_loading_period;
    private String cached_create_ts;

}
