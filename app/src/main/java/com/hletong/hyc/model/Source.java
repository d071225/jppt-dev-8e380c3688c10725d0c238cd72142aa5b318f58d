package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hletong.mob.dialog.selector.IItemShow;
import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;
import com.hletong.mob.util.StringUtil;

import java.util.List;
import java.util.Locale;

/**
 * Created by ddq on 2016/10/28.
 */

public class Source implements Parcelable {
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
    private WrtrInfo wrtrTips;//兜底服务信息
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
    private List<DeductRt> deduction_tax_rate;//车辆会员为公司性质同时还是平台开票的时候，如果货源也是平台开票，那么就会有抵扣税费这一项
    private List<AppointCarrier> appointTransportList;//指定车船信息
    private PayerMemberInfo payerMemberInfo;//付款人信息
    private String version;

    //下面的字段不是从服务端返回，是将上面的一个或多个字段处理之后得到的，由于列表滑动过程会
    // 不断取值，这里把处理过得值缓存下来，避免做无用功
    private String cached_loading_period;
    private String cached_create_ts;

    private SimpleDate startDt;
    private SimpleDate endDt;
    private SimpleDate startTm;
    private SimpleDate endTm;

    public String getFormatCreateTs() {
        if (cached_create_ts == null) {
            SimpleDate simpleDate = SimpleDate.parse(create_ts);
            cached_create_ts = simpleDate.getDateDescriptionWithTime();
        }
        return cached_create_ts;
    }

    public void setCreate_ts(String create_ts) {
        this.create_ts = create_ts;
    }

    public void setCargo_uuid(String cargo_uuid) {
        this.cargo_uuid = cargo_uuid;
    }

    public void setCargo_price(double cargo_price) {
        this.cargo_price = cargo_price;
    }

    public void setTransport_unit_amt(double transport_unit_amt) {
        this.transport_unit_amt = transport_unit_amt;
    }

    public void setBookRefType(DictionaryItem di) {
        if (di != null) {
            book_ref_type = di.getIdAsInt();
            book_ref_type_ = di.getText();
        }
    }

    public void setMeasureType(DictionaryItem di) {
        if (di != null) {
            this.measure_type = di.getId();
            this.measure_type_ = di.getText();
        }
    }

    public DictionaryItem getBookRefTypeAsDictionaryItem() {
        if (book_ref_type == 0)
            return new DictionaryItem("0", "重量");
        else if (book_ref_type == 1)
            return new DictionaryItem("1", "数量");
        return null;
    }

    public double getWrtr_serv_unit_amt() {
        return wrtr_serv_unit_amt;
    }

    public double getTransport_unit_price() {
        return transport_unit_price;
    }

    public double getOther_unit_amt() {
        return other_unit_amt;
    }

    public void setOrgin_cargon_kind_name(String orgin_cargon_kind_name) {
        this.orgin_cargon_kind_name = orgin_cargon_kind_name;
    }

    public void setCargo_owner_sname(String cargo_owner_sname) {
        this.cargo_owner_sname = cargo_owner_sname;
    }

    public void setCargo_owner_tel(String cargo_owner_tel) {
        this.cargo_owner_tel = cargo_owner_tel;
    }

    public void setCargo_owner_fax(String cargo_owner_fax) {
        this.cargo_owner_fax = cargo_owner_fax;
    }

