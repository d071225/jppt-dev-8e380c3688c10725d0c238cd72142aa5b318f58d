package com.hletong.hyc.model;

import java.util.List;

/**
 * Created by ddq on 2017/3/28.
 * 货源的报价信息
 */

public class QuoteInfos{
    private Source cargoMap;
    private List<Quote> quoteList;

    public Source getCargoMap() {
        return cargoMap;
    }

    public List<Quote> getQuoteList() {
        return quoteList;
    }
}
