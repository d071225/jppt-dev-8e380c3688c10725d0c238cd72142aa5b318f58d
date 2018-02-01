package com.hletong.hyc.model;

import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/5/10.
 * 自主交易拨打电话信息统计用到的字段
 */

public class CallInfo {
    //SELF_TRADE_CONFIRM_CARRIER("10001000", "自主交易-车船方确认拨打电话", "bizUuid 为承运交易流水号"),
    //SELF_TRADE_CONFIRM_CARGO("10001001", "自主交易-货方确认拨打电话", "bizUuid 为承运交易流水号"),
    //SELF_TRADE_YJ_CARRIER_QUOTE("10002000", "自主交易议价-车船报价拨打电话", "bizUuid 为报价流水号"),
    //SELF_TRADE_YJ_CARRIER_QUOTE_MOD("10002001", "自主交易议价-车船修改报价拨打电话", "bizUuid 为报价流水号"),
    //SELF_TRADE_YJ_CARGO_CALL("10002002", "自主交易议价-货方议价拨打电话", "bizUuid 为报价流水号"),
    //SELF_TRADE_YJ_CARRIER_BOOK("10002003", "自主交易议价-车船报价摘牌拨打电话", "bizUuid 为报价流水号"),
    //SELF_TRADE_DHYJ_CARRIER("10003000", "自主交易电话议价-车船拨打电话", "bizUuid 为货源流水号"),
    //SELF_TRADE_DHYJ_CARGO("100030001", "自主交易电话议价-货方拨打电话", "bizUuid 为货源流水号"),

    private String calledMemberCode;// 被呼叫会员编号
    private String calledMemberSname;// 被呼叫会员名
    private String calledTel;//被呼叫电话
    private String callEventSrc;//主呼叫事件编号
    private String bizUuid;//主呼叫业务流水号
    private String callMemberCode;// 主呼叫方编号
    private String callMemberSname;// 主呼叫方会员名

    private String url;

    private CallInfo() {
        LoginInfo loginInfo = LoginInfo.getLoginInfo();
        if (loginInfo != null) {
            this.callMemberCode = loginInfo.getMember_code();
            this.callMemberSname = loginInfo.getUser_name();
        }
    }

    //承运方和货方的自主交易确认操作
    public static CallInfo CONFIRM(String contact, String memberCode, String tel, String tradeUuid) {
        if (AppTypeConfig.isCargo()) {
            return INSTANCE(contact, memberCode, tel, tradeUuid, BizUuid.SELF_TRADE_CONFIRM_CARGO.code);
        } else {
            return INSTANCE(contact, memberCode, tel, tradeUuid, BizUuid.SELF_TRADE_CONFIRM_CARRIER.code);
        }
    }

    //车船未提交报价信息之前的操作
    public static CallInfo QUOTE(Source source) {
        return INSTANCE(source.getLoadingContact(), source.getCargo_owner_code(), source.getLoading_contacts_tel(), source.getCargo_uuid(), BizUuid.SELF_TRADE_YJ_CARRIER_QUOTE.code);
    }

    //车船摘自主交易货源(非议价)
    public static CallInfo BOOK(Source source) {
        return INSTANCE(source.getLoadingContact(), source.getCargo_owner_code(), source.getLoading_contacts_tel(), source.getCargo_uuid(), BizUuid.SELF_TRADE_CARRIER_BOOK.code);
    }

    //议价成功之后车船摘牌
    public static CallInfo QUOTE_BOOK(String contact, String memberCode, String tel, String bizUuid) {
        return INSTANCE(contact, memberCode, tel, bizUuid, BizUuid.SELF_TRADE_YJ_CARRIER_BOOK.code);
    }

    //承运方提交报价信息之后 承运方和货方的操作
    public static CallInfo QUOTE_PROGRESS(String contact, String memberCode, String tel, String bizUuid) {
        if (AppTypeConfig.isCargo()) {
            //货方拨打电话议价
            return INSTANCE(contact, memberCode, tel, bizUuid, BizUuid.SELF_TRADE_YJ_CARGO_CALL.code);
        } else
            //车船修改报价-拨打电话
            return INSTANCE(contact, memberCode, tel, bizUuid, BizUuid.SELF_TRADE_YJ_CARRIER_QUOTE_MOD.code);
    }

