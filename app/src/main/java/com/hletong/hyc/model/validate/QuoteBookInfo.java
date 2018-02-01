package com.hletong.hyc.model.validate;

import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by dongdaqing on 2017/5/3.
 * 报价摘牌，非报价
 */

public class QuoteBookInfo implements PayMode {
    private int payMode;
    private String quoteUuid;

    public QuoteBookInfo(String quoteUuid) {
        this.quoteUuid = quoteUuid;
        payMode = 0;//默认自己付款
    }

    @Override
    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

    @Override
    public int getPayMode() {
        return payMode;
    }

    public void setQuoteUuid(String quoteUuid) {
        this.quoteUuid = quoteUuid;
    }

    @Override
    public OkRequest validate(IBaseView baseView) {
        if (Validator.isNotNull(quoteUuid, baseView, "报价流水号为空")) {
            return EasyOkHttp
                    .get(Constant.QUOTE_TRADE_BOOK)
                    .param("payMode", payMode)
                    .param("quoteUuid", quoteUuid)
                    .build();
        }
        return null;
    }
}
