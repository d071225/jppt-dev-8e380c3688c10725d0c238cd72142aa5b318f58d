package com.hletong.hyc.presenter;

import android.content.Intent;

import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.model.CallInfo;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

import static com.hletong.hyc.contract.SourceContract.SOURCE_LOADED;

/**
 * Created by dongdaqing on 2017/8/7.
 * 货源详情 - 摘牌部分
 */

public class BookSourcePresenter extends SourcePresenter {
    private String cargoUuid;
    private String cargoSrcType;
    private String quoteUuid;
    private int transType;

    public BookSourcePresenter(SourceContract.View view, int type, String cargoUuid, String cargoSrcType, String quoteUuid, int transType) {
        super(view, type);
        this.cargoUuid = cargoUuid;
        this.cargoSrcType = cargoSrcType;
        this.quoteUuid = quoteUuid;
        this.transType = transType;
    }

    @Override
    public void start() {
        //注册广播接收器
        getView().register(
                BookContract.QUOTE_INFO_CHANGED,//报价信息发生了变化
                BookContract.BID_INFO_CHANGED);//平台货源竞价信息发生了变化

        OkRequest request = EasyOkHttp
                .get(Constant.CARGO_DETAIL_GUAJIA)
                .param("cargoUuid", cargoUuid)
                .param("cargoSrcType", cargoSrcType)
                .build();

        new ExecutorCall<Source>(request).enqueue(new EasyCallback<Source>(getView()) {
            @Override
            public void onSuccess(OkCall<Source> okCall, Source response) {
                response.setTrans_type(transType);
                init(response, response.getCargo_code());
            }
        });
    }

    @Override
    protected void init(Source source, String docNo) {
        super.init(source, docNo);
        //发出货源已加载通知
        Intent intent = new Intent(SOURCE_LOADED);
        intent.putExtra("source", source);
        getView().broadcast(intent);
    }

    @Override
    protected CallInfo quoteChanged(Quote quote, Source source) {
        //车船议价进度的case,详见CarrierQuotePresenter
        if (quote.isStatus("00")) {
            //货方还未同意报价
            return CallInfo.QUOTE_PROGRESS(source.getLoadingContact(), source.getCargo_owner_code(), source.getLoading_contacts_tel(), quoteUuid);
        } else if (quote.isStatus("10")) {
            //货方已同意报价，
            return CallInfo.QUOTE_BOOK(source.getLoadingContact(), source.getCargo_owner_code(), source.getLoading_contacts_tel(), quoteUuid);
        }
        return null;
    }

    @Override
    protected CallInfo getCallInfo(Source source, int type) {
        if (type == GUAJIA) {
            if (source.getBilling_type() == 3)
                return CallInfo.BOOK(source);//自主交易拨号
        } else if (type == QUOTE) {
            if (source.getTrans_type() == 300 && "4".equals(source.getCargo_src_type())) {
                //国联议价
                return CallInfo.QUOTE_WITH_PHONE(source.getLoadingContact(), source.getCargo_owner_code(), source.getLoading_contacts_tel(), source.getCargo_uuid());
            } else {
                //正常议价
                return CallInfo.QUOTE(source);
            }
        } else if (type == QUOTE_PROGRESS) {
            return CallInfo.QUOTE_PROGRESS(source.getLoadingContact(), source.getCargo_owner_code(), source.getLoading_contacts_tel(), quoteUuid);
        }
        return null;
    }

    @Override
    protected String getTitle(int type) {
        switch (type) {
            case GUAJIA:
                return "竞价投标";
            case CB:
                return "挂价摘牌";
            case QUOTE:
                return "报价";
            default:
                return null;
        }
    }
}