    //国联议价
    public static CallInfo QUOTE_WITH_PHONE(String contact, String memberCode, String tel, String bizUuid) {
        if (AppTypeConfig.isCargo()) {
            //货方拨打电话议价
            return INSTANCE(contact, memberCode, tel, bizUuid, BizUuid.SELF_TRADE_DHYJ_CARGO.code);
        } else
            //车船修改报价-拨打电话
            return INSTANCE(contact, memberCode, tel, bizUuid, BizUuid.SELF_TRADE_DHYJ_CARRIER.code);
    }

    private static CallInfo INSTANCE(String contact, String memberCode, String tel, String bizUuid, String code) {
        CallInfo callInfo = new CallInfo();
        callInfo.calledMemberCode = memberCode;
        callInfo.calledMemberSname = contact;
        callInfo.calledTel = tel;
        callInfo.callEventSrc = code;
        callInfo.bizUuid = bizUuid;
        callInfo.url = Constant.getUrl(Constant.PHONE_TRACK);
        return callInfo;
    }

    private ParamsHelper getParams() {
        return new ParamsHelper()
                .put("calledMemberCode", calledMemberCode)//被呼叫会员编号
                .put("calledMemberSname", calledMemberSname)//被呼叫会员名
                .put("calledTel", calledTel)//被呼叫电话
                .put("callEventSrc", callEventSrc)//主呼叫事件编号
                .put("bizUuid", bizUuid)//主呼叫业务流水号
                .put("callMemberCode", callMemberCode)//主呼叫方编号
                .put("callMemberSname", callMemberSname); // 主呼叫方会员名
    }

//    public ItemRequestValue<CommonResult> getRequestValue(IBaseView baseView) {
//        return new ItemRequestValue<CommonResult>(baseView.hashCode(), url, getParams()) {
//        };
//    }

    public OkRequest getRequest(IBaseView baseView) {
        return EasyOkHttp.get(url).tag(baseView.hashCode()).params(getParams().getMaps()).build();
    }

    public String getCalledTel() {
        return calledTel;
    }

    public enum BizUuid {
        SELF_TRADE_CONFIRM_CARRIER("10001000", "自主交易-车船方确认拨打电话", "bizUuid 为承运交易流水号"),
        SELF_TRADE_CONFIRM_CARGO("10001001", "自主交易-货方确认拨打电话", "bizUuid 为承运交易流水号"),
        SELF_TRADE_CARRIER_BOOK("10001002", "自主交易-车船摘牌拨打电话", "bizUuid 为货源流水号"),
        SELF_TRADE_YJ_CARRIER_QUOTE("10002000", "自主交易议价-车船报价拨打电话", "bizUuid 为报价流水号"),
        SELF_TRADE_YJ_CARRIER_QUOTE_MOD("10002001", "自主交易议价-车船修改报价拨打电话", "bizUuid 为报价流水号"),
        SELF_TRADE_YJ_CARGO_CALL("10002002", "自主交易议价-货方议价拨打电话", "bizUuid 为报价流水号"),
        SELF_TRADE_YJ_CARRIER_BOOK("10002003", "自主交易议价-车船报价摘牌拨打电话", "bizUuid 为报价流水号"),
        SELF_TRADE_DHYJ_CARRIER("10003000", "自主交易电话议价-车船拨打电话", "bizUuid 为货源流水号"),
        SELF_TRADE_DHYJ_CARGO("100030001", "自主交易电话议价-货方拨打电话", "bizUuid 为货源流水号"),;
        private String code;
        private String des1;
        private String des2;

        BizUuid(String code, String des1, String des2) {
            this.code = code;
            this.des1 = des1;
            this.des2 = des2;
        }
    }
}
