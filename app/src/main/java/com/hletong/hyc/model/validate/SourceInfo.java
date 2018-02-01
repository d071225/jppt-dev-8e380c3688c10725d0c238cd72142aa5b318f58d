package com.hletong.hyc.model.validate;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.hletong.hyc.http.parse.DataStrParse;
import com.hletong.hyc.model.BidInfo;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.HttpParser;
import com.xcheng.okhttp.request.OkRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ddq on 2017/1/5.
 * 摘牌界面需要提交信息的基类
 */

public abstract class SourceInfo {
    public static final int CB = 1;
    public static final int GJ = 2;
    public static final int QUOTE = 3;

    private int type;//1 => 竞价, 2 => 挂价,3 =>议价
    private String cargo;
    private String bidPrice;
    private Source.DeductRt deductTaxRt;//只有在特定情况下才会有抵扣税率
    //以下信息不需要校验
    private boolean shouldUploadDeductTaxRt;
    private String cargoUuid;//货源Id
    private String roundUuid;//场次Id
    private double rem_qt;//货源剩余量
    private String unit;//货源单位
    private String memberCode;
    private int transportType;//运输方式
    private int bookType = -1;//重量还是数量
    private boolean selfTrade;//是不是自主交易
    private String wrtrServUnitAmt;//兜底服务单价

    public SourceInfo(String cargo, String bidPrice, Source.DeductRt deductTaxRt, int type, int transportType) {
        this.cargo = cargo;
        this.bidPrice = bidPrice;
        this.deductTaxRt = deductTaxRt;
        this.type = type;
        this.transportType = transportType;
    }

    public void setBookType(int bookType) {
        this.bookType = bookType;
    }

    public void setSelfTrade(boolean selfTrade) {
        this.selfTrade = selfTrade;
    }

    public void setExtras(String roundUuid, String cargoUuid, boolean shouldUploadDeductTaxRt, double rem_qt, String unit, double wrtrServUnitAmt) {
        this.roundUuid = roundUuid;
        this.cargoUuid = cargoUuid;
        this.shouldUploadDeductTaxRt = shouldUploadDeductTaxRt;
        this.rem_qt = rem_qt;
        this.unit = unit;
        this.wrtrServUnitAmt = String.valueOf(wrtrServUnitAmt);
    }

    public void setExtras(String memberCode, String cargoUuid, double rem_qt, String unit) {
        this.memberCode = memberCode;
        this.cargoUuid = cargoUuid;
        this.rem_qt = rem_qt;
        this.unit = unit;
    }

    /**
     * 平开时使用
     *
     * @param bidPrice
     */
    public void setBidPrice(BidInfo bidPrice) {
        if (bidPrice == null) {
            this.bidPrice = null;
        } else {
            this.bidPrice = bidPrice.getSum();
        }
    }

    @CallSuper
    protected boolean validateInternal(IBaseView baseView) {
        if (Validator.isNotNull(getWeight(), baseView, "请输入承运重量")) {
            double weight = Double.parseDouble(getWeight());
            String message = transportType == 1 ? "车辆最大摘牌量为100吨" : String.format(Locale.getDefault(), "当前船舶最大摘牌量为%.3f吨", getCarrierLoad());

            if (Validator.isNoLargerThan(weight, getCarrierLoad(), baseView, message)
                    && Validator.isNotNull(getNumber(), baseView, "请输入承运数量")
                    && Validator.isNoLargerThan(Double.parseDouble(cargo), rem_qt, baseView, "最大可摘牌量为" + rem_qt + unit)
                    && (type == 2 || Validator.isNotNull(bidPrice, baseView, type == 1 ? "请输入竞价价格" : "请输入议价价格"))
                    && (!shouldUploadDeductTaxRt || Validator.isNotNull(deductTaxRt, baseView, "请选择抵扣税率"))) {
                return true;
            }
        }

        return false;
    }

    private String getWeight() {
        if (bookType == -1)
            throw new IllegalStateException("bookType == -1");
        return bookType == 0 ? cargo : "0";
    }