    public void setCargo_owner_addr(String cargo_owner_addr) {
        this.cargo_owner_addr = cargo_owner_addr;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setUnits(String units, String units_) {
        this.units = units;
        this.units_ = units_;
    }

    public void setMax_wastage_rt(double max_wastage_rt) {
        this.max_wastage_rt = max_wastage_rt;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setOrign_unit_amt(double orign_unit_amt) {
        this.orign_unit_amt = orign_unit_amt;
    }

    public String getCargo_owner_code() {
        return cargo_owner_code;
    }

    public String getCargo_owner_addr() {
        return cargo_owner_addr;
    }

    public String getCargo_owner_tel() {
        return cargo_owner_tel;
    }

    public String getCargo_owner_fax() {
        return cargo_owner_fax;
    }

    public String getAppoint_transport_flag() {
        return appoint_transport_flag;
    }

    public String getAppoint_transport_flag_() {
        return appoint_transport_flag_;
    }

    public void setBillingType(DictionaryItem di) {
        this.billing_type = di.getIdAsInt();
        this.billing_type_ = di.getText();
    }

    public boolean isProtocolTransport() {
        return 1 == wrtr_transport_flag;
    }

    public int getTrans_type() {
        return trans_type;
    }

    public String getCargo_src_type() {
        return cargo_src_type;
    }

    public void setBilling_type(int billing_type) {
        this.billing_type = billing_type;
    }

    public String getCarrier_length_type_() {
        return carrier_length_type_;
    }


    public String getCarrier_length_type() {
        return carrier_length_type;
    }

    public int getTransport_type() {
        return transport_type;
    }

    public String getTransportType() {
        return transport_type_;
    }

    public String getCarrier_model_type() {
        return carrier_model_type;
    }

    public String getCarrier_model_type_() {
        return carrier_model_type_;
    }

    public Source(String carrier_length_type) {
        this.carrier_length_type = carrier_length_type;
    }

    public String getCargo_owner_sname() {
        return cargo_owner_sname;
    }

    public String getMulti_transport_flag() {
        return multi_transport_flag;
    }

    public String getMultiTransportFlag() {
        return multi_transport_flag_;
    }

    public int getBilling_type() {
        return billing_type;
    }

    public String getBilling_type_() {
        return billing_type_;
    }

    public double getConfer_unit_amt() {
        return confer_unit_amt;
    }

    public String getCreate_ts() {
        return create_ts;
    }

    public String getLoading_end_tm() {
        return loading_end_tm;
    }

    public String getLoading_start_tm() {
        return loading_start_tm;
    }

    public String getLoading_start_dt() {
        return loading_start_dt;
    }

    public String getLoading_end_dt() {
        return loading_end_dt;
    }

    public String getCargo_code() {
        return cargo_code;
    }

    public String getCargo_uuid() {
        return cargo_uuid;
    }

    public String getStatus() {
        return status;
    }

    public String getStatus_() {
        return status_;
    }

    public String getMatch_status() {
        return match_status;
    }

    public String getRound_uuid() {
        return round_uuid;
    }

    public String getLoading_country() {
        return loading_country;
    }

    public String getUnload_country() {
        return unload_country;
    }

    public double getDeduct_tax_rt() {
        return deduct_tax_rt;
    }

    public String getDeductTaxRt(boolean withUnit) {
        return NumberUtil.format3f(withUnit ? deduct_tax_rt * 100 : deduct_tax_rt);
    }

    public String getDeductTaxRtWithUnit() {
        return getDeductTaxRt(true) + "%";
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setUnit_ct(double unit_ct) {
        this.unit_ct = unit_ct;
    }

    public void setVolumn(Source source) {
        this.length = source.length;
        this.width = source.width;
        this.height = source.height;
    }

    public String getLength() {
        return NumberUtil.format3f(length);
    }

    public String getWidth() {
        return NumberUtil.format3f(width);
    }

    public String getHeight() {
        return NumberUtil.format3f(height);
    }

    public String getVolume(String separator) {
        return getLength() + separator + getWidth() + separator + getHeight() + "米";
    }

    public void setChargeReferType(DictionaryItem di) {
        if (di != null) {
            this.charge_refer_type = di.getId();
            this.charge_refer_type_ = di.getText();
        }
    }

    public DictionaryItem getChargeRefTypeAsDI() {
        if (charge_refer_type != null && charge_refer_type_ != null) {
            return new DictionaryItem(charge_refer_type, charge_refer_type_);
        }
        return null;
    }

    //平台结算，自主结算
    public void setSettleType(DictionaryItem di) {
        if (di != null) {
            this.settle_type = di.getId();
            this.settle_type_ = di.getText();
        }
    }

    public DictionaryItem getSettleTypeAsDI() {
        if (settle_type_ != null && settle_type != null)
            return new DictionaryItem(settle_type, settle_type_);
        return null;
    }

    //现结，月结
    public void setSettleAuth(DictionaryItem di) {
        if (di != null) {
            this.settle_auth = di.getIdAsInt();
            this.settle_auth_ = di.getText();
        }
    }

    public DictionaryItem getSettleAuthAsDI() {
        if (settle_auth_ != null && settle_auth >= 0 && settle_auth <= 1)
            return new DictionaryItem(String.valueOf(settle_auth), settle_auth_);
        return null;
    }

    public void setInsuranceType(DictionaryItem di) {
        if (di != null) {
            this.insure_type = di.getIdAsInt();
            this.insure_type_ = di.getText();
        }
    }

    public DictionaryItem getInsuranceTypeAsDictionaryItem() {
        if (insure_type_ != null && insure_type >= 1 && insure_type <= 2) {
            return new DictionaryItem(String.valueOf(insure_type), insure_type_);
        }
        return null;
    }

    public List<AppointCarrier> getAppointTransportList() {
        return appointTransportList;
    }

    public void setAppointFlag(DictionaryItem di) {
        if (di != null) {
            this.appoint_transport_flag = di.getId();
            this.appoint_transport_flag_ = di.getText();
        }
    }

    public DictionaryItem getAppointFlagAsDI() {
        if (appoint_transport_flag != null && appoint_transport_flag_ != null)
            return new DictionaryItem(appoint_transport_flag, appoint_transport_flag_);
        return null;
    }

    //发货联系人
    public void setShipContact(BusinessContact contact) {
        if (contact != null) {
            loading_contacts_tax_code = contact.getTaxpayer_code();
            loading_contacts_name = contact.getContact_name();
        }
    }

    //收货联系人
    public void setReceiveContact(BusinessContact contact) {
        if (contact != null) {
            unload_contacts_tax_code = contact.getTaxpayer_code();
            unload_contacts_name = contact.getContact_name();
        }
    }

    public double getCargo_price() {
        return cargo_price;
    }

    public String getCargoPrice() {
        return NumberUtil.format2f(cargo_price);
    }

    public String getCargoPriceInThousands() {
        return NumberUtil.format2f(cargo_price / 10000) + "万元";
    }

    public void setDraweeInfo(Drawee draweeInfo) {
        if (draweeInfo != null) {
            drawee_name = draweeInfo.getInvoice_name();
            drawee_tax_code = draweeInfo.getInvoice_taxpayer();
            drawee_tel = draweeInfo.getInvoice_tel();
            drawee_addr = draweeInfo.getInvoice_address();
            drawee_bank_acct_no = draweeInfo.getInvoice_bank_code();
            drawee_open_bank_name = draweeInfo.getInvoice_bank();
        }
    }

    public Drawee getDrawee() {
        if (drawee_name != null) {
            return new Drawee(drawee_addr,
                    drawee_open_bank_name,
                    drawee_bank_acct_no,
                    null,
                    drawee_name,
                    drawee_tax_code, drawee_tel, null);
        }
        return null;
    }

    public void setPayerMemberCode(Payer payer) {
        if (payer != null) {
            payer_member_code = payer.getAssign_code();
        }
    }

    public Payer getPayer() {
        if (payerMemberInfo != null) {
            return new Payer(
                    payerMemberInfo.getAssign_code(),
                    payerMemberInfo.getBank_address(),
                    payerMemberInfo.getBank_code(),
                    payerMemberInfo.getBank_type(),
                    payerMemberInfo.getDelegate_name(),
                    payerMemberInfo.getDelegate_uuid(),
                    payerMemberInfo.getName());
        }
        return null;
    }

    public void setLoading_start_dt(String loading_start_dt) {
        this.loading_start_dt = loading_start_dt;
    }

    public void setLoading_end_dt(String loading_end_dt) {
        this.loading_end_dt = loading_end_dt;
    }

    public void setLoading_start_tm(String loading_start_tm) {
        this.loading_start_tm = loading_start_tm;
    }

    public void setLoading_end_tm(String loading_end_tm) {
        this.loading_end_tm = loading_end_tm;
    }

    public String getCargoPriceWithUnit() {
        return getCargoPrice() + "元";
    }

    public String getLoadingContact() {
        return loading_contacts;
    }

    public String getEncryptedLoadingContact() {
        if (getLoadingContact() == null || getLoadingContact().length() < 2)
            return getLoadingContact();
        return getLoadingContact().substring(0, 1) + "师傅";
    }

    public String getEncryptedUnLoadContact() {
        String s = getUContact();
        if (s == null || s.length() < 2)
            return s;
        return s.substring(0, 1) + "师傅";
    }

    public String getLoadingContactWithTel() {
        return String.format(Locale.CHINA, "%s(%s)", getLoadingContact(), getLoading_contacts_tel());
    }

    public String getUnloadContactWithTel() {
        return String.format(Locale.CHINA, "%s(%s)", getUContact(), getUnload_contacts_tel());
    }

    public String getEncryptedUnloadContactWithTel() {
        return String.format(Locale.CHINA, "%s(%s)", getEncryptedUnLoadContact(), getUnload_contacts_tel());
    }

    public String getEncryptedLoadingContactWithTel() {
        return String.format(Locale.CHINA, "%s(%s)", getEncryptedLoadingContact(), getLoading_contacts_tel());
    }

    public String getLoading_contacts_tel() {
        return loading_contacts_tel;
    }

    public String getTipsMsg() {
        if (wrtrTips != null)
            return wrtrTips.getTipsMsg();
        return "";
    }

    private String getUContact() {
        if (getUnload_contacts() != null)
            return getUnload_contacts();
        return getUnload_contacts_name();
    }

    public String getUnload_contacts() {
        return unload_contacts;
    }

    public String getUnload_contacts_name() {
        return unload_contacts_name;
    }

    public String getUnload_contacts_tel() {
        return unload_contacts_tel;
    }

    public String getUnits_() {
        return units_;
    }

    public String getUnits() {
        return units;
    }

    public DictionaryItem getUnitAsDI() {
        if (book_ref_type == 0) {
            return new DictionaryItem("吨", "00");
        } else {
            return new DictionaryItem(units, units_);
        }
    }

    public String getMeasure_type() {
        return measure_type_;
    }

    public String getMeasureType() {
        return measure_type;
    }

    public DictionaryItem getMeasureTypeAsDI() {
        return new DictionaryItem(measure_type, measure_type_);
    }

    /**
     * 运费单价
     *
     * @return
     */
    public double getTransport_unit_amt() {
        return transport_unit_amt;
    }

    //运费单价
    public final String getTransportUnitAmt() {
        return NumberUtil.format2f(getTransport_unit_amt());
    }

    //运费单价加上单位
    public String getTransportUnitAmtFee() {
        return getTransportUnitAmt() + getUnitForFee();
    }

    //自主交易参考单价
    public String getSelfTradeReferUnitPrice() {
        return getTransportUnitAmt() + getUnitForFee();
    }

    public String getSettle_type() {
        return settle_type_;
    }

    public String getSettle_auth_() {
        return settle_auth_;
    }

    public String getMax_wastage_rt(boolean withUnit) {
        return NumberUtil.format3f(withUnit ? max_wastage_rt * 1000 : max_wastage_rt);
    }

    public double getMax_wastage_rt() {
        return max_wastage_rt;
    }

    public String getMaxWastageRtWithUnit() {
        return getMax_wastage_rt(true) + "‰";
    }

    public String getSpecial_req() {
        if (StringUtil.empty(special_req)) {
            return "无";
        }
        return special_req;
    }

    public String getOSpecialReq() {
        return special_req;
    }

    public String getSpecialRequest() {
        return special_req;
    }

    public String getOrgin_cargon_kind_name() {
        return orgin_cargon_kind_name;
    }

    public String getLoading_province() {
        return loading_province;
    }

    public String getLoading_city() {
        return loading_city;
    }

    public String getUnload_province() {
        return unload_province;
    }

    public String getUnload_city() {
        return unload_city;
    }

    public int getRound_trans_type() {
        return round_trans_type;
    }

    public String getTotalLoadingUnloadingDays() {
        return stevedorage_days + "天";
    }

    public String getTransport_days() {
        return transport_days;
    }

    public String getTransPortDays() {
        return transport_days + "天";
    }

    public String getTransporterRequests() {
        if (carrier_model_type_ != null)
            return carrier_model_type_;
        switch (transport_type) {
            case 1://车辆
                if (car_model_type_ != null)
                    return car_model_type_;
                else
                    return "无";
            case 2://船舶
                if (ship_model_type_ == null)
                    return "无";
                return ship_model_type_;
        }
        return "";
    }

    public double getTransport_tax_rt() {
        return transport_tax_rt;
    }

    public String getTransportTaxRt(boolean withUnit) {
        return NumberUtil.format3f(withUnit ? transport_tax_rt * 100 : transport_tax_rt);
    }

    public String getTransportTaxRtWithUnit() {
        return getTransportTaxRt(true) + "%";
    }

    public String getLoadingStartDtInChineseFormat() {
        SimpleDate date = SimpleDate.parse(getLoading_start_dt());
        if (date == null)
            return null;
        return date.chineseFormat();
    }

    public String getLoadingEndDtInChineseFormat() {
        SimpleDate date = SimpleDate.parse(getLoading_end_dt());
        if (date == null)
            return null;
        return date.chineseFormat();
    }

    public String getLoadingPeriod() {
        if (cached_loading_period != null) {
            return cached_loading_period;
        }
        SimpleDate start = SimpleDate.parse(getLoading_start_dt());
        SimpleDate end = SimpleDate.parse(getLoading_end_dt());
        if (start == null || end == null)
            return null;

        cached_loading_period = start.chineseFormat(true, false) + "-" + end.chineseFormat(true, false);
        return cached_loading_period;
    }

    public String getLoadingPeriodWithTime() {
        SimpleDate start = SimpleDate.parse(getLoading_start_dt());
        SimpleDate end = SimpleDate.parse(getLoading_end_dt());
        if (start == null || end == null)
            return null;

        SimpleDate startTm = SimpleDate.parse(loading_start_tm);
        SimpleDate endTm = SimpleDate.parse(loading_end_tm);
        if (startTm == null || endTm == null)
            return null;

        return start.chineseFormatWithoutYear() + startTm.timeString(true, ":", false) + "-"
                + end.chineseFormatWithoutYear() + endTm.timeString(true, ":", false);
    }

    public boolean hasTimeString() {
        //竞价或者挂价(非自主交易)
        return trans_type == 1 || (trans_type == 2 && billing_type != 3);
    }

    //收费依据
    public String getChargeReferType() {
        return charge_refer_type_;
    }

    public String getBookRefType() {
        if (getBook_ref_type_() == null) {
            switch (getBook_ref_type()) {
                case 0:
                    return "重量";
                case 1:
                    return "数量";
            }
            return "类型未知";
        }
        return getBook_ref_type_();
    }

    public double getWeight() {
        return weight;
    }

    public final String getCargoWeight() {
        return NumberUtil.format3f(getWeight());
    }

    public String getCargoWeightWithUnit() {
        return getCargoWeight() + "吨";
    }

    public double getUnit_ct() {
        return unit_ct;
    }

    public String getCargoNumbers() {
        return NumberUtil.format3f(getUnit_ct());
    }

    public String getCargoNumbersWithUnit() {
        return getCargoNumbers() + (units_ == null ? "" : units_);
    }

    public double getRem_qt() {
        return rem_qt;
    }

    public double getCargoDesc() {
        switch (getBook_ref_type()) {
            case 0:
                return getWeight();
            case 1:
                return getUnit_ct();
        }
        return 0;
    }

    public String getCargoDescWithUnit() {
        switch (getBook_ref_type()) {
            case 0:
                return getCargoWeightWithUnit();
            case 1:
                return getCargoNumbersWithUnit();
        }
        return "";
    }

    public String getRemainCargoDescWithUnit() {
        return NumberUtil.format3f(rem_qt) + getCargoUnit();
    }

    public String getRemainCargoDescWithoutUnit() {
        return NumberUtil.format3f(rem_qt);
    }

    public String getCargoNumberAndWeight(char separator) {
        return getCargoNumbers() + separator + getCargoWeight();
    }

    public String getCargoNumberAndWeightWithUnit(char separator) {
        return getCargoNumbersWithUnit() + separator + getCargoWeightWithUnit();
    }

    public String getCarrierAndLoadingPeriod() {
        return getLoadingPeriod();
    }

    public int getBook_ref_type() {
        return book_ref_type;
    }

    public String getBook_ref_type_() {
        return book_ref_type_;
    }

    public String getLoadingAddress() {
        Address address = getStartAddress();
        if (address != null)
            return address.buildAddress(' ', true);
        return "";
    }

    public String getUnLoadingAddress() {
        Address address = getEndAddress();
        if (address != null)
            return address.buildAddress(' ', true);
        return "";
    }

    /***
     * 起始地
     */
    public Address getStartAddress() {
        //国联数据可能只有省
        if (!TextUtils.isEmpty(loading_province)) {
            return new Address(loading_province, loading_city, loading_country, loading_addr);
        }
        return null;
    }

    /***
     * 目的地
     */
    public Address getEndAddress() {
        //国联数据可能只有省
        if (!TextUtils.isEmpty(unload_province)) {
            return new Address(unload_province, unload_city, unload_country, unload_addr);
        }
        return null;
    }

    public String getCargoUnit() {
        if (getBook_ref_type() == 0)
            return "吨";
        else
            return units_;
    }

    public String getTransportFeeForCargoOwner() {
        double ct = getWeight();
        if (getBook_ref_type() == 1)
            ct = getUnit_ct();
        return NumberUtil.format2f(ct * total_unit_amt);
    }

    public String getTotalUnitAmt() {
        return NumberUtil.format2f(total_unit_amt);
    }

    //当前单位对应的可填写字段是否能够输入小数
    public boolean withFraction() {//只有单位是吨或者立方的时候才能输入小数，其他只能输入整数
        return "00".equals(units) || "02".equals(units);
    }

    public String getUnitForFee() {
        return "元/" + getCargoUnit();
    }

    public String getTimePeriod(String separator) {
        SimpleDate startTm = SimpleDate.parse(loading_start_tm);
        SimpleDate endTm = SimpleDate.parse(loading_end_tm);
        if (startTm == null || endTm == null)
            return null;

        return startTm.timeString(true, ":") + separator + endTm.timeString(true, ":");
    }

    //获取可用的时间字段
    public String getAvailableTime() {
        return create_ts;
    }

    //货源的承运车辆
    public String getPlate() {
        return null;
    }

    public String getCargoFileId() {
        if ("".equals(cargo_file_id) || "null".equals(cargo_file_id))
            return null;

        return cargo_file_id;
    }

    public void setCargo_file_id(String cargo_file_id) {
        this.cargo_file_id = cargo_file_id;
    }

    public boolean shouldDisplayDeductRt() {
        return deduction_tax_rate != null && deduction_tax_rate.size() > 0 && transport_tax_rt != 0;
    }

    public List<DeductRt> getDeduction_tax_rate() {
        return deduction_tax_rate;
    }

    public String getOrignUnitAmt() {
        return NumberUtil.format2f(orign_unit_amt);
    }

    public double getOrign_unit_amt() {
        return orign_unit_amt;
    }

    public String getOrignUnitAmtWithUnit() {
        return NumberUtil.format2f(orign_unit_amt) + getUnitForFee();
    }

    public void setLoadingInfo(CargoContact info) {
        if (info != null) {
            loading_province = info.getProvince();
            loading_city = info.getCity();
            loading_country = info.getCountry();
            loading_addr = info.getAddress();
            if (billing_type != 3) {
                loading_contacts = info.getContactName();
                loading_contacts_tel = info.getContactTel();
                loading_harbor_depth = info.getWaterDepth();
            }
        }
    }

    public void setLoading_contacts(String loading_contacts) {
        this.loading_contacts = loading_contacts;
    }

    public void setLoading_contacts_tel(String loading_contacts_tel) {
        this.loading_contacts_tel = loading_contacts_tel;
    }

    public CargoContact getLoadingInfo() {
        return new CargoContact(loading_contacts, loading_contacts_tel, loading_province, loading_city, loading_country, loading_addr, loading_harbor_depth);
    }

    public void setUnLoadInfo(CargoContact info) {
        if (info != null) {
            unload_province = info.getProvince();
            unload_city = info.getCity();
            unload_country = info.getCountry();
            unload_addr = info.getAddress();
            unload_contacts = info.getContactName();
            unload_contacts_tel = info.getContactTel();
            unload_harbor_depth = info.getWaterDepth();
        }
    }

    public CargoContact getUnLoadInfo() {
        return new CargoContact(unload_contacts, unload_contacts_tel, unload_province, unload_city, unload_country, unload_addr, unload_harbor_depth);
    }

    public void setMultiTransportFlag(DictionaryItem di) {
        if (di != null) {
            this.multi_transport_flag = di.getId();
            this.multi_transport_flag_ = di.getText();
        }
    }

    public DictionaryItem getMultiTransportFlagAsDI() {
        if (multi_transport_flag != null && multi_transport_flag_ != null) {
            return new DictionaryItem(multi_transport_flag, multi_transport_flag_);
        }
        return null;
    }

    public void setTransportType(DictionaryItem di) {
        if (di != null) {
            this.transport_type = di.getIdAsInt();
            this.transport_type_ = di.getText();
        }
    }

    public DictionaryItem getTransportTypeAsDI() {
        if (transport_type_ != null && transport_type >= 1 && transport_type <= 2) {
            return new DictionaryItem(String.valueOf(transport_type), transport_type_);
        }
        return null;
    }

    public void setCarrierModel(DictionaryItem di) {
        if (di != null) {
            this.carrier_model_type = di.getId();
            this.carrier_model_type_ = di.getText();
        }
    }

    public DictionaryItem getCarrierModelAsDI() {
        if (carrier_model_type_ != null && carrier_model_type != null) {
            return new DictionaryItem(carrier_model_type, carrier_model_type_);
        }
        return null;
    }

    public void setCarrierLength(DictionaryItem di) {
        if (di != null) {
            this.carrier_length_type = di.getId();
            this.carrier_length_type_ = di.getText();
        }
    }

    public DictionaryItem getCarrierLengthAsDI() {
        if (carrier_length_type != null && carrier_length_type_ != null) {
            return new DictionaryItem(carrier_length_type, carrier_length_type_);
        }
        return null;
    }

    public void setRoundTransType(DictionaryItem di) {
        if (di != null) {
            this.round_trans_type = di.getIdAsInt();
            this.round_trans_type_ = di.getText();
        }
    }

    public DictionaryItem getTransTypeAsDI() {
        if (trans_type_ != null && trans_type >= 0 && trans_type <= 2) {
            return new DictionaryItem(String.valueOf(trans_type), trans_type_);
        }
        return null;
    }

    public void setTrans_type(int trans_type) {
        this.trans_type = trans_type;
    }

    public void setTransport_days(String transport_days) {
        this.transport_days = transport_days;
    }

    public void setStevedorage_days(int stevedorage_days) {
        this.stevedorage_days = stevedorage_days;
    }

    public String getStevedorage_days() {
        return String.valueOf(stevedorage_days);
    }

    public void setSpecialReq(String special_req) {
        this.special_req = special_req;
    }

    public boolean get_disableRow_() {
        return _disableRow_;
    }

    public WrtrInfo getWrtrTips() {
        return wrtrTips;
    }

    public static class DeductRt implements Parcelable, IItemShow {
        String id;
        String text;

        public String getId() {
            return id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.text);
        }

        public DeductRt() {
        }

        protected DeductRt(Parcel in) {
            this.id = in.readString();
            this.text = in.readString();
        }

        public static final Creator<DeductRt> CREATOR = new Creator<DeductRt>() {
            @Override
            public DeductRt createFromParcel(Parcel source) {
                return new DeductRt(source);
            }

            @Override
            public DeductRt[] newArray(int size) {
                return new DeductRt[size];
            }
        };

        @Override
        public String getValue() {
            return text;
        }
    }

    public Source() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cargo_code);
        dest.writeString(this.cargo_uuid);
        dest.writeString(this.round_uuid);
        dest.writeByte(this._disableRow_ ? (byte) 1 : (byte) 0);
        dest.writeInt(this.round_trans_type);
        dest.writeString(this.round_trans_type_);
        dest.writeString(this.orgin_cargon_kind_name);
        dest.writeDouble(this.unit_ct);
        dest.writeString(this.units);
        dest.writeString(this.units_);
        dest.writeDouble(this.weight);
        dest.writeString(this.loading_start_dt);
        dest.writeString(this.loading_end_dt);
        dest.writeString(this.loading_start_tm);
        dest.writeString(this.loading_end_tm);
        dest.writeInt(this.stevedorage_days);
        dest.writeString(this.loading_province);
        dest.writeString(this.loading_city);
        dest.writeString(this.loading_country);
        dest.writeString(this.loading_addr);
        dest.writeString(this.loading_pwd);
        dest.writeString(this.loading_contacts);
        dest.writeString(this.loading_contacts_tel);
        dest.writeString(this.loading_contacts_name);
        dest.writeString(this.loading_contact_info_);
        dest.writeString(this.loading_longitude_top);
        dest.writeString(this.loading_latitude_top);
        dest.writeString(this.loading_latitude_bottom);
        dest.writeString(this.loading_longitude_bottom);
        dest.writeString(this.loading_harbor_depth);
        dest.writeString(this.loading_latitude);
        dest.writeString(this.loading_longitude);
        dest.writeString(this.loading_contacts_tax_code);
        dest.writeString(this.unload_province);
        dest.writeString(this.unload_city);
        dest.writeString(this.unload_country);
        dest.writeString(this.unload_addr);
        dest.writeString(this.unload_contacts_tel);
        dest.writeString(this.unload_contacts);
        dest.writeString(this.unload_contacts_name);
        dest.writeString(this.unload_contacts_tax_code);
        dest.writeString(this.unload_contact_info_);
        dest.writeString(this.unload_pwd);
        dest.writeString(this.unload_latitude);
        dest.writeString(this.unload_longitude);
        dest.writeString(this.unload_longitude_top);
        dest.writeString(this.unload_latitude_top);
        dest.writeString(this.unload_longitude_bottom);
        dest.writeString(this.unload_latitude_bottom);
        dest.writeString(this.unload_harbor_depth);
        dest.writeDouble(this.length);
        dest.writeDouble(this.width);
        dest.writeDouble(this.height);
        dest.writeDouble(this.cargo_price);
        dest.writeString(this.measure_type);
        dest.writeString(this.measure_type_);
        dest.writeInt(this.book_ref_type);
        dest.writeString(this.book_ref_type_);
        dest.writeString(this.settle_type);
        dest.writeString(this.settle_type_);
        dest.writeString(this.carrier_model_type);
        dest.writeString(this.carrier_model_type_);
        dest.writeString(this.carrier_length_type);
        dest.writeString(this.carrier_length_type_);
        dest.writeString(this.car_model_type);
        dest.writeString(this.car_model_type_);
        dest.writeString(this.truck_model_type);
        dest.writeString(this.truck_length_type);
        dest.writeString(this.ship_model_type);
        dest.writeString(this.ship_model_type_);
        dest.writeString(this.transport_days);
        dest.writeDouble(this.max_wastage_rt);
        dest.writeString(this.special_req);
        dest.writeString(this.charge_refer_type);
        dest.writeString(this.charge_refer_type_);
        dest.writeDouble(this.transport_unit_amt);
        dest.writeDouble(this.rem_qt);
        dest.writeDouble(this.reserve_qt);
        dest.writeDouble(this.accept_qt);
        dest.writeDouble(this.orign_unit_amt);
        dest.writeDouble(this.confer_unit_amt);
        dest.writeDouble(this.transport_unit_price);
        dest.writeDouble(this.other_unit_amt);
        dest.writeString(this.status);
        dest.writeString(this.status_);
        dest.writeString(this.cargo_status);
        dest.writeString(this.cargo_owner_sname);
        dest.writeString(this.cargo_agent_sname);
        dest.writeString(this.cargo_owner_tel);
        dest.writeString(this.cargo_owner_fax);
        dest.writeString(this.cargo_owner_addr);
        dest.writeString(this.multi_transport_flag);
        dest.writeString(this.multi_transport_flag_);
        dest.writeInt(this.wrtr_transport_flag);
        dest.writeDouble(this.per_min_qt);
        dest.writeDouble(this.transport_tax_rt);
        dest.writeDouble(this.wrtr_serv_unit_amt);
        dest.writeParcelable(this.wrtrTips, flags);
        dest.writeDouble(this.unit_weight);
        dest.writeInt(this.trans_type);
        dest.writeString(this.trans_type_);
        dest.writeInt(this.settle_auth);
        dest.writeString(this.settle_auth_);
        dest.writeDouble(this.last_min_rem_qt);
        dest.writeInt(this.insure_type);
        dest.writeString(this.insure_type_);
        dest.writeDouble(this.deduct_tax_rt);
        dest.writeString(this.confer_cargo_price);
        dest.writeString(this.premium_unit_amt);
        dest.writeString(this.create_ts);
        dest.writeString(this.appr_dttm);
        dest.writeInt(this.billing_type);
        dest.writeString(this.billing_type_);
        dest.writeString(this.cargo_src_type);
        dest.writeString(this.cargo_spare_tel);
        dest.writeString(this.cargo_agent_code);
        dest.writeString(this.cargo_owner_code);
        dest.writeDouble(this.total_unit_amt);
        dest.writeString(this.payer_member_code);
        dest.writeString(this.drawee_open_bank_name);
        dest.writeString(this.drawee_tax_code);
        dest.writeString(this.drawee_name);
        dest.writeString(this.drawee_addr);
        dest.writeString(this.drawee_info_);
        dest.writeString(this.drawee_bank_acct_no);
        dest.writeString(this.drawee_tel);
        dest.writeString(this.estimate_unit_amt);
        dest.writeString(this.agrt_price_flag);
        dest.writeString(this.agrt_price_flag_);
        dest.writeString(this.agrt_price_uuid);
        dest.writeString(this.match_status);
        dest.writeString(this.match_status_);
        dest.writeString(this.bill_req);
        dest.writeString(this.last_mod_dttm);
        dest.writeInt(this.transport_type);
        dest.writeString(this.transport_type_);
        dest.writeString(this.appoint_transport_flag);
        dest.writeString(this.appoint_transport_flag_);
        dest.writeString(this.insurance_rt);
        dest.writeString(this.revoke_qt);
        dest.writeString(this.cargo_file_id);
        dest.writeString(this.cargo_owner_sub_code);
        dest.writeString(this.trans_round_ct);
        dest.writeString(this.accepter_code);
        dest.writeString(this.match_phase);
        dest.writeString(this.trans_fee_rt);
        dest.writeString(this.cargo_kind_code);
        dest.writeString(this.appr_code);
        dest.writeString(this.cargo_owner_mgt_unit_code);
        dest.writeString(this.profit_amt);
        dest.writeTypedList(this.deduction_tax_rate);
        dest.writeTypedList(this.appointTransportList);
        dest.writeParcelable(this.payerMemberInfo, flags);
        dest.writeString(this.version);
    }

