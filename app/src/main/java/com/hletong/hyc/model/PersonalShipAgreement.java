package com.hletong.hyc.model;

import com.hletong.hyc.R;
import com.hletong.hyc.util.LinkView;
import com.hletong.hyc.util.Pages;

/**
 * Created by dongdaqing on 2017/7/13.
 */

public class PersonalShipAgreement extends Agreement {
    public String contractYear;//入会协议签约年
    public String contractMonth;//入会协议签约月
    public String contractDate;//入会协议签约日
    @LinkView({R.id.et_shipNumber, R.id.cv_ship})
    public String shipName;//船名（支持多辆船舶），如shipName=1&shipName=2
    @LinkView({R.id.et_shipOwner, R.id.cv_shipOwner})
    public String mCharge;//船舶所有人

    public String shipType;//船舶（支持多辆船舶）
    public String shipCert;//国籍证书初次登记号码（支持多辆船舶）
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
    public String shipCrane;//船吊个数（支持多辆船舶）
    public String shipCDate;//建造日期（支持多辆船舶）
    public String shipMobile;//手机号码（支持多辆船舶）
    public String shipBankName;//银行卡对应用户名（支持多辆船舶）
    public String shipBank;//银行类型（支持多辆船舶）
    public String shipBankNo;//银行卡号（支持多辆船舶）
    public String shipScYear;//结算证明签约年（支持多辆船舶）
    public String shipScMonth;//结算证明签约月（支持多辆船舶）
    public String shipScDate;//结算证明签约日（支持多辆船舶）

    @Override
    protected String getUrl() {
        return Pages.PERSONAL_SHIP_REGISTER_CONTRACT;
    }
}