    private String getNumber() {
        if (bookType == -1)
            throw new IllegalStateException("bookType == -1");
        return bookType == 1 ? cargo : "0";
    }

    private Map<String, String> getRequestParams(IBaseView baseView) {
        if (Validator.isNotNull(cargoUuid, baseView, "摘牌参数出错，请退出页面重试")
                && Validator.isNotNull(roundUuid, baseView, "货源场次已结束，请稍后再试")) {
            Map<String, String> map = new HashMap<>();
            map.put("cargoUuid", cargoUuid);
            map.put("roundUuid", roundUuid);
            map.put("wrtrServUnitAmt", wrtrServUnitAmt);
            map.put("delistItems[0].carrierMemberSubCode", getCarrierMemberSubCode());
            map.put("delistItems[0].carrierNo", getCarrierNo());
            map.put("delistItems[0].bookWeight", getWeight());
            map.put("delistItems[0].bookUnitCt", getNumber());

            if (type == 1) {//竞价的时候多两个参数
                map.put("delistItems[0].undefined", "0");
                map.put("delistItems[0].bookUnitAmt", bidPrice);
            }

            //添加抵扣税率
            if (shouldUploadDeductTaxRt) {
                map.put("deductTaxRt", deductTaxRt.getId());
            }
            //如果要添加额外的参数，重写paramsPrepared方法
            addExtras(map);
            return map;
        }
        return null;
    }

    private Map<String, String> getQuoteParams(IBaseView baseView) {
        if (cargoUuid == null || memberCode == null) {
            baseView.handleError(ErrorFactory.getParamError("报价参数出错，请退出页面重试"));
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("cargoUuid", cargoUuid);
        map.put("memberCode", memberCode);
        map.put("items[0].memberSubCode", getCarrierMemberSubCode());
        map.put("items[0].carrierNo", getCarrierNo());
        map.put("items[0].unitCt", getNumber());
        map.put("items[0].weight", getWeight());
        map.put("items[0].unitAmt", bidPrice);
        return map;
    }

    public abstract String getCarrierMemberSubCode();

    protected abstract String getCarrierNo();

    protected abstract double getCarrierLoad();//承运车船的荷载

    //提交竞价摘牌信息的请求参数
    protected final OkRequest getBiddingRequest(IBaseView baseView) {
        Map<String, String> object = getRequestParams(baseView);
        if (object == null)//摘牌请求没有参数是不行的
            return null;

        return EasyOkHttp
                .get(Constant.COMPETITIVE_BIDDING)
                .params(object)
                .tag(baseView.hashCode())
                .build();
    }

    //提交挂价摘牌信息的请求参数
    protected final OkRequest getRequest(IBaseView baseView) {
        Map<String, String> object = getRequestParams(baseView);
        if (object == null)//摘牌请求没有参数是不行的
            return null;

        //自主交易和非自主交易解析的内容不一样
        if (selfTrade) {
            return EasyOkHttp
                    .get(Constant.GUAJIA)
                    .params(object)
                    .tag(baseView.hashCode())
                    .build();
        } else {
            return EasyOkHttp
                    .get(Constant.GUAJIA)
                    .params(object)
                    .parserFactory(new HttpParser.Factory() {
                        @NonNull
                        @Override
                        public HttpParser<?> parser(OkRequest request) {
                            return DataStrParse.INSTANCE;
                        }
                    })
                    .tag(baseView.hashCode())
                    .build();
        }
    }

    protected final OkRequest getQuoteRequest(IBaseView baseView) {
        Map<String, String> object = getQuoteParams(baseView);
        if (object == null)//摘牌请求没有参数是不行的
            return null;

        return EasyOkHttp
                .get(Constant.SUBMIT_QUOTE)
                .params(object)
                .tag(baseView.hashCode())
                .build();
    }

    //如果要添加额外的参数，重写本方法
    protected void addExtras(Map<String, String> params) {

    }
}