    protected Source(Parcel in) {
        this.cargo_code = in.readString();
        this.cargo_uuid = in.readString();
        this.round_uuid = in.readString();
        this._disableRow_ = in.readByte() != 0;
        this.round_trans_type = in.readInt();
        this.round_trans_type_ = in.readString();
        this.orgin_cargon_kind_name = in.readString();
        this.unit_ct = in.readDouble();
        this.units = in.readString();
        this.units_ = in.readString();
        this.weight = in.readDouble();
        this.loading_start_dt = in.readString();
        this.loading_end_dt = in.readString();
        this.loading_start_tm = in.readString();
        this.loading_end_tm = in.readString();
        this.stevedorage_days = in.readInt();
        this.loading_province = in.readString();
        this.loading_city = in.readString();
        this.loading_country = in.readString();
        this.loading_addr = in.readString();
        this.loading_pwd = in.readString();
        this.loading_contacts = in.readString();
        this.loading_contacts_tel = in.readString();
        this.loading_contacts_name = in.readString();
        this.loading_contact_info_ = in.readString();
        this.loading_longitude_top = in.readString();
        this.loading_latitude_top = in.readString();
        this.loading_latitude_bottom = in.readString();
        this.loading_longitude_bottom = in.readString();
        this.loading_harbor_depth = in.readString();
        this.loading_latitude = in.readString();
        this.loading_longitude = in.readString();
        this.loading_contacts_tax_code = in.readString();
        this.unload_province = in.readString();
        this.unload_city = in.readString();
        this.unload_country = in.readString();
        this.unload_addr = in.readString();
        this.unload_contacts_tel = in.readString();
        this.unload_contacts = in.readString();
        this.unload_contacts_name = in.readString();
        this.unload_contacts_tax_code = in.readString();
        this.unload_contact_info_ = in.readString();
        this.unload_pwd = in.readString();
        this.unload_latitude = in.readString();
        this.unload_longitude = in.readString();
        this.unload_longitude_top = in.readString();
        this.unload_latitude_top = in.readString();
        this.unload_longitude_bottom = in.readString();
        this.unload_latitude_bottom = in.readString();
        this.unload_harbor_depth = in.readString();
        this.length = in.readDouble();
        this.width = in.readDouble();
        this.height = in.readDouble();
        this.cargo_price = in.readDouble();
        this.measure_type = in.readString();
        this.measure_type_ = in.readString();
        this.book_ref_type = in.readInt();
        this.book_ref_type_ = in.readString();
        this.settle_type = in.readString();
        this.settle_type_ = in.readString();
        this.carrier_model_type = in.readString();
        this.carrier_model_type_ = in.readString();
        this.carrier_length_type = in.readString();
        this.carrier_length_type_ = in.readString();
        this.car_model_type = in.readString();
        this.car_model_type_ = in.readString();
        this.truck_model_type = in.readString();
        this.truck_length_type = in.readString();
        this.ship_model_type = in.readString();
        this.ship_model_type_ = in.readString();
        this.transport_days = in.readString();
        this.max_wastage_rt = in.readDouble();
        this.special_req = in.readString();
        this.charge_refer_type = in.readString();
        this.charge_refer_type_ = in.readString();
        this.transport_unit_amt = in.readDouble();
        this.rem_qt = in.readDouble();
        this.reserve_qt = in.readDouble();
        this.accept_qt = in.readDouble();
        this.orign_unit_amt = in.readDouble();
        this.confer_unit_amt = in.readDouble();
        this.transport_unit_price = in.readDouble();
        this.other_unit_amt = in.readDouble();
        this.status = in.readString();
        this.status_ = in.readString();
        this.cargo_status = in.readString();
        this.cargo_owner_sname = in.readString();
        this.cargo_agent_sname = in.readString();
        this.cargo_owner_tel = in.readString();
        this.cargo_owner_fax = in.readString();
        this.cargo_owner_addr = in.readString();
        this.multi_transport_flag = in.readString();
        this.multi_transport_flag_ = in.readString();
        this.wrtr_transport_flag = in.readInt();
        this.per_min_qt = in.readDouble();
        this.transport_tax_rt = in.readDouble();
        this.wrtr_serv_unit_amt = in.readDouble();
        this.wrtrTips = in.readParcelable(WrtrInfo.class.getClassLoader());
        this.unit_weight = in.readDouble();
        this.trans_type = in.readInt();
        this.trans_type_ = in.readString();
        this.settle_auth = in.readInt();
        this.settle_auth_ = in.readString();
        this.last_min_rem_qt = in.readDouble();
        this.insure_type = in.readInt();
        this.insure_type_ = in.readString();
        this.deduct_tax_rt = in.readDouble();
        this.confer_cargo_price = in.readString();
        this.premium_unit_amt = in.readString();
        this.create_ts = in.readString();
        this.appr_dttm = in.readString();
        this.billing_type = in.readInt();
        this.billing_type_ = in.readString();
        this.cargo_src_type = in.readString();
        this.cargo_spare_tel = in.readString();
        this.cargo_agent_code = in.readString();
        this.cargo_owner_code = in.readString();
        this.total_unit_amt = in.readDouble();
        this.payer_member_code = in.readString();
        this.drawee_open_bank_name = in.readString();
        this.drawee_tax_code = in.readString();
        this.drawee_name = in.readString();
        this.drawee_addr = in.readString();
        this.drawee_info_ = in.readString();
        this.drawee_bank_acct_no = in.readString();
        this.drawee_tel = in.readString();
        this.estimate_unit_amt = in.readString();
        this.agrt_price_flag = in.readString();
        this.agrt_price_flag_ = in.readString();
        this.agrt_price_uuid = in.readString();
        this.match_status = in.readString();
        this.match_status_ = in.readString();
        this.bill_req = in.readString();
        this.last_mod_dttm = in.readString();
        this.transport_type = in.readInt();
        this.transport_type_ = in.readString();
        this.appoint_transport_flag = in.readString();
        this.appoint_transport_flag_ = in.readString();
        this.insurance_rt = in.readString();
        this.revoke_qt = in.readString();
        this.cargo_file_id = in.readString();
        this.cargo_owner_sub_code = in.readString();
        this.trans_round_ct = in.readString();
        this.accepter_code = in.readString();
        this.match_phase = in.readString();
        this.trans_fee_rt = in.readString();
        this.cargo_kind_code = in.readString();
        this.appr_code = in.readString();
        this.cargo_owner_mgt_unit_code = in.readString();
        this.profit_amt = in.readString();
        this.deduction_tax_rate = in.createTypedArrayList(DeductRt.CREATOR);
        this.appointTransportList = in.createTypedArrayList(AppointCarrier.CREATOR);
        this.payerMemberInfo = in.readParcelable(PayerMemberInfo.class.getClassLoader());
        this.version = in.readString();
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel source) {
            return new Source(source);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };
}
