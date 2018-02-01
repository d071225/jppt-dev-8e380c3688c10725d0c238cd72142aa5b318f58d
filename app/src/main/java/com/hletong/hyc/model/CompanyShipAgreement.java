package com.hletong.hyc.model;

import com.hletong.hyc.util.Pages;

/**
 * Created by dongdaqing on 2017/7/13.
 */

public class CompanyShipAgreement extends Agreement {
    public String mUserName;//船舶会员名称（管理账号）
    public String mCompLegal;//法定代表人
    public String mPhone;//电话号码
    public String mAddress;//联系地址
    public String mFax;//传真
    public String mZipCode;//邮政编码
    public String mMail;//邮箱
    public String mContact;//业务联系人
    public String mContactMobile;//业务联系人手机
    public String mCompName;//船舶公司名称
    public String regContractYear;//签订入会协议年
    public String regContractMonth;//签订入会协议月
    public String regContractDate;//签订入会协议日
    public String mCompCharge;//船舶会员负责人
    public String mChargeIdCode;//船舶负责人身份证号（支持多辆船舶）
    public String mChargeMobile;//船舶会员负责人手机号
    public String mChargePhone;//船舶会员负责人电话
    public String mChargeMail;//船舶会员负责人邮箱
    public String mChargeFax;//船舶会员负责人传真
    public String shipName;//船舶名称（支持多辆船舶），如shipName=1&shipName=2
    public String mCharge;//船舶负责人名称（支持多辆船舶）
    public String mChargeAddress;//船舶会员负责人联系地址
    public String mChargeZipCode;//船舶会员负责人邮政编码
    public String shipType;//船舶类型（支持多辆船舶）
    public String shipCert;//船舶国籍证书初次登记号码（支持多辆船舶）
    public String shipMaxLoad;//总吨（支持多辆船舶）
    public String shipWeight;//净吨（支持多辆船舶）
    public String shipCarry;//载重吨（支持多辆船舶）
    public String shipCaptain;//船长（支持多辆船舶）
    public String shipHatchNum;//舱口数（支持多辆船舶）
    public String shipHatchNo;//舱口号（支持多辆船舶）
    public String shipHatchLength;//舱口长（支持多辆船舶）
    public String shipHatchWidth;//舱口宽（支持多辆船舶）
    public String shipHatchBLength;//舱底长（支持多辆船舶）
    public String shipHatchBWidth;//舱底宽（支持多辆船舶）
    public String shipDepth;//型深（支持多辆船舶）
    public String shipWidth;//型宽（支持多辆船舶）
    public String shipMaxDepth;//满载吃水（支持多辆船舶）
    public String shipHeight;//水上高度（支持多辆船舶）
    public String shipCrane;//船吊（支持多辆船舶）
    public String shipCDate;//建造日期（支持多辆船舶）
    public String shipMobile;//手机号码（支持多辆船舶）
    public String mContactYear;//运输合同签订年
    public String mContactMonth;//运输合同签订月
    public String mContactDate;//运输合同签订日
    public String shipBankName;//船舶结算银行卡用户姓名（支持多辆船舶）
    public String shipBankType;//船舶结算银行类型（支持多辆船舶）
    public String shipBankNo;//船舶结算银行账号（支持多辆船舶）
    public String scContractYear;//船舶结算签约年（支持多辆船舶）
    public String scContractMonth;//船舶结算签约月（支持多辆船舶）
    public String scContractDate;//船舶结算签约日（支持多辆船舶）

    @Override
    protected String getUrl() {
        return Pages.COMPANY_SHIP_REGISTER_CONTRACT_COMPANY;
    }
}
