package com.hletong.hyc.model;

/**
 * Created by dongdaqing on 2017/8/9.
 * 兜底运输信息
 */

public class ProtocolTransport {
    private String bookUnitAmt;//摘牌订单单价
    private String wrtrServUnitAmt;//兜底现场作业服务费单价
    private String agrtUnitAmt;//长期协议价单价
    private String units_;//单位
    private String wrtrServRecvName;//服务费收取人名称
    private String tipsMsg;//提示信息

    public String getTipsMsg() {
        return tipsMsg;
    }
}
