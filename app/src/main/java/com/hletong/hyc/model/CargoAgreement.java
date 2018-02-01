package com.hletong.hyc.model;

import com.hletong.hyc.util.Pages;

/**
 * Created by dongdaqing on 2017/7/13.
 */

public class CargoAgreement extends Agreement {
    public String agent;//授权代理（市场部审核时填写）
    public String contractYear;//入会合同签订年
    public String contractMonth;//入会合同签订月
    public String contractDate;//入会合同签订日
    public String mBank;//结算银行类型，如建设银行
    public String mBankNo;//结算银行账号
    public String scContractYear;//结算证明签约年
    public String scContractMonth;//结算证明签约月
    public String scContractDate;//结算证明签约日

    @Override
    protected String getUrl() {
        return Pages.CARGO_CONTRACT;
    }
}
